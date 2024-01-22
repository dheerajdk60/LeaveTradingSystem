package com.lts.LeaveTradingSystem.service;

import com.lts.LeaveTradingSystem.entity.MarketHistory;
import com.lts.LeaveTradingSystem.repository.MarketHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class MarketHistoryService {
    @Autowired
    MarketHistoryRepository marketHistoryRepository;
    public void save(MarketHistory mh) {
        marketHistoryRepository.save(mh);
    }
}
