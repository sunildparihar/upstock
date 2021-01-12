package com.upstock.trade.subs.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upstock.trade.commons.logger.ExceptionLogger;
import com.upstock.trade.subs.event.PacketReceivingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * OHLC packet Listener implementation that transmits the packets to the subscriber's web socket session
 */
public class SendToWebSocketOHLCPacketListener implements OHLCPacketListener {

    private static final Logger logger = LoggerFactory.getLogger(SendToWebSocketOHLCPacketListener.class);

    private String symbol;
    private final WebSocketSession webSocketSession;
    private int barSize;
    private ObjectMapper objectMapper = new ObjectMapper();

    public SendToWebSocketOHLCPacketListener(WebSocketSession webSocketSession, String symbol, int barSize) {
        this.symbol = symbol;
        this.webSocketSession = webSocketSession;
        this.barSize = barSize;
    }

    @Override
    public void onEvent(PacketReceivingEvent event) {
        if (isListening(event)) {
            try {
                String json = objectMapper.writeValueAsString(event.getEventData());
                logger.info(json); //print output json to verify
                synchronized (webSocketSession) {
                    webSocketSession.sendMessage(new TextMessage(json));
                }
            } catch (IOException e) {
                new ExceptionLogger().logException(e, "Exception occurred while sending packet to client");
            }
        }
    }

    private boolean isListening(PacketReceivingEvent event) {
        return symbol.equalsIgnoreCase(event.getEventData().getSymbol())
                && barSize == event.getEventData().getBarSizeInSeconds();
    }

    public String getSymbol() {
        return symbol;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public int getBarSize() {
        return barSize;
    }

}
