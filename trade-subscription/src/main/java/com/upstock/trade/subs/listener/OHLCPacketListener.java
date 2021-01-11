package com.upstock.trade.subs.listener;

import com.upstock.trade.commons.pojo.OHLCPacket;

public interface OHLCPacketListener {

    /**
     * Action to be taken when an ohlc packet arrives
     * @param packet
     */
    void onPacketReceived(OHLCPacket packet);

    String getTradingSymbolToListen();
}
