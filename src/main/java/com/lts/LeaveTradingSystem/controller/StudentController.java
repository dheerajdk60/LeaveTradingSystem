package com.lts.LeaveTradingSystem.controller;

import com.lts.LeaveTradingSystem.entity.*;
import com.lts.LeaveTradingSystem.model.*;
import com.lts.LeaveTradingSystem.service.MarketPriceService;
import com.lts.LeaveTradingSystem.service.StudentService;
import com.lts.LeaveTradingSystem.service.redis.RedisUtils;
import com.lts.LeaveTradingSystem.service.redis.TradeAction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentController {


    private final StudentService studentService;
    private final MarketPriceService marketPriceService;

    @Value("lts.coupon.serviceCharge")
    float serviceCharge;
    @PostMapping("")
    public Student register(@RequestBody Student student)
    {
        return studentService.save(student);
    }
    @PostMapping("/update/coupon/{id}/{coupons}")
    public Student updateCoupons(@PathVariable String id,@PathVariable int coupons) throws Exception {
        int couponShortage=0;
        if(coupons<0)
        {
            Student s=studentService.findById(id);
            if(s.getCoupon()+coupons<0)
            {
                couponShortage=-(s.getCoupon()+coupons);

            }
        }
         return studentService.updateCouponById(id,coupons);
    }
    @PostMapping("/trade/{id}/{coupons}")
    public Student trade(@PathVariable String id,@PathVariable int coupons) throws Exception {
        int couponShortage=0;
        Student s=studentService.findById(id);
        if(coupons<0 && s.getCoupon()+coupons<0)
        {
            throw new Exception("You are trying to sell more coupons than you own");
        }
        MarketHistory mh=studentService.updateBalanceAndCouponCreateMarketHistory(s,coupons);

        MarketPrice mp =marketPriceService.trade(mh,s.getSemester(),coupons);

        return studentService.save(s);
    }
    @PostMapping("/limitOrder/{id}/{coupons}/{price}")
    public Student limitOrder(@PathVariable String id,@PathVariable int coupons,@PathVariable float price) throws Exception {
        int couponShortage=0;
        RedisUtils redisUtils=new RedisUtils();
        Student s=studentService.findById(id);
        if(coupons<0 && s.getCoupon()+coupons<0)
        {
            throw new Exception("You are trying to sell more coupons than you own");
        }
        TradeAction action = coupons<0?TradeAction.SELL:TradeAction.BUY;
        coupons=Math.abs(coupons);
        TradeInfo tradeInfo = new TradeInfo(coupons,price,action,s,serviceCharge);
        if(action==TradeAction.BUY && s.getBalance()< (tradeInfo.getPrice()*tradeInfo.getCoupons())+serviceCharge)
        {
            throw new Exception("Not enough balance to perform this transaction");
        }
        redisUtils.matchOrInsert(tradeInfo);
        MarketHistory mh=studentService.updateBalanceAndCouponCreateMarketHistory(s,coupons);

        MarketPrice mp =marketPriceService.trade(mh,s.getSemester(),coupons);

        return studentService.save(s);
    }
    @GetMapping("/attendance/{id}")
    public List<Attendance> attendance(@PathVariable String id) throws Exception {
        List<StudentSubject> studentSubjects=studentService.findById(id).getStudentSubjects();
        List<Attendance> attendances = new ArrayList<>();
        for (StudentSubject studentSubject:
             studentSubjects) {
            Attendance attendance = new Attendance();
            attendance.setSubjectCode(studentSubject.getSubject().getCode());
            attendance.setSubjectName(studentSubject.getSubject().getName());
            attendance.setClassesTaken(studentSubject.getStudent().getSections().stream()
                    .filter(section -> section.getSubject().getCode().equals(attendance.getSubjectCode())).findFirst().get().getTotalClasses());
            attendance.setClassesAttended(attendance.getClassesTaken()-studentSubject.getAbsent());
            attendance.setPercentage(String.format(".1f",100* attendance.getClassesAttended()/ attendance.getClassesTaken()));
            attendances.add(attendance);
        }
        return attendances;
    }

}

