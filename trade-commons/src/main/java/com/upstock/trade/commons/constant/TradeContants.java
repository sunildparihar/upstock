package com.upstock.trade.commons.constant;

import java.math.BigInteger;

public final class TradeContants {

    private TradeContants() {
    }

    public static final long NANOSEC_IN_A_SECOND = BigInteger.valueOf(1000l * 1000l * 1000l).longValue();
    public static final String EVENT_NOTIFY = "ohlc_notify";
    public static final String EVENT_SUBSCRIBE = "subscribe";
}
