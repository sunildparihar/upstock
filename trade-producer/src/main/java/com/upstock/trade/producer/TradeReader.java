package com.upstock.trade.producer;

import com.upstock.trade.commons.pojo.Trade;

public interface TradeReader extends AutoCloseable {
    /**
     *
     * @return Next Available Trade. Returns an empty Trade (to mark the end) if end of file has occurred with an empty line
     */
    Trade readNext();
}
