package com.lts.LeaveTradingSystem.repository;

import com.lts.LeaveTradingSystem.entity.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketPriceRepository extends JpaRepository<MarketPrice,Integer> {
    MarketPrice findBySemester(int semester);
}
