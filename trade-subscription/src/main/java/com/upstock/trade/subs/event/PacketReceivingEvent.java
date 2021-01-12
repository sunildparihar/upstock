package com.upstock.trade.subs.event;

import com.upstock.trade.commons.pojo.OHLCPacket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class PacketReceivingEvent {

    private String symbol;

    private int interval;

    private OHLCPacket eventData;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketReceivingEvent that = (PacketReceivingEvent) o;
        return interval == that.interval &&
                Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {

        return Objects.hash(symbol, interval);
    }
}
