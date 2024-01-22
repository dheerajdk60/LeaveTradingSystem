package com.lts.LeaveTradingSystem.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class OrderTableRedisSet {

    private final RedisTemplate<String, String> redisTemplate;
    private final ZSetOperations<String, String> zSetOperations;

    @Autowired
    public OrderTableRedisSet(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    public void addToTable(String tableName,String priceTimestamp, long score) {
        zSetOperations.add(tableName, priceTimestamp, score);
    }

    public Set<String> buyerList() {
        return zSetOperations.reverseRange(TradeAction.BUY.toString(), 0, -1);
    }
    public String top(String tableName) {
        Set<String> topPlayers=null;
        if(tableName.equalsIgnoreCase(TradeAction.BUY.toString()))
            topPlayers = zSetOperations.reverseRange(TradeAction.BUY.toString(), 0, 0);
        else
            topPlayers = zSetOperations.range(TradeAction.SELL.toString(), 0, 0);
        if (!topPlayers.isEmpty()) {
            return topPlayers.iterator().next();
        }
        return null;
    }
    public Set<String> topN(String tableName,int n) {
        Set<String> topPlayers=null;
        if(tableName.equalsIgnoreCase(TradeAction.BUY.toString())) {
            topPlayers = zSetOperations.reverseRange(TradeAction.BUY.toString(), 0, n - 1);
        }
        else {
            topPlayers = zSetOperations.range(TradeAction.SELL.toString(), 0, n - 1);
        }
        return topPlayers;
    }
    public void deleteFromSet(String tableName,String player) {
        zSetOperations.remove(tableName,player);
    }
    public Set<String> sellerList() {
        return zSetOperations.range(TradeAction.SELL.toString(), 0, -1);
    }

//    public Double getPlayerScore(String player) {
//        return zSetOperations.score(TradeRedisSetNames.BUYER_TABLE.toString(), player);
//    }
}
