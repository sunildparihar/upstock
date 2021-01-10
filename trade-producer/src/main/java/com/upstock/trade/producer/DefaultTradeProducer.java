package com.upstock.trade.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upstock.trade.commons.logger.ExceptionLogger;
import com.upstock.trade.commons.pojo.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class DefaultTradeProducer implements TradeProducer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTradeProducer.class);

    private Scanner fileScanner;

    public DefaultTradeProducer(InputStream tradesFile) {
        fileScanner = new Scanner(tradesFile);
    }

    @Override
    public Trade produce() {
        Optional<Trade> trade;
        boolean isLastRecord = false;
        do {
            String rawData= readNextLine();
            if(!fileScanner.hasNextLine()){
                isLastRecord = true;
            }
            trade = Optional.ofNullable(parseTrade(rawData));

        } while (!trade.isPresent() && !isLastRecord);

       return enrichTrade(trade.get(), isLastRecord);
    }

    @Override
    public void close() {
        try{
            fileScanner.close();
        }catch (Exception e){
            new ExceptionLogger().logException(e, "Exception occurred in producer while closing resources.");
        }
    }

    private Trade enrichTrade(Trade trade, boolean isLastTrade) {
        if(Objects.isNull(trade)) {
            return Trade.createEmptyTrade();
        }
        trade.setLastTrade(isLastTrade);
        return trade;
    }

    private Trade parseTrade(String rawData){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(rawData, Trade.class);
        } catch (IOException e) {
            return null;
        }
    }

    private String readNextLine(){
        String nextLine = null;
        if(fileScanner.hasNextLine()){
            nextLine = fileScanner.nextLine();
        }
        return nextLine;
    }
}
