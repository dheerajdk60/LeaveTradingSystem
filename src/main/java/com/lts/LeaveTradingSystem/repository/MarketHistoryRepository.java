package com.lts.LeaveTradingSystem.repository;

import com.lts.LeaveTradingSystem.entity.MarketHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketHistoryRepository extends JpaRepository<MarketHistory,Long> {
}
