package com.laughter.laughter.Component;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import com.laughter.laughter.Repository.UserRepository;
import com.laughter.laughter.Security.JwtUtil;


import com.laughter.laughter.Entity.User;

@Component
public class WebSocketAuthInterceptor  implements ChannelInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message ,MessageChannel channel){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        List <String> authHeaders =accessor.getNativeHeader("Authorization");
        if(authHeaders !=null && !authHeaders.isEmpty()){
            String token = authHeaders.get(0).replace("Bearer ","");
            String email=jwtUtil.extractEmail(token);
            
            User user = userRepository.findByEmail(email).orElseThrow();
            accessor.setUser(new UsernamePasswordAuthenticationToken(user,null,Collections.emptyList()));
        }
        return message;
    }

}
