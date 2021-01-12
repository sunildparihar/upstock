package com.upstock.trade.processor;

import com.upstock.trade.commons.logger.ExceptionLogger;
import com.upstock.trade.commons.pojo.OHLCPacket;
import com.upstock.trade.commons.pojo.Trade;
import com.upstock.trade.processor.helper.OHLCPacketHelper;
import com.upstock.trade.processor.publish.OHLCPacketPublisher;
import com.upstock.trade.queue.OHLCPacketQueue;
import com.upstock.trade.queue.TradeRecordQueue;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Worker2 reads trades from TradeRecordQueue. Hands over trades to Trade Processor and pushes the ohlc packet list output to the ohlc packet queue.
 */
@Component
public class Worker2 implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Worker2.class);

    @Value("${ohlc.bar.size}")
    private int barSize;

    @Autowired
    private TradeRecordQueue tradeRecordQueue;

    @Autowired
    private OHLCPacketPublisher ohlcPacketPublisher;

    @SneakyThrows
    @Override
    public void run() {
        OHLCPacketHelper ohlcPacketHelper = new OHLCPacketHelper(barSize);
        TraderProcessor traderProcessor = new DefaultTradeProcessor(barSize);
        try {
            Trade trade;
            List<OHLCPacket> previousPacketList = new ArrayList<>();
            do {
                trade = tradeRecordQueue.get();
                List<OHLCPacket> packetList = traderProcessor.process(trade);
                ohlcPacketHelper.markLastPacketOfSeries(trade, packetList, previousPacketList);
                packetList.stream().forEachOrdered(ohlcPacketPublisher::publishNewPacket);
                previousPacketList = packetList;
            } while (!trade.isLastTrade());
        } catch (Exception e) {
            new ExceptionLogger().logException(e, "Exception occurred in worker2 during start..aborting!!");
            throw e;
        }
    }
}
