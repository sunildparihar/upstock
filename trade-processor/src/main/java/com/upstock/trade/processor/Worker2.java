package com.upstock.trade.processor;

import com.upstock.trade.commons.logger.ExceptionLogger;
import com.upstock.trade.commons.pojo.Trade;
import com.upstock.trade.queue.TradeRecordQueue;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker2 extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(Worker2.class);

    private TradeRecordQueue tradeRecordQueue;
    private TraderProcessor traderProcessor;

    public Worker2 (TradeRecordQueue tradeRecordQueue, TraderProcessor traderProcessor) {
        this.tradeRecordQueue = tradeRecordQueue;
        this.traderProcessor = traderProcessor;
    }

    @SneakyThrows
    @Override
    public void run() {
        try {
            Trade trade;
            do {
                trade = tradeRecordQueue.get();
                traderProcessor.process(trade);
            } while (!trade.isLastTrade());
        } catch (Exception e) {
            new ExceptionLogger().logException(e, "Exception occurred in worker2 during start..aborting!!");
            throw e;
        }
    }
}
