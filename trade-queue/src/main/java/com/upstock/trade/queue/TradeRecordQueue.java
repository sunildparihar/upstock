package com.upstock.trade.queue;

import com.upstock.trade.commons.pojo.Trade;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * A wrapper to trade record queue. The underlying implementation of queue can be replaced with any messaging queue.
 */
@Component
public class TradeRecordQueue {

    @Value("${queue.traderecord.capacity}")
    private int queueCapacity;

    private BlockingQueue<Trade> dataQueue;

    @PostConstruct
    public void init() {
        dataQueue = new ArrayBlockingQueue<>(queueCapacity);
    }

    @SneakyThrows
    public Trade get() {
        return dataQueue.take();
    }

    @SneakyThrows
    public void put(Trade trade) {
        dataQueue.put(trade);
    }
}
