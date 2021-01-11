package com.upstock.trade.producer;

import com.upstock.trade.commons.logger.ExceptionLogger;
import com.upstock.trade.commons.pojo.Trade;
import com.upstock.trade.queue.TradeRecordQueue;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Worker1 reads trades from TreadReader and pushes to the TradeRecordQueue
 */
@Component
public class Worker1 implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Worker1.class);

    @Value("classpath:trades.json")
    private Resource tradesFile;

    @Autowired
    private TradeRecordQueue tradeRecordQueue;

    @SneakyThrows
    @Override
    public void run() {
        try (
                TradeReader tradeReader = new DefaultTradeReader(tradesFile.getInputStream())
        ) {
            boolean isLastRecord = false;
            while (!isLastRecord) {
                Trade trade;
                try {
                    trade = tradeReader.readNext();
                    tradeRecordQueue.put(trade);
                    isLastRecord = trade.isLastTrade();
                } catch (Exception e) {
                    new ExceptionLogger().logException(e, "Exception occurred in reader while producing data. Skipping this record.");
                }
            }
            logger.info("###### All records read");
        } catch (Exception e) {
            new ExceptionLogger().logException(e, "Exception occurred in worker1 during start..aborting!!");
            throw e;
        }
    }
}
