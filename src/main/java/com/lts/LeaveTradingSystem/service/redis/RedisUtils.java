package com.lts.LeaveTradingSystem.service.redis;

import com.lts.LeaveTradingSystem.entity.Student;
import com.lts.LeaveTradingSystem.model.TradeInfo;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisUtils {

    @Autowired
    private RedisMapService redisMapService;
    @Autowired
    OrderTableRedisSet orderTableRedisSet;

    public void createMapAndSet(TradeInfo tradeInfo) {
        String key = tradeInfo.getKey();
        redisMapService.addToMap( key, tradeInfo);
        orderTableRedisSet.addToTable(tradeInfo.getTradeMode().toString(),key,Long.parseLong(key));
    }
    public void deleteMapAndSet(TradeInfo tradeInfo) {
        String key = tradeInfo.getKey();
        redisMapService.removeFromMap( key);
        orderTableRedisSet.deleteFromSet(tradeInfo.getTradeMode().toString(),key);
    }

    public void matchOrInsert(TradeInfo tradeInfo) {
        Student student=tradeInfo.getStudent();
        TradeAction action=tradeInfo.getTradeMode();
        TradeAction matchAction=TradeAction.BUY==action?TradeAction.SELL:TradeAction.BUY;
        boolean loop=true;
        if(tradeInfo.isForceBuy() && tradeInfo.getCoupons()==1)
        {
            tradeInfo.setPrice(Short.MAX_VALUE);
        }
        while(loop)
        {
            loop=false;
            String key = orderTableRedisSet.top(matchAction.toString());
            TradeInfo existingOrder=redisMapService.getFromMap(key);
            if (TradeAction.BUY==action && existingOrder.getPrice() <= tradeInfo.getPrice()||
                    TradeAction.SELL==action && existingOrder.getPrice() >= tradeInfo.getPrice()) {
                int tradeCoupons=tradeInfo.getCoupons();
                int existingCoupons=existingOrder.getCoupons();
                if(tradeCoupons==existingCoupons)
                {
                    existingOrder.setCoupons(existingCoupons-tradeCoupons);
                    deleteMapAndSet(existingOrder);
                }
                else if(tradeCoupons<existingCoupons)
                {
                    existingOrder.setCoupons(existingCoupons-tradeCoupons);
                    createMapAndSet(existingOrder);
                }
                else if(tradeCoupons>existingCoupons)
                {
                    deleteMapAndSet(existingOrder);
                    tradeInfo.setCoupons(tradeCoupons-existingCoupons);
                    loop=true;
                }
            }
            else {
                createMapAndSet(tradeInfo);
            }
        }

    }
}
