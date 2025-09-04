package com.laughter.laughter.Component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import com.laughter.laughter.Security.JwtUtil;

public class WebSocketEventListener implements  ApplicationListener<SessionConnectEvent> {
    @Autowired
    private WebSocketSessionRegistry sessionRegistry;
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public void onApplicationEvent(SessionConnectEvent event){
        StompHeaderAccessor accessor =StompHeaderAccessor.wrap(event.getMessage());
        List<String> authHeaders = accessor.getNativeHeader("Authorization");

        if(authHeaders !=null && !authHeaders.isEmpty()){
            String token = authHeaders.get(0).replace("Bearer ","");
            String email=jwtUtil.extractEmail(token);
            sessionRegistry.register(email);
        }
    }

}
