package com.lts.LeaveTradingSystem.service;

import com.lts.LeaveTradingSystem.entity.Section;
import com.lts.LeaveTradingSystem.entity.Student;
import com.lts.LeaveTradingSystem.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {
    @Autowired
    private SectionRepository sectionRepository;

    public Section saveSection(Section section) {
        return sectionRepository.save(section);
    }

    public List<Student> getSectionBySubjectIdAndCode(String subjectId, String section) {
        return sectionRepository.findBySubjectIdAndCode(subjectId,section);
    }
}
