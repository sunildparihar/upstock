package com.upstock.trade.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upstock.trade.commons.constant.TradeConstants;
import com.upstock.trade.perf.ServerPerformanceTracker;
import com.upstock.trade.subs.OHLCPacketSubscriptionService;
import com.upstock.trade.subs.listener.OHLCPacketListener;
import com.upstock.trade.subs.listener.SendToWebSocketOHLCPacketListener;
import com.upstock.trade.server.model.UserSubscription;
import com.upstock.trade.worker.WorkerStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    @Autowired
    private OHLCPacketSubscriptionService ohlcPacketSubscriptionService;

    private ConcurrentHashMap<WebSocketSession, List<OHLCPacketListener>> socketSessionOHLCPacketListenerMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<WebSocketSession, Object> userSessionToLockMap = new ConcurrentHashMap<>();

    @Autowired
    private ServerPerformanceTracker serverPerformanceTracker;

    @Autowired
    private WorkerStarter workerStarter;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {

        UserSubscription userSubscription = parseUserInput(message);

        if(isValidSubscriptionRequest(userSubscription)) {

            Object lock = userSessionToLockMap.get(session);

            synchronized (lock) {

                clearExistingSubscriptions(session);

                //create a new ohlc packet listener and register it
                OHLCPacketListener ohlcPacketListener =
                        new SendToWebSocketOHLCPacketListener(session, userSubscription.getSymbol(), userSubscription.getInterval());

                serverPerformanceTracker.increaseSubscriptionCount();

                ohlcPacketSubscriptionService.addNewListener(ohlcPacketListener);

                socketSessionOHLCPacketListenerMap.get(session).add(ohlcPacketListener);
            }

            //start workers on first subscription from any client
            workerStarter.startAll();

        } else {
            session.sendMessage(new TextMessage("Invalid Input!!"));
        }

    }

    private void clearExistingSubscriptions(WebSocketSession session) {
        socketSessionOHLCPacketListenerMap.get(session).forEach(
                listener ->
                {
                    serverPerformanceTracker.decreaseSubscriptionCount();
                    ohlcPacketSubscriptionService.removeListener(listener);
                });
    }

    @Nullable
    private UserSubscription parseUserInput(TextMessage message){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(message.getPayload(), UserSubscription.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        socketSessionOHLCPacketListenerMap.putIfAbsent(session, new ArrayList<>());
        userSessionToLockMap.putIfAbsent(session, new Object());
        serverPerformanceTracker.increaseSessionCount();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        serverPerformanceTracker.decreaseSessionCount();
        socketSessionOHLCPacketListenerMap.get(session).forEach(
                listener ->
                {
                    serverPerformanceTracker.decreaseSubscriptionCount();
                    ohlcPacketSubscriptionService.removeListener(listener);
                });
        socketSessionOHLCPacketListenerMap.remove(session);
        userSessionToLockMap.remove(session);
    }

    private boolean isValidSubscriptionRequest(UserSubscription userSubscription) {
        return userSubscription != null && TradeConstants.EVENT_SUBSCRIBE.equalsIgnoreCase(userSubscription.getEvent());
    }

}

