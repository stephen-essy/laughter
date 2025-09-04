package com.laughter.laughter.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketSessionRegistry {
    private final Set<String> connectedUserEmails=ConcurrentHashMap.newKeySet();
    public void register(String email){
        connectedUserEmails.add(email);
    }
    public void unregister(String email){
        connectedUserEmails.remove(email);
    }
    public Set<String> getConnectedUserEmails(){
        return connectedUserEmails;
    }

}
