package com.verge.parking.common;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KeyCache {
    @Bean
    public Map<String, String> getKeyCache() {
        return new HashMap<>();
    }

}
