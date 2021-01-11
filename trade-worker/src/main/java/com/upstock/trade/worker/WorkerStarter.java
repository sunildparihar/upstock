package com.upstock.trade.worker;

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

    public void startAll() {
        logger.info("###### Starting Workers...");
        new Thread(worker1).start();
        new Thread(worker2).start();
        new Thread(worker3).start();
    }
}
