package com.upstock.trade.perf;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PerformanceStat {

    private int totalActiveSubscriptions;
    private int totalActiveSessions;
    private int totalTradesProcessed;
    private long totalTimeTakenInSeconds;
    private double tradesProcessedPerSecond;

    @Override
    public String toString() {
        return "PerformanceStat{" +
                "totalActiveSubscriptions=" + totalActiveSubscriptions +
                ", totalActiveSessions=" + totalActiveSessions +
                ", totalTradesProcessed=" + totalTradesProcessed +
                ", totalTimeTakenInSeconds=" + totalTimeTakenInSeconds +
                ", tradesProcessedPerSecond=" + tradesProcessedPerSecond +
                '}';
    }
}
