package com.lts.LeaveTradingSystem.service;

import com.lts.LeaveTradingSystem.entity.Teacher;
import com.lts.LeaveTradingSystem.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    @Autowired
    TeacherRepository teacherRepository;

    public Teacher findTeacherById(String tid) {
        return teacherRepository.findById(tid).get();
    }

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public List<Teacher> findTeacherByIds(List<String> collect) {
        return teacherRepository.findAllById(collect);
    }

    public List<Teacher> saveAll(List<Teacher> teachers) {
        return teacherRepository.saveAll(teachers);
    }
}
