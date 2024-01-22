package com.lts.LeaveTradingSystem.model;

public interface Person {
    void setId(String trim);

    void setName(String trim);

    void setMobile(String trim);

    void setEmail(String trim);

    String getMobile();

    String getEmail();

    String getId();

    String getName();
}
