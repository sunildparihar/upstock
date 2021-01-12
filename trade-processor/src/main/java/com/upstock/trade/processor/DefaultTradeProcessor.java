package com.upstock.trade.processor;

import com.upstock.trade.commons.pojo.OHLCPacket;
import com.upstock.trade.commons.pojo.Trade;
import com.upstock.trade.processor.helper.TradeProcessorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DefaultTradeProcessor implements TraderProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTradeProcessor.class);

    private TradeProcessorHelper tradeProcessorHelper;

    public DefaultTradeProcessor(int barSize) {
        this.tradeProcessorHelper = new TradeProcessorHelper(barSize);
    }

    @Override
    public List<OHLCPacket> process(Trade newTrade) {

        List<OHLCPacket> packetList = new ArrayList<>();

        try {
            if (newTrade.isEmpty()) { // this means end of trading file with an empty line.
                return packetList;
            }

            // check for closing bar boundaries for all other trading symbols and create closing bars and new empty bars
            tradeProcessorHelper.processClosingBarsForOtherTradingSymbols(newTrade, packetList);

            // create ohlc packets for incoming trading symbol
            tradeProcessorHelper.processNewTrade(newTrade, packetList);

        } finally {
            // check if the last trade has already arrived. Close all other bars without waiting for 15 sec boundary as no more trades are expected.
            tradeProcessorHelper.checkAndCloseAllTrades(newTrade, packetList);
        }

        return packetList;
    }

}
