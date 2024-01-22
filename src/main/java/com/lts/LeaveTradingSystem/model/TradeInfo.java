package com.lts.LeaveTradingSystem.model;

import com.lts.LeaveTradingSystem.entity.Student;
import com.lts.LeaveTradingSystem.service.redis.TradeAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TradeInfo {
    private boolean forceBuy;
    private String studentId;
    private int coupons;
    private float price;
    private LocalDateTime orderPlacedDate;
    private TradeAction tradeMode;
    private String key;
    private Student student;
    @Value("lts.coupon.serviceCharge")
    float serviceCharge;

    public TradeInfo(int coupons, float price, TradeAction tradeMode,Student student,float serviceCharge,boolean forceBuy) {
        this.studentId = student.getId();
        this.coupons = coupons;
        this.price = price;
        this.orderPlacedDate = LocalDateTime.now();
        this.tradeMode = tradeMode;
        this.student = student;
        this.serviceCharge = serviceCharge;
        this.forceBuy = forceBuy;
        this.key = this.getKeyCode();
    }
    public TradeInfo(int coupons, float price, TradeAction tradeMode,Student student,float serviceCharge) {
        this.studentId = student.getId();
        this.coupons = coupons;
        this.price = price;
        this.orderPlacedDate = LocalDateTime.now();
        this.tradeMode = tradeMode;
        this.student = student;
        this.serviceCharge = serviceCharge;
        this.key = this.getKeyCode();
    }

    public String getKeyCode()
    {
        String keyTime=orderPlacedDate.toString();
        keyTime=keyTime.replaceAll("(\\.\\d*)|-|:|T","");
        String keyPrice=String.format("%.2f",price);
        return keyPrice+keyTime;
    }
}
