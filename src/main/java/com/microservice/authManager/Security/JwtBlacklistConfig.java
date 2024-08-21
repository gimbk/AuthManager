package com.microservice.authManager.Security;

import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class JwtBlacklistConfig {
    private Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

    public void blacklistToken(String token) {
        blacklist.add(token);
    }
}
