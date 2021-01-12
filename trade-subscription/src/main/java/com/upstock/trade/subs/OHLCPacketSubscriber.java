package com.upstock.trade.subs;

import com.upstock.trade.commons.pojo.OHLCPacket;

public interface OHLCPacketSubscriber {

    void handleNewPacket(OHLCPacket ohlcPacket);
}
