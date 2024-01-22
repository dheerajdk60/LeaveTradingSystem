package com.lts.LeaveTradingSystem.controller;

import com.lts.LeaveTradingSystem.entity.MarketPrice;
import com.lts.LeaveTradingSystem.service.MarketPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/marketprice")
public class MarketPriceController {
    @Autowired
    MarketPriceService marketPriceService;

    @GetMapping("/price/{semester}")
    public float getPrice(@PathVariable int semester){
        return marketPriceService.getPrice(semester);
    }
    /*
    @GetMapping("/trade/{semester}/{coupons}")
    public MarketPrice trade(@PathVariable int semester, @PathVariable int coupons){
         marketPriceService.trade(null,semester,coupons);
    }*/
}
