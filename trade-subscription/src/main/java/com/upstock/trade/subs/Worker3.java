package com.upstock.trade.subs;

import com.upstock.trade.commons.logger.ExceptionLogger;
import com.upstock.trade.commons.pojo.OHLCPacket;
import com.upstock.trade.queue.OHLCPacketQueue;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker3 extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(Worker3.class);

    private OHLCPacketQueue ohlcPacketQueue;
    private OHLCPacketSubscriptionService ohlcPacketSubscriptionService;

    public Worker3(OHLCPacketQueue ohlcPacketQueue, OHLCPacketSubscriptionService ohlcPacketSubscriptionService) {
        this.ohlcPacketQueue = ohlcPacketQueue;
        this.ohlcPacketSubscriptionService = ohlcPacketSubscriptionService;
    }

    @SneakyThrows
    @Override
    public void run() {
        try {
            while (true) {
                OHLCPacket ohlcPacket = ohlcPacketQueue.get();
                ohlcPacketSubscriptionService.notifyAllListenersAsync(ohlcPacket);
            }
        } catch (Exception e) {
            new ExceptionLogger().logException(e, "Exception occurred in worker3 during start..aborting!!");
        }
    }

}
