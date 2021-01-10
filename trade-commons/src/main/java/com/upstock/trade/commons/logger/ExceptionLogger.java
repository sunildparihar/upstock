package com.upstock.trade.commons.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

@Component
public class ExceptionLogger {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionLogger.class);

    public void logException(Exception e, String message) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        logger.error(message + " Exception: "+sw.toString());
    }
}
