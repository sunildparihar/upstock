package com.upstock.trade.queue;

import com.upstock.trade.commons.pojo.OHLCPacket;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * A wrapper to ohlc queue. The underlying implementation of queue can be replaced with any messaging queue.
 */
@Component
public class OHLCPacketQueue {

    @Value("${queue.ohlc.capacity}")
    private int queueCapacity;

    private BlockingQueue<OHLCPacket> dataQueue;

    @PostConstruct
    public void init() {
        dataQueue = new ArrayBlockingQueue<>(queueCapacity);
    }

    @SneakyThrows
    public OHLCPacket get() {
        return dataQueue.take();
    }

    @SneakyThrows
    public void put(OHLCPacket ohlcPacket) {
        dataQueue.put(ohlcPacket);
    }
}
