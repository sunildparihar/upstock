package com.upstock.trade.producer;

import com.upstock.trade.commons.pojo.Trade;

public interface TradeProducer extends AutoCloseable {
    Trade produce();
}
