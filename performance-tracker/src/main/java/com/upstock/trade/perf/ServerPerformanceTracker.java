package com.upstock.trade.perf;

import com.upstock.trade.commons.logger.ExceptionLogger;
import com.upstock.trade.commons.pojo.OHLCPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class will start logging server performance statistics (at a configured interval "perf.tracker.capture.interval.millis") upon receiving first subscription from any client.
 * It stops logging after the last trade is processed.
 * It will restart logging when workers are restarted
 * Check perf.log file under the root project directory.
 * @see PerformanceStat
 */
@Component
public class ServerPerformanceTracker {

    private static final Logger logger = LoggerFactory.getLogger(ServerPerformanceTracker.class);

    @Value("${perf.tracker.stats.queue.capacity}")
    private int perfStatQueueSize;

    @Value("${perf.tracker.capture.interval.millis}")
    private int perfCaptureIntervalMillis;

    private AtomicInteger tradesProcessed = new AtomicInteger(0);
    private AtomicInteger sessionCount = new AtomicInteger(0);
    private AtomicInteger subscriptionCount = new AtomicInteger(0);
    private boolean isLastPacketProcessed;

    private BlockingQueue<PerformanceStat> loggingQueue;

    private Lock lastTradeLock = new ReentrantLock();

    private volatile Thread trackerThread;

    @PostConstruct
    private void init() {
        loggingQueue = new ArrayBlockingQueue<>(perfStatQueueSize);
        startLogger();
    }

    private void resetTradeStats() {
        tradesProcessed.set(0);
        isLastPacketProcessed = false;
    }

    public void start() {
        synchronized (this) {
            resetTradeStats();
            trackerThread = createNewTracker();
            trackerThread.setName("Tracker-"+trackerThread.getId() + System.currentTimeMillis()); //setting a unique name
            trackerThread.start();
        }
    }

    public void addTradeProcessed(OHLCPacket ohlcPacket) {
        if(ohlcPacket.isLastPacket()) {
            lastTradeLock.lock();
            try{
                isLastPacketProcessed = true;
                if(ohlcPacket.isNewTradePacket()) {
                    tradesProcessed.incrementAndGet();
                }
            } finally {
                lastTradeLock.unlock();
            }
        } else if(ohlcPacket.isNewTradePacket()) {
            tradesProcessed.incrementAndGet();
        }
    }

    public void increaseSubscriptionCount() {
        subscriptionCount.incrementAndGet();
    }

    public void decreaseSubscriptionCount() {
        subscriptionCount.decrementAndGet();
    }

    public void increaseSessionCount() {
        sessionCount.incrementAndGet();
    }

    public void decreaseSessionCount() {
        sessionCount.decrementAndGet();
    }

    private Thread createNewTracker() {
        return new Thread(() -> {
            boolean isLastTradeProcessed = false;
            long startTimeInMillis = System.currentTimeMillis();
            while (isLatestTrackerThread(Thread.currentThread()) && !isLastTradeProcessed) {
                try {
                    Thread.sleep(perfCaptureIntervalMillis);

                    if (!isLatestTrackerThread(Thread.currentThread())) {
                        break;
                    }

                    long timeElapsedSinceStart = System.currentTimeMillis() - startTimeInMillis;
                    int totalTradesCount;
                    try{
                        lastTradeLock.lock();
                        totalTradesCount = tradesProcessed.get();
                        isLastTradeProcessed = this.isLastPacketProcessed;
                    } finally {
                        lastTradeLock.unlock();
                    }
                    double tradesProcessedPerSecond =
                            BigDecimal.valueOf(totalTradesCount)
                                    .divide(
                                            BigDecimal.valueOf(timeElapsedSinceStart), 2, 1
                                    ).doubleValue()*1000;

                    long totalTimeTakenInSeconds = timeElapsedSinceStart/1000;

                    loggingQueue.put(
                            new PerformanceStat(
                                    subscriptionCount.get(),
                                    sessionCount.get(),
                                    totalTradesCount,
                                    totalTimeTakenInSeconds,
                                    tradesProcessedPerSecond)
                    );
                } catch (Exception e) {
                    new ExceptionLogger().logException(e, "Error while capturing performance stats.");
                }
            }
        });
    }

    private boolean isLatestTrackerThread(Thread thread) {
        return thread.getName().equals(trackerThread.getName());
    }

    private void startLogger() {
        new Thread(() -> {
            while (true) {
                PerformanceStat nextStat = null;
                try {
                    nextStat = loggingQueue.take();
                    logger.info(nextStat.toString());
                } catch (InterruptedException e) {
                    new ExceptionLogger().logException(e, "Error while logging performance stats.");
                }
            }
        }).start();
    }

}
