package com.lts.LeaveTradingSystem.service;

import com.lts.LeaveTradingSystem.entity.Subject;
import com.lts.LeaveTradingSystem.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;

    public Subject findSubjectByCode(String subjectCode) {
        return subjectRepository.findByCode(subjectCode);
    }

    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    public void saveAll(List<Subject> subjects) {
        subjectRepository.saveAll(subjects);
    }
}
