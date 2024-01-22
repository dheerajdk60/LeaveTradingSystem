package com.lts.LeaveTradingSystem.repository;


import com.lts.LeaveTradingSystem.entity.Section;
import com.lts.LeaveTradingSystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section,Long> {
    List<Student> findBySubjectIdAndCode(String subjectId, String section);
}
