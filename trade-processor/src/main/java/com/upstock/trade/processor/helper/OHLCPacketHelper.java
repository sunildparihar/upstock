package com.upstock.trade.processor.helper;

import com.upstock.trade.commons.pojo.OHLCPacket;
import com.upstock.trade.commons.pojo.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class OHLCPacketHelper {

    private static final Logger logger = LoggerFactory.getLogger(OHLCPacketHelper.class);

    @Value("${ohlc.bar.size}")
    private int barSize;

    /**
     * Creates and returns a closing bar packet with a new fresh incoming trade that happened exactly at the 15 sec bar closing boundary.
     * @param newTrade
     * @param lastPacket
     * @return
     */
    public OHLCPacket createClosingBarPacketWithNewTrade(Trade newTrade, OHLCPacket lastPacket) {

        OHLCPacket closingPacket = createNewEmptyPacket(lastPacket.getSymbol(), lastPacket.getBarStartTime(), lastPacket.getBarNumber(), barSize);

        closingPacket.setOpen(
                Optional.ofNullable(lastPacket.getOpen()).orElse(newTrade.getPrice())
        );
        closingPacket.setHigh(
                Math.max(newTrade.getPrice(), Optional.ofNullable(lastPacket.getHigh()).orElse(newTrade.getPrice()))
        );
        closingPacket.setLow(
                Math.min(newTrade.getPrice(), Optional.ofNullable(lastPacket.getLow()).orElse(newTrade.getPrice()))
        );
        closingPacket.setClose(newTrade.getPrice());
        closingPacket.setVolume(
                newTrade.getQuantity() + Optional.ofNullable(lastPacket.getVolume()).orElse(0d)
        );
        closingPacket.setNewTradePacket(true);

        return closingPacket;
    }


    /**
     * Creates and returns a closing bar packet without any new trade. Closing bar is created based on the previous packet of the bar.
     * @param lastPacket
     * @return
     */
    public Optional<OHLCPacket> createClosingBarPacketWithoutNewTrade(OHLCPacket lastPacket) {
        /*
         * If there were not any trades in the 15 seconds bar, an empty closing packet need not to be created
         */
        if (isPacketEmpty(lastPacket)) {
            return Optional.empty();
        }

        OHLCPacket closingPacket = createNewEmptyPacket(lastPacket.getSymbol(), lastPacket.getBarStartTime(), lastPacket.getBarNumber(), barSize);
        closingPacket.setOpen(lastPacket.getOpen());
        closingPacket.setHigh(lastPacket.getHigh());
        closingPacket.setLow(lastPacket.getLow());
        closingPacket.setClose(lastPacket.getLastTradedPrice());
        closingPacket.setVolume(lastPacket.getVolume());
        return Optional.of(closingPacket);
    }

    /**
     * Creates a new empty Bar packet by incrementing bar number from previous packet.
     * @param previousPacket
     * @param barSizeInNanos
     * @return
     */
    public OHLCPacket createEmptyNewBarPacket(OHLCPacket previousPacket, BigInteger barSizeInNanos) {

        long newBarStartTime = BigInteger.valueOf(previousPacket.getBarStartTime())
                .add(barSizeInNanos)
                .add(BigInteger.ONE)
                .longValue();

        return createNewEmptyPacket(previousPacket.getSymbol(), newBarStartTime, previousPacket.getBarNumber() + 1, barSize);
    }

    /**
     * Creates the very first packet (bar num 1) based on the first trade for any symbol.
     * @param newTrade
     * @return
     */
    public OHLCPacket createFirstPacketWithNewTrade(Trade newTrade) {

        OHLCPacket ohlcPacket = createNewEmptyPacket(newTrade.getSymbol(), newTrade.getTs().longValue(), 1, barSize);
        ohlcPacket.setOpen(newTrade.getPrice());
        ohlcPacket.setHigh(newTrade.getPrice());
        ohlcPacket.setLow(newTrade.getPrice());
        ohlcPacket.setClose(0d);
        ohlcPacket.setVolume(newTrade.getQuantity());
        ohlcPacket.setLastTradedPrice(newTrade.getPrice());
        ohlcPacket.setNewTradePacket(true);

        return ohlcPacket;

    }

    /**
     * Creates and returns a new packet for the existing bar with a new incoming trade.
     * @param newTrade
     * @param lastPacket
     * @return
     */
    public OHLCPacket createNextPacketWithNewTrade(Trade newTrade, OHLCPacket lastPacket) {

        OHLCPacket nextPacket = createNewEmptyPacket(lastPacket.getSymbol(), lastPacket.getBarStartTime(), lastPacket.getBarNumber(), barSize);

        nextPacket.setOpen(
                Optional.ofNullable(lastPacket.getOpen()).orElse(newTrade.getPrice())
        );
        nextPacket.setHigh(
                Math.max(newTrade.getPrice(), Optional.ofNullable(lastPacket.getHigh()).orElse(newTrade.getPrice()))
        );
        nextPacket.setLow(
                Math.min(newTrade.getPrice(), Optional.ofNullable(lastPacket.getLow()).orElse(newTrade.getPrice()))
        );
        nextPacket.setClose(0d);
        nextPacket.setVolume(
                BigDecimal.valueOf(newTrade.getQuantity())
                        .add(BigDecimal.valueOf(Optional.ofNullable(lastPacket.getVolume()).orElse(0d)))
                        .doubleValue()
        );
        nextPacket.setLastTradedPrice(newTrade.getPrice());
        nextPacket.setNewTradePacket(true);

        return nextPacket;
    }

    /**
     * Identify and marks the last OHLC packet of the series when end of trading file has occurred.
     * @param currentTrade
     * @param currentPacketList
     * @param previousPacketList
     */
    public void markLastPacketOfSeries(Trade currentTrade, List<OHLCPacket> currentPacketList, List<OHLCPacket> previousPacketList) {
        Optional<OHLCPacket> lastPacketOfSeries = Optional.empty();
        if (currentTrade.isEmpty()) {
            lastPacketOfSeries = Optional.ofNullable(CollectionUtils.lastElement(previousPacketList));
        } else if(currentTrade.isLastTrade()) {
            lastPacketOfSeries = Optional.of(CollectionUtils.lastElement(currentPacketList));
        }
        lastPacketOfSeries.ifPresent(packet -> packet.setLastPacket(true));
    }

    private boolean isPacketEmpty(OHLCPacket ohlcPacket) {
        return Objects.isNull(ohlcPacket) || Objects.isNull(ohlcPacket.getOpen());
    }

    private OHLCPacket createNewEmptyPacket(String symbol, long barStartTime, int barNumber, int barSize) {
        OHLCPacket ohlcPacket = new OHLCPacket();
        ohlcPacket.setSymbol(symbol);
        ohlcPacket.setBarStartTime(barStartTime);
        ohlcPacket.setBarNumber(barNumber);
        ohlcPacket.setBarSizeInSeconds(barSize);
        return ohlcPacket;
    }

}
