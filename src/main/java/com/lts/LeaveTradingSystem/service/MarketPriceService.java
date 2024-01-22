package com.lts.LeaveTradingSystem.service;

import com.lts.LeaveTradingSystem.entity.MarketHistory;
import com.lts.LeaveTradingSystem.entity.MarketPrice;
import com.lts.LeaveTradingSystem.repository.MarketHistoryRepository;
import com.lts.LeaveTradingSystem.repository.MarketPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MarketPriceService {
    @Autowired
    MarketPriceRepository marketPriceRepository;

    @Autowired
    MarketHistoryRepository marketHistoryRepository;
    @Value("${lts.coupon.priceMultiplier}")
    private int couponPriceMultiplier;
    @Autowired
    KafkaTemplate<String,MarketPrice> kafkaTemplate;
    private static final String topic ="MarketPrice";

    public float getPrice(int semester) {
        return marketPriceRepository.findBySemester(semester).getCurrentPrice();
    }
    public MarketPrice trade(MarketHistory mh,int semester,int coupon) {

        MarketPrice mp =marketPriceRepository.findBySemester(semester);
        float currentPrice=mp.getCurrentPrice();
        mp.setCurrentPrice(currentPrice+couponPriceMultiplier*coupon);
        mp.setCouponsAvailable(mp.getCouponsAvailable()-coupon);
        //send to kafka topic mp.getCurrentPrice();
         marketPriceRepository.save(mp);
         kafkaTemplate.send(topic,mp);
         mh.setAmount(currentPrice);
         marketHistoryRepository.save(mh);
         return mp;
    }
}
