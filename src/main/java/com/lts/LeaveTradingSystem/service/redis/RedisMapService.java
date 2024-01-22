package com.lts.LeaveTradingSystem.service.redis;


import com.lts.LeaveTradingSystem.model.TradeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisMapService {

    private final RedisTemplate<String, TradeInfo> redisTemplate;

    @Autowired
    public RedisMapService(RedisTemplate<String, TradeInfo> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addToMap(String key, TradeInfo tradeInfo) {
        HashOperations<String, String, TradeInfo> hashOps = redisTemplate.opsForHash();
        hashOps.put(TradeAction.TRADE_INFO_MAP.toString(), key, tradeInfo);
    }

    public TradeInfo getFromMap( String key) {
        HashOperations<String, String, TradeInfo> hashOps = redisTemplate.opsForHash();
        return hashOps.get(TradeAction.TRADE_INFO_MAP.toString(), key);
    }

    public Map<String, TradeInfo> getAllFromMap() {
        HashOperations<String, String, TradeInfo> hashOps = redisTemplate.opsForHash();
        return hashOps.entries(TradeAction.TRADE_INFO_MAP.toString());
    }
    public void removeFromMap(String key) {
        HashOperations<String, String, TradeInfo> hashOps = redisTemplate.opsForHash();
        hashOps.delete(TradeAction.TRADE_INFO_MAP.toString(), key);
    }
}
