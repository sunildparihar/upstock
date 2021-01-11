package com.upstock.trade.processor.helper;

import com.upstock.trade.commons.constant.TradeConstants;
import com.upstock.trade.commons.pojo.OHLCPacket;
import com.upstock.trade.commons.pojo.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TradeProcessorHelper {

    @Value("${ohlc.bar.size}")
    private int barSize;

    private BigInteger barSizeInNanos;

    @Autowired
    private OHLCPacketHelper ohlcPacketHelper;

    @PostConstruct
    public void init() {
        barSizeInNanos = BigInteger.valueOf(Math.multiplyExact(barSize, TradeConstants.NANOSEC_IN_A_SECOND));
    }

    private ConcurrentHashMap<String, OHLCPacket> symbolToPreviousPacketMap = new ConcurrentHashMap<>();

    /**
     * Creates ohlc packets for incoming trading symbol
     * @param newTrade
     * @param packetList
     */
    public void processNewTrade(Trade newTrade, List<OHLCPacket> packetList) {
        String symbol = newTrade.getSymbol();
        if (isFirstTradeOfSymbol(symbol)) {
            processFirstTradeOfSymbol(symbol, newTrade, packetList);
        } else {
            processSubsequentTradesOfSymbol(symbol, newTrade, packetList);
        }
    }

    /**
     * Checks for closing bar boundaries for all other trading symbols and create closing bars and new empty bars
     * @param newTrade
     * @param packetList
     */
    public void processClosingBarsForOtherTradingSymbols(Trade newTrade, List<OHLCPacket> packetList) {

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
                        ohlcPacketHelper.createClosingBarPacketWithoutNewTrade(previousPacket).ifPresent(packetList::add);

                        /* Step 2. */
                        OHLCPacket newEmptyOHLCPacket = ohlcPacketHelper.createEmptyNewBarPacket(previousPacket, barSizeInNanos);
                        packetList.add(newEmptyOHLCPacket);

                        symbolToPreviousPacketMap.put(previousPacket.getSymbol(), newEmptyOHLCPacket);
                    }

                });

    }

    /**
     * Checks if the last trade has already arrived. Close all other bars without waiting for 15 sec boundary as no more trades are expected.
     * @param newTrade
     * @param packetList
     */
    public void checkAndCloseAllTrades(Trade newTrade, List<OHLCPacket> packetList) {
        if (newTrade.isEmpty() || newTrade.isLastTrade()) {
            symbolToPreviousPacketMap.values().forEach(
                    packet -> ohlcPacketHelper.createClosingBarPacketWithoutNewTrade(packet).ifPresent(packetList::add)
            );
        }
    }

    private void processFirstTradeOfSymbol(String symbol, Trade firstTrade, List<OHLCPacket> packetList) {
        OHLCPacket firstPacket = ohlcPacketHelper.createFirstPacketWithNewTrade(firstTrade);
        packetList.add(firstPacket);
        symbolToPreviousPacketMap.put(symbol, firstPacket);
    }

    private void processSubsequentTradesOfSymbol(String symbol, Trade newTrade, List<OHLCPacket> packetList) {
        OHLCPacket previousPacket = symbolToPreviousPacketMap.get(symbol);

        long barStartTimeNanos = previousPacket.getBarStartTime();
        long newTradeTimeNanos = newTrade.getTs().longValue();
        long timeElapsedSinceLastBar = newTradeTimeNanos - barStartTimeNanos;

        if (timeElapsedSinceLastBar < barSizeInNanos.longValue()) {

            /*
                This trading packet is to be added to the existing bar
             */

            OHLCPacket nextPacket = ohlcPacketHelper.createNextPacketWithNewTrade(newTrade, previousPacket);
            packetList.add(nextPacket);

            symbolToPreviousPacketMap.put(symbol, nextPacket);

        } else if (timeElapsedSinceLastBar == barSizeInNanos.longValue()) {
            /*
                New trading arrives exactly at the bar closing boundary. Do the following:
                1. Create closing bar packet with the incoming new trade
                2. Create next empty bar.
             */

            /* Step 1. */
            packetList.add(ohlcPacketHelper.createClosingBarPacketWithNewTrade(newTrade, previousPacket));

            /* Step 2. */
            OHLCPacket newEmptyOHLCPacket = ohlcPacketHelper.createEmptyNewBarPacket(previousPacket, barSizeInNanos);
            packetList.add(newEmptyOHLCPacket);

            symbolToPreviousPacketMap.put(symbol, newEmptyOHLCPacket);

        } else {

            /*
                New trading arrives after the existing bar closing boundary.  Do the following:
                1. Create closing bar packet from the existing packet in the bar (without adding new trade).
                2. Create next empty bar.
                3. Create a trading packet to the new bar from incoming new trade.
             */

            /* Step 1. */
            ohlcPacketHelper.createClosingBarPacketWithoutNewTrade(previousPacket).ifPresent(packetList::add);

            /* Step 2. */
            OHLCPacket newEmptyOHLCPacket = ohlcPacketHelper.createEmptyNewBarPacket(previousPacket, barSizeInNanos);
            packetList.add(newEmptyOHLCPacket);

            /* Step 3. */
            OHLCPacket nextOHLCPacket = ohlcPacketHelper.createNextPacketWithNewTrade(newTrade, newEmptyOHLCPacket);
            packetList.add(nextOHLCPacket);

            symbolToPreviousPacketMap.put(symbol, nextOHLCPacket);
        }

    }

    private boolean isFirstTradeOfSymbol(String symbol) {
        return !symbolToPreviousPacketMap.containsKey(symbol);
    }

}
