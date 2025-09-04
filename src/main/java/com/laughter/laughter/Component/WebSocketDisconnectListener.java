package com.laughter.laughter.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class WebSocketDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {
    @Autowired
    private WebSocketSessionRegistry sessionRegistry;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event){
        StompHeaderAccessor accessor=StompHeaderAccessor.wrap(event.getMessage());
        @SuppressWarnings("null")
        String email=accessor.getUser().getName();
        sessionRegistry.unregister(email);
    }
}
