package com.upstock.trade.subs;

import com.upstock.trade.commons.logger.ExceptionLogger;
import com.upstock.trade.commons.pojo.OHLCPacket;
import com.upstock.trade.queue.OHLCPacketQueue;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Worker3 reads ohlc packets from local ohlc packet queue and calls the subscription service to notify all the listeners.
 * Without affecting rest of the application this can be enhanced to subscribe to a topic from a packet publishing message broker.
 */
@Component
public class Worker3 implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Worker3.class);

    @Autowired
    private OHLCPacketQueue ohlcPacketQueue;

    @Autowired
    private OHLCPacketSubscriptionService ohlcPacketSubscriptionService;

    @SneakyThrows
    @Override
    public void run() {
        OHLCPacket ohlcPacket;
        do {
            ohlcPacket = ohlcPacketQueue.get();
            try {
                ohlcPacketSubscriptionService.handleNewPacket(ohlcPacket);
            } catch (Exception e) {
                new ExceptionLogger().logException(e, "Exception occurred in worker3 skipping this record!!");
            }
        } while (!ohlcPacket.isLastPacket());
    }

}
