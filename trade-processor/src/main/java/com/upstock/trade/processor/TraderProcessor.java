package com.upstock.trade.processor;

import com.upstock.trade.commons.pojo.OHLCPacket;
import com.upstock.trade.commons.pojo.Trade;

import java.util.List;

public interface TraderProcessor {
    /**
     *
     * @param trade The next incoming trade
     * @return An ordered list of OHLC packets that needs to be pushed forward in the workflow pertaining to the incoming trade.
     * Min size of this list can be zero when ending of trade file happens with an empty line.
     * The packet list can also contain closing bar OHLC packets and new empty bar packets for all of the
     * trading symbols that have already completed 15 sec bar at the time of this incoming trade.
     */
    List<OHLCPacket> process(Trade trade);
}
