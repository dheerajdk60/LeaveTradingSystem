package com.lts.LeaveTradingSystem.repository;

import com.lts.LeaveTradingSystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,String> {

    /*@Modifying
    @Query("UPDATE Student s SET s.coupon = :coupon  WHERE s.id = :id")
    void updateCouponById(@Param("coupon") int coupon, @Param("id") Long id);*/
}
