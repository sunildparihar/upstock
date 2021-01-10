package com.upstock.trade.producer;

import com.upstock.trade.commons.logger.ExceptionLogger;
import com.upstock.trade.commons.pojo.Trade;
import com.upstock.trade.queue.TradeRecordQueue;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

public class Worker1 extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(Worker1.class);

    private InputStream tradesFile;
    private TradeRecordQueue tradeRecordQueue;

    public Worker1(InputStream tradesFile, TradeRecordQueue tradeRecordQueue) {
        this.tradesFile = tradesFile;
        this.tradeRecordQueue = tradeRecordQueue;
    }

    @SneakyThrows
    @Override
    public void run() {
        try (
                TradeProducer tradeProducer = new DefaultTradeProducer(tradesFile)
        ) {
            boolean isLastRecord = false;
            while (!isLastRecord) {
                Trade trade;
                try {
                    trade = tradeProducer.produce();
                    tradeRecordQueue.put(trade);
                    isLastRecord = trade.isLastTrade();
                } catch (Exception e) {
                    new ExceptionLogger().logException(e, "Exception occurred in producer while producing data. Skipping this record.");
                }
            }
            logger.info("###### All records read");
        } catch (Exception e) {
            new ExceptionLogger().logException(e, "Exception occurred in worker1 during start..aborting!!");
            throw e;
        }
    }
}
