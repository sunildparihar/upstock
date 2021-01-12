package com.upstock.trade.processor.publish;

import com.upstock.trade.commons.pojo.OHLCPacket;
import com.upstock.trade.queue.OHLCPacketQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * An publisher that publishes new packets to local queue.
 */
@Component
public class LocalMQPublisher implements OHLCPacketPublisher{

    @Autowired
    private OHLCPacketQueue ohlcPacketQueue;

    @Override
    public void publishNewPacket(OHLCPacket ohlcPacket) {
        ohlcPacketQueue.put(ohlcPacket);
    }
}
