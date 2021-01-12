package com.upstock.trade.processor.publish;

import com.upstock.trade.commons.pojo.OHLCPacket;

/**
 * An publisher to publish new packets received.
 * Without affecting rest of the application a publisher can be implemented that publishes packets to a message broker topic.
 */
public interface OHLCPacketPublisher {

    void publishNewPacket(OHLCPacket ohlcPacket);
}
