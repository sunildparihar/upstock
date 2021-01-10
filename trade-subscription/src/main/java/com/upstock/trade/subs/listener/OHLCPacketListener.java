package com.upstock.trade.subs.listener;

import com.upstock.trade.commons.pojo.OHLCPacket;

public interface OHLCPacketListener {

    void onPacketReceived(OHLCPacket packet);

    String getTradingSymbolToListen();
}
