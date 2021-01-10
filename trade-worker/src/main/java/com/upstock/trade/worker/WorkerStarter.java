package com.upstock.trade.worker;

import com.upstock.trade.processor.TraderProcessor;
import com.upstock.trade.processor.Worker2;
import com.upstock.trade.producer.Worker1;
import com.upstock.trade.queue.OHLCPacketQueue;
import com.upstock.trade.queue.TradeRecordQueue;
import com.upstock.trade.subs.OHLCPacketSubscriptionService;
import com.upstock.trade.subs.Worker3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;

@Component
public class WorkerStarter {

    private static final Logger logger = LoggerFactory.getLogger(WorkerStarter.class);

    @Value("classpath:trades.json")
    private Resource tradesFile;

    @Autowired
    private TradeRecordQueue tradeRecordQueue;

    @Autowired
    private TraderProcessor traderProcessor;

    @Autowired
    private OHLCPacketQueue ohlcPacketQueue;

    @Autowired
    private OHLCPacketSubscriptionService ohlcPacketSubscriptionService;

    public void startAll() throws IOException {
        logger.info("###### Starting Workers...");
        new Worker1(tradesFile.getInputStream(), tradeRecordQueue).start();
        new Worker2(tradeRecordQueue, traderProcessor).start();
        new Worker3(ohlcPacketQueue, ohlcPacketSubscriptionService).start();
    }
}
