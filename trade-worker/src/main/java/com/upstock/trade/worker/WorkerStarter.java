package com.upstock.trade.worker;

import com.upstock.trade.commons.logger.ExceptionLogger;
import com.upstock.trade.perf.ServerPerformanceTracker;
import com.upstock.trade.processor.Worker2;
import com.upstock.trade.producer.Worker1;
import com.upstock.trade.subs.Worker3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  A fully reusable worker starter.
 */
@Component
public class WorkerStarter {

    private static final Logger logger = LoggerFactory.getLogger(WorkerStarter.class);

    @Autowired
    private Worker1 worker1;

    @Autowired
    private Worker2 worker2;

    @Autowired
    private Worker3 worker3;

    @Autowired
    private ServerPerformanceTracker serverPerformanceTracker;

    private static volatile boolean isRunning;

    public void startAll() {

        if(!isRunning) {
            synchronized (WorkerStarter.class) {
                if(!isRunning) {
                    logger.info("###### Starting Workers...");
                    serverPerformanceTracker.start();
                    new Thread(() -> {

                        Thread workerThread1 = new Thread(worker1);
                        Thread workerThread2 = new Thread(worker2);
                        Thread workerThread3 = new Thread(worker3);

                        workerThread1.start();
                        workerThread2.start();
                        workerThread3.start();

                        waitForWorkerToComplete(workerThread1);
                        waitForWorkerToComplete(workerThread2);
                        waitForWorkerToComplete(workerThread3);

                        isRunning = false;

                    }).start();

                    isRunning = true;
                }
            }
        } else {
            logger.info("###### Not Starting Workers as it's already running");
        }

    }

    private void waitForWorkerToComplete(Thread workerThread) {
        try {
            workerThread.join();
        } catch (InterruptedException e) {
            new ExceptionLogger().logException(e, "Error in Workers!!");
        }
    }

}
