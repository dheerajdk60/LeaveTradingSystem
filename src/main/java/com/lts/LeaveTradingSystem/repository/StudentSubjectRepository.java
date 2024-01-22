package com.lts.LeaveTradingSystem.repository;

import com.lts.LeaveTradingSystem.entity.Student;
import com.lts.LeaveTradingSystem.entity.StudentSubject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSubjectRepository extends JpaRepository<StudentSubject,Long> {
    StudentSubject findByStudentAndSubjectCode(Student student, String subjectCode);
}
