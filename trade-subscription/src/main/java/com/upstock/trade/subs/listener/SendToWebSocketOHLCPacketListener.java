package com.upstock.trade.subs.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upstock.trade.commons.logger.ExceptionLogger;
import com.upstock.trade.commons.pojo.OHLCPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Objects;

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
    public void onPacketReceived(OHLCPacket packet) {
        if (packet.getSymbol().equalsIgnoreCase(symbol) && packet.getBarSizeInSeconds() == barSize) {
            try {
                String json = objectMapper.writeValueAsString(packet);
                logger.info(json); //print output json to verify
                synchronized (webSocketSession) {
                    webSocketSession.sendMessage(new TextMessage(json));
                }
            } catch (IOException e) {
                new ExceptionLogger().logException(e, "Exception occurred while sending packet to client");
            }
        }
    }

    @Override
    public String getTradingSymbolToListen() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendToWebSocketOHLCPacketListener that = (SendToWebSocketOHLCPacketListener) o;
        return Objects.equals(webSocketSession, that.webSocketSession);
    }

    @Override
    public int hashCode() {
        return Objects.hash(webSocketSession);
    }
}
