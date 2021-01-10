package com.upstock.trade.processor;

import com.upstock.trade.commons.pojo.Trade;

public interface TraderProcessor {
    void process(Trade trade);
}
