package com.upstock.trade.subs.listener;

import com.upstock.trade.subs.event.PacketReceivingEvent;

public interface OHLCPacketListener {

    /**
     * Action to be taken when an packet receiving event generates
     * @param event
     */
    void onEvent(PacketReceivingEvent event);

}
