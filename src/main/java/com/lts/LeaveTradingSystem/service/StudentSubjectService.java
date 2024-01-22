package com.lts.LeaveTradingSystem.service;

import com.lts.LeaveTradingSystem.entity.Student;
import com.lts.LeaveTradingSystem.entity.StudentSubject;
import com.lts.LeaveTradingSystem.repository.StudentSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentSubjectService {
    @Autowired
    private StudentSubjectRepository studentSubjectRepository;

    public StudentSubject save(StudentSubject studentSubject) {
        return studentSubjectRepository.save(studentSubject);
    }

    public StudentSubject findByStudentAndSubjectCode(Student student, String subjectCode) {
        return studentSubjectRepository.findByStudentAndSubjectCode(student,subjectCode);
    }
}
