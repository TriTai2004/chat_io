package com.chatio.socket.futures.websocket.listener;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class SessionRegistry {
    
    private Map<String, String> userSessions = new ConcurrentHashMap<>();
    
    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        String email = event.getUser().getName();
        String sessionId = event.getMessage()
            .getHeaders().get("simpSessionId").toString();
        
        userSessions.put(email, sessionId);
    }
    
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        userSessions.remove(event.getUser().getName());
    }

    // Lấy tất cả user đang online
    public Set<String> getOnlineUsers() {
        return userSessions.keySet();
    }

    // User này có online không?
    public boolean isOnline(String userId) {
        return userSessions.containsKey(userId);
    }
}