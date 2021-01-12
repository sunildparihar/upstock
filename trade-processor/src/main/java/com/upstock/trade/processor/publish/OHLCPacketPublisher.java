package com.upstock.trade.processor.publish;

import com.upstock.trade.commons.pojo.OHLCPacket;

/**
 * An publisher to publish new packets received
 */
public interface OHLCPacketPublisher {

    void publishNewPacket(OHLCPacket ohlcPacket);
}
