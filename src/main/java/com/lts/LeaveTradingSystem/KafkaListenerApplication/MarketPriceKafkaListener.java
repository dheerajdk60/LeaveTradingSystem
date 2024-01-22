package com.lts.LeaveTradingSystem.KafkaListenerApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class MarketPriceKafkaListener {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @KafkaListener(topics = "marketprice",groupId = "lts")
    public void consumeKafkaAndSendToWebsocket(String msg)
    {
        simpMessagingTemplate.convertAndSend("/live",msg);
        System.out.println("consumed "+msg);
    }
}
