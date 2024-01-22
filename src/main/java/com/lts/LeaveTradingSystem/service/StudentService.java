package com.lts.LeaveTradingSystem.service;

import com.lts.LeaveTradingSystem.entity.MarketHistory;
import com.lts.LeaveTradingSystem.entity.Student;
import com.lts.LeaveTradingSystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private  StudentRepository studentRepository;

    @Value("${lts.coupon.initialCouponPerStudent}")
    private String initialCoupons;
    @Value("${lts.coupon.priceMultiplier}")
    private int couponPriceMultiplier;
    public Student save(Student student)
    {
        return studentRepository.save(student);
    }
    public Student findById(String id) throws Exception {
        Optional<Student> studentOp= studentRepository.findById(id);
        if(studentOp.isPresent())
        {
            return studentOp.get();
        }
        else {
            throw new Exception(String.format("Student with id:%ld is not present",id));
        }

    }
    public MarketHistory  updateBalanceAndCouponCreateMarketHistory(Student s,int coupons)
    {
        int pricePerCoupon=1;// get current price from kafka.
        s.setCoupon(s.getCoupon()+coupons);
        s.setBalance(s.getBalance()-coupons*pricePerCoupon);
        MarketHistory mh = new MarketHistory();
        {//creating a record of transaction
            mh.setStudentId(s.getId());
            mh.setStudentName(s.getName());
            mh.setSemester(s.getSemester());
            mh.setAction(coupons < 0 ? "SELL" : "BUY");
        }//Creating record of transaction
        return mh;
    }
    public Student updateCouponById(String id,int coupon) {
         //studentRepository.updateCouponById(coupon,id);
        Student s=studentRepository.findById(id).get();
        s.setCoupon(s.getCoupon()+coupon);
        return studentRepository.save(s);

    }


    public List<Student> findStudentsByIds(List<String> ids) {
        return studentRepository.findAllById(ids);
    }

    public List<Student> saveAll(List<Student> students) {
        return studentRepository.saveAll(students);
    }
}
