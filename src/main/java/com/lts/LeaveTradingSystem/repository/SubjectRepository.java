package com.lts.LeaveTradingSystem.repository;

import com.lts.LeaveTradingSystem.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject,Integer> {
    Subject findByCode(String subjectId);
}
