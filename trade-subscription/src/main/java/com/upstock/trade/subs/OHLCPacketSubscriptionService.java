package com.upstock.trade.subs;

import com.upstock.trade.commons.pojo.OHLCPacket;
import com.upstock.trade.subs.listener.OHLCPacketListener;
import com.upstock.trade.subs.event.PacketReceivingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * An OHLC packet subscription service implementation for
 *
 *  1. Tracking and managing all the listeners.
 *  2. Generating Packet Receiving Event and handing over new events to a packet transmission thread pool so that worker3 don't block upon listener's action completion.
 *
 */
@Component
public class OHLCPacketSubscriptionService implements OHLCPacketSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(OHLCPacketSubscriptionService.class);

    private List<OHLCPacketListener> ohlcPacketListeners = new CopyOnWriteArrayList<>();

    private ThreadPoolExecutor notifyPoolExecutor;

    @Value("${notify.pool.coresize}")
    private int corePoolSize;

    @Value("${notify.pool.maxsize}")
    private int maxPoolSize;

    @Value("${notify.pool.keepAliveSeconds}")
    private int keepAliveTime;

    @Value("${notify.pool.task.queue.capacity}")
    private int taskQueueSize;

    @PostConstruct
    public void init() {
        notifyPoolExecutor =
                new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
                        TimeUnit.SECONDS, new ArrayBlockingQueue<>(taskQueueSize), new ThreadPoolExecutor.AbortPolicy());
    }

    public void addNewListener(OHLCPacketListener ohlcPacketListener) {
        ohlcPacketListeners.add(ohlcPacketListener);
    }

    public void removeListener(OHLCPacketListener ohlcPacketListener) {
        ohlcPacketListeners.remove(ohlcPacketListener);
    }

    private void handleNewPacketReceivedAsync(OHLCPacket ohlcPacket) {
        PacketReceivingEvent event = generateEvent(ohlcPacket);
        notifyPoolExecutor.submit(
                () -> ohlcPacketListeners.forEach(listener -> listener.onEvent(event))
        );
    }

    private PacketReceivingEvent generateEvent(OHLCPacket ohlcPacket) {
        PacketReceivingEvent packetReceivingEvent = new PacketReceivingEvent();
        packetReceivingEvent.setInterval(ohlcPacket.getBarSizeInSeconds());
        packetReceivingEvent.setSymbol(ohlcPacket.getSymbol());
        packetReceivingEvent.setEventData(ohlcPacket);
        return packetReceivingEvent;
    }

    @Override
    public void handleNewPacket(OHLCPacket ohlcPacket) {
        handleNewPacketReceivedAsync(ohlcPacket);
    }
}
