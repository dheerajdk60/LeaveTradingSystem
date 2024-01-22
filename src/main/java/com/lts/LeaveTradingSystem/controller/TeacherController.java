package com.lts.LeaveTradingSystem.controller;

import com.lts.LeaveTradingSystem.entity.*;
import com.lts.LeaveTradingSystem.model.TradeInfo;
import com.lts.LeaveTradingSystem.service.*;
import com.lts.LeaveTradingSystem.service.redis.RedisUtils;
import com.lts.LeaveTradingSystem.service.redis.TradeAction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {

    @Value("lts.coupon.serviceCharge")
    float serviceCharge;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final SubjectService subjectService;

    private final SectionService sectionService;
    private final StudentSubjectService studentSubjectService;




    @PostMapping("")
    public Teacher save(@RequestBody Teacher teacher)
    {
        return teacherService.save(teacher);
    }
    @GetMapping("/attendance/{studentIds}/{teacherId}/{subjectCode}/{sectionCode}")
    public void attendance(@PathVariable List<String> studentIds,@PathVariable String teacherId,@PathVariable String subjectCode,@PathVariable String sectionCode)
    {
        List<Student> absentStudents =studentService.findStudentsByIds(studentIds);
        Teacher teacher=teacherService.findTeacherById(teacherId);

        //marks students as absent
        Flux.fromIterable(absentStudents).publishOn(Schedulers.parallel()).subscribe(student -> {
            StudentSubject studentSubject =student.getStudentSubjects().stream().filter(stuSub-> stuSub.getSubject().getCode().equals(subjectCode)).findFirst().get();//studentSubjectService.findByStudentAndSubjectCode(student,subjectCode);
            studentSubject.getAbsentDates().add(LocalDateTime.now());
            studentSubject.setAbsent(studentSubject.getAbsent()+1);
            RedisUtils redisUtils=new RedisUtils();
            TradeInfo tradeInfo=new TradeInfo(1,0, TradeAction.BUY,student,serviceCharge,true);
            redisUtils.matchOrInsert(tradeInfo);
            studentSubjectService.save(studentSubject);
        });
        //classesTaken++
        Section section = teacher.getSections().stream().filter(sec ->
                sec.getSubject().getCode().equals(subjectCode) &&
                        sec.getCode().equals(sectionCode) ).findFirst().get();
        section.setTotalClasses(section.getTotalClasses()+1);
        section.getLastClassTaken().add(LocalDateTime.now());
        sectionService.saveSection(section);
    }
    @GetMapping("students/{tid}/{subId}/{section}/")
    public List<Student> getStudentsByTeacherBySubject(@PathVariable("tId") String teacherId,
                                                       @PathVariable("subId") String subjectId,
                                                       @PathVariable("section") String sectionCode)
    {
        Teacher teacher=teacherService.findTeacherById(teacherId);
        return teacher.getSections().stream().filter(section->section.getSubject().getCode().equals(subjectId)&&section.getCode().equals(sectionCode)).findFirst().get().getStudents();
        //return sectionService.getSectionBySubjectIdAndCode(subjectId,sectionCode);
        //teacherService.findTeacherById(teacherId).getSubjects().stream().
                //filter(sub->sub.getCode().equals(subjectId)).findFirst().get().getStudents();
    }
    @GetMapping("/subjects/{tid}")
    public List<Subject> getSubjects(@PathVariable String tid)
    {
        Teacher teacher = teacherService.findTeacherById(tid);
        List<Subject> subjects=teacher.getSections().stream().map(section -> section.getSubject()).collect(Collectors.toList());
        return subjects;
    }
    @GetMapping("/semesters/{teacherId}")
    public List<Integer> getSemesters(@PathVariable String teacherId)
    {
        Teacher teacher = teacherService.findTeacherById(teacherId);
        List<Integer> semesters = teacher.getSections().stream().map(section -> section.getSubject().getSemester()).distinct().sorted().collect(Collectors.toList());
        return semesters;
    }
    @GetMapping("/sections/{teacherId}/{subjectId}")
    public List<String> getSections(@PathVariable String teacherId,@PathVariable String subjectId)
    {
        Jedis redis= new Jedis("localhost");
        //redis.
        Teacher teacher = teacherService.findTeacherById(teacherId);
        List<String> sections = teacher.getSections().stream().filter(section -> section.getSubject().getCode().equals(subjectId)).map(section -> section.getCode()).collect(Collectors.toList());
        return sections;
    }
}
/*
id
student
subject
absent
total

 */