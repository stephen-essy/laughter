package com.laughter.laughter.Component;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.laughter.laughter.Security.JwtUtil;
import com.laughter.laughter.Service.UserDetailsServiceImplementation;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> authHeaders = accessor.getNativeHeader("Authorization");
            if (authHeaders != null && !authHeaders.isEmpty()) {
                String token = authHeaders.get(0).replace("Bearer ", "");
                String email = jwtUtil.extractEmail(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (email != null && jwtUtil.validateToken(token, userDetails)) {
                    Principal principal = () -> email;
                    accessor.setUser(principal);
                    System.out.println("Websocket connect authenticated for :"+email);
                }
            }
        }
        return message;
    }

}
