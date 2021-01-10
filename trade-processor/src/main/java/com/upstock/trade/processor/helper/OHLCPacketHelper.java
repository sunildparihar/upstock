package com.upstock.trade.processor.helper;

import com.upstock.trade.commons.pojo.OHLCPacket;
import com.upstock.trade.commons.pojo.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

@Component
public class OHLCPacketHelper {

    private static final Logger logger = LoggerFactory.getLogger(OHLCPacketHelper.class);

    @Value("${ohlc.bar.size}")
    private int barSize;

    public OHLCPacket createClosingBarPacketWithNewTrade(Trade newTrade, OHLCPacket lastPacket) {

        OHLCPacket closingPacket = OHLCPacket.createNewEmptyPacket(lastPacket.getSymbol(), lastPacket.getBarStartTime(), lastPacket.getBarNumber(), barSize);

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

        return closingPacket;
    }


    public Optional<OHLCPacket> createClosingBarPacketWithoutNewTrade(OHLCPacket lastPacket) {
        /*
         * If there were not any trades in the 15 seconds bar, an empty closing packet need not to be created
         */
        if (isPacketEmpty(lastPacket)) {
            return Optional.empty();
        }

        OHLCPacket closingPacket = OHLCPacket.createNewEmptyPacket(lastPacket.getSymbol(), lastPacket.getBarStartTime(), lastPacket.getBarNumber(), barSize);
        closingPacket.setOpen(lastPacket.getOpen());
        closingPacket.setHigh(lastPacket.getHigh());
        closingPacket.setLow(lastPacket.getLow());
        closingPacket.setClose(lastPacket.getLastTradedPrice());
        closingPacket.setVolume(lastPacket.getVolume());
        return Optional.of(closingPacket);
    }

    public OHLCPacket createEmptyNewBarPacket(OHLCPacket previousPacket, BigInteger barSizeInNanos) {

        long newBarStartTime = BigInteger.valueOf(previousPacket.getBarStartTime())
                .add(barSizeInNanos)
                .add(BigInteger.ONE)
                .longValue();

        return OHLCPacket.createNewEmptyPacket(previousPacket.getSymbol(), newBarStartTime, previousPacket.getBarNumber() + 1, barSize);
    }

    public OHLCPacket createFirstPacketWithNewTrade(Trade newTrade) {

        OHLCPacket ohlcPacket = OHLCPacket.createNewEmptyPacket(newTrade.getSymbol(), newTrade.getTs().longValue(), 1, barSize);
        ohlcPacket.setOpen(newTrade.getPrice());
        ohlcPacket.setHigh(newTrade.getPrice());
        ohlcPacket.setLow(newTrade.getPrice());
        ohlcPacket.setClose(0d);
        ohlcPacket.setVolume(newTrade.getQuantity());
        ohlcPacket.setLastTradedPrice(newTrade.getPrice());

        return ohlcPacket;

    }

    public OHLCPacket createNextPacketWithNewTrade(Trade newTrade, OHLCPacket lastPacket) {

        OHLCPacket nextPacket = OHLCPacket.createNewEmptyPacket(lastPacket.getSymbol(), lastPacket.getBarStartTime(), lastPacket.getBarNumber(), barSize);

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

        return nextPacket;
    }

    private boolean isPacketEmpty(OHLCPacket ohlcPacket) {
        return Objects.isNull(ohlcPacket) || Objects.isNull(ohlcPacket.getOpen());
    }

}
