package com.lts.LeaveTradingSystem.service.redis;

public enum TradeAction {
    BUY("Buy"),
    SELL("Sell"),
    TRADE_INFO_MAP("TradeInfoMap");
    private final String setName;
    TradeAction(String setName)
    {
        this.setName=setName;
    }

}
