package com.upstock.trade.processor;

import com.upstock.trade.commons.constant.TradeContants;
import com.upstock.trade.commons.pojo.OHLCPacket;
import com.upstock.trade.commons.pojo.Trade;
import com.upstock.trade.processor.helper.OHLCPacketHelper;
import com.upstock.trade.queue.OHLCPacketQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DefaultTradeProcessor implements TraderProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTradeProcessor.class);

    @Value("${ohlc.bar.size}")
    private int barSize;

    @Autowired
    private OHLCPacketQueue ohlcPacketQueue;

    private BigInteger barSizeInNanos;

    @Autowired
    private OHLCPacketHelper ohlcPacketHelper;

    private ConcurrentHashMap<String, OHLCPacket> symbolToPreviousPacketMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        barSizeInNanos = BigInteger.valueOf(Math.multiplyExact(barSize, TradeContants.NANOSEC_IN_A_SECOND));
    }

    @Override
    public void process(Trade newTrade) {

        try {
            if (newTrade.isEmpty()) {
                return;
            }

            processClosingBarsForOtherTradingSymbols(newTrade);

            processNewTrade(newTrade);

        } finally {
            checkAndCloseAllTrades(newTrade);
        }
    }

    private void processNewTrade(Trade newTrade) {
        String symbol = newTrade.getSymbol();
        if (isFirstTradeOfSymbol(symbol)) {
            processFirstTradeOfSymbol(symbol, newTrade);
        } else {
            processSubsequentTradesOfSymbol(symbol, newTrade);
        }
    }

    private void processFirstTradeOfSymbol(String symbol, Trade firstTrade) {
        OHLCPacket firstPacket = ohlcPacketHelper.createFirstPacketWithNewTrade(firstTrade);
        ohlcPacketQueue.put(firstPacket);
        symbolToPreviousPacketMap.put(symbol, firstPacket);
    }

    private void processSubsequentTradesOfSymbol(String symbol, Trade newTrade) {
        OHLCPacket previousPacket = symbolToPreviousPacketMap.get(symbol);

        long barStartTimeNanos = previousPacket.getBarStartTime();
        long newTradeTimeNanos = newTrade.getTs().longValue();
        long timeElapsedSinceLastBar = newTradeTimeNanos - barStartTimeNanos;

        if (timeElapsedSinceLastBar < barSizeInNanos.longValue()) {

            /*
                This trading packet is to be added to the existing bar
             */

            OHLCPacket nextPacket = ohlcPacketHelper.createNextPacketWithNewTrade(newTrade, previousPacket);
            ohlcPacketQueue.put(nextPacket);

            symbolToPreviousPacketMap.put(symbol, nextPacket);

        } else if (timeElapsedSinceLastBar == barSizeInNanos.longValue()) {
            /*
                New trading arrives exactly at the bar closing boundary. Do the following:
                1. Create closing bar packet with the incoming new trade
                2. Create next empty bar.
             */

            /* Step 1. */
            ohlcPacketQueue.put(ohlcPacketHelper.createClosingBarPacketWithNewTrade(newTrade, previousPacket));

            /* Step 2. */
            OHLCPacket newEmptyOHLCPacket = ohlcPacketHelper.createEmptyNewBarPacket(previousPacket, barSizeInNanos);
            ohlcPacketQueue.put(newEmptyOHLCPacket);

            symbolToPreviousPacketMap.put(symbol, newEmptyOHLCPacket);

        } else {

            /*
                New trading arrives after the existing bar closing boundary.  Do the following:
                1. Create closing bar packet from the existing packet in the bar (without adding new trade).
                2. Create next empty bar.
                3. Create a trading packet to the new bar from incoming new trade.
             */

            /* Step 1. */
            ohlcPacketHelper.createClosingBarPacketWithoutNewTrade(previousPacket).ifPresent(ohlcPacketQueue::put);

            /* Step 2. */
            OHLCPacket newEmptyOHLCPacket = ohlcPacketHelper.createEmptyNewBarPacket(previousPacket, barSizeInNanos);
            ohlcPacketQueue.put(newEmptyOHLCPacket);

            /* Step 3. */
            OHLCPacket nextOHLCPacket = ohlcPacketHelper.createNextPacketWithNewTrade(newTrade, newEmptyOHLCPacket);
            ohlcPacketQueue.put(nextOHLCPacket);

            symbolToPreviousPacketMap.put(symbol, nextOHLCPacket);
        }

    }

    private void processClosingBarsForOtherTradingSymbols(Trade newTrade) {

        symbolToPreviousPacketMap.values().stream()
                .filter(packet -> !packet.getSymbol().equalsIgnoreCase(newTrade.getSymbol()))
                .forEach(previousPacket -> {
                    long barStartTimeNanos = previousPacket.getBarStartTime();
                    long newTradeTimeNanos = newTrade.getTs().longValue();
                    long timeLapseSinceLastBar = newTradeTimeNanos - barStartTimeNanos;

                    if (timeLapseSinceLastBar >= barSizeInNanos.longValue()) {
                    /*
                        For other trading symbols, this comes to the end of existing bar. Do below:
                        1. Create closing bar packet from the existing packet in the bar (without adding new trade as it doesn't belong to them).
                        2. Create next empty bar.
                     */

                        /* Step 1. */
                        ohlcPacketHelper.createClosingBarPacketWithoutNewTrade(previousPacket).ifPresent(ohlcPacketQueue::put);

                        /* Step 2. */
                        OHLCPacket newEmptyOHLCPacket = ohlcPacketHelper.createEmptyNewBarPacket(previousPacket, barSizeInNanos);
                        ohlcPacketQueue.put(newEmptyOHLCPacket);

                        symbolToPreviousPacketMap.put(previousPacket.getSymbol(), newEmptyOHLCPacket);
                    }

                });

    }

    private boolean isFirstTradeOfSymbol(String symbol) {
        return !symbolToPreviousPacketMap.containsKey(symbol);
    }

    private void checkAndCloseAllTrades(Trade newTrade) {
        if (newTrade.isEmpty() || newTrade.isLastTrade()) {
            symbolToPreviousPacketMap.values().forEach(
                    packet -> ohlcPacketHelper.createClosingBarPacketWithoutNewTrade(packet).ifPresent(ohlcPacketQueue::put)
            );
        }
    }
}
