package com.upstock.trade.subs;

import com.upstock.trade.commons.pojo.OHLCPacket;
import com.upstock.trade.subs.listener.OHLCPacketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class OHLCPacketSubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(OHLCPacketSubscriptionService.class);

    private ConcurrentHashMap<String, Set<OHLCPacketListener>> symbolToOHLCPacketListenerMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ReadWriteLock> symbolToLockMap = new ConcurrentHashMap<>();

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
                        TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(taskQueueSize), new ThreadPoolExecutor.AbortPolicy());
    }

    public void addNewListener(OHLCPacketListener ohlcPacketListener) {
        String tradingSymbol = ohlcPacketListener.getTradingSymbolToListen();
        if (!symbolToOHLCPacketListenerMap.containsKey(tradingSymbol)) {
            symbolToOHLCPacketListenerMap.putIfAbsent(tradingSymbol, new HashSet<>());
            symbolToLockMap.putIfAbsent(tradingSymbol, new ReentrantReadWriteLock());
        }
        ReadWriteLock readWriteLock = symbolToLockMap.get(tradingSymbol);
        Lock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            symbolToOHLCPacketListenerMap.get(tradingSymbol).add(ohlcPacketListener);
        } finally {
            writeLock.unlock();
        }
    }

    public void removeListener(OHLCPacketListener ohlcPacketListener) {
        Set<OHLCPacketListener> listeners = symbolToOHLCPacketListenerMap.get(ohlcPacketListener.getTradingSymbolToListen());
        if (!CollectionUtils.isEmpty(listeners)) {
            ReadWriteLock readWriteLock = symbolToLockMap.get(ohlcPacketListener.getTradingSymbolToListen());
            Lock writeLock = readWriteLock.writeLock();
            writeLock.lock();
            try {
                listeners.remove(ohlcPacketListener);
            } finally {
                writeLock.unlock();
            }
        }
    }

    public void notifyAllListenersAsync(OHLCPacket ohlcPacket) {
        String tradingSymbol = ohlcPacket.getSymbol();
        ReadWriteLock readWriteLock = symbolToLockMap.get(tradingSymbol);
        if (!Objects.isNull(readWriteLock)) {
            notifyPoolExecutor.submit(() -> invokeListeners(readWriteLock, tradingSymbol, ohlcPacket));
        }
    }

    private void invokeListeners(ReadWriteLock readWriteLock, String tradingSymbol, OHLCPacket ohlcPacket) {
        Optional<Set<OHLCPacketListener>> ohlcPacketListeners;
        Lock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            ohlcPacketListeners = Optional.ofNullable(symbolToOHLCPacketListenerMap.get(tradingSymbol));
        } finally {
            readLock.unlock();
        }

        ohlcPacketListeners.ifPresent(listeners -> listeners.forEach(listener -> listener.onPacketReceived(ohlcPacket)));
    }
}
