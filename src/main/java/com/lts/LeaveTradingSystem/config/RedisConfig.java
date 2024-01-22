package com.lts.LeaveTradingSystem.config;

import com.lts.LeaveTradingSystem.model.TradeInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, TradeInfo> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, TradeInfo> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;

    }
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
        return new LettuceConnectionFactory(config);
    }

}
