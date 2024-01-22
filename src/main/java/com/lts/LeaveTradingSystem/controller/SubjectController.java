package com.lts.LeaveTradingSystem.controller;

import com.lts.LeaveTradingSystem.entity.Student;
import com.lts.LeaveTradingSystem.entity.Subject;
import com.lts.LeaveTradingSystem.entity.Teacher;
import com.lts.LeaveTradingSystem.service.StudentService;
import com.lts.LeaveTradingSystem.service.SubjectService;
import com.lts.LeaveTradingSystem.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
    SubjectService subjectService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    StudentService studentService;

    @PostMapping("")
    public Subject createSubject(@RequestBody Subject subject)
    {

        return subjectService.save(subject);
    }
}
