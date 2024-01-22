package com.lts.LeaveTradingSystem.controller;

import com.lts.LeaveTradingSystem.entity.*;
import com.lts.LeaveTradingSystem.model.Person;
import com.lts.LeaveTradingSystem.service.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/excel")
public class LinkingController {
    @Autowired
    StringUtil stringUtil;
    @Autowired
    SectionService sectionService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    StudentService studentService;

    @Autowired
    TeacherService teacherService;
    @Autowired
    StudentSubjectService studentSubjectService;

    Logger logger = LoggerFactory.getLogger(LinkingController.class);
    @PostMapping("/import")//code,name,sem
    public void insertRelations(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {
        insertPerson(reapExcelDataFile,0);//insert students
        insertPerson(reapExcelDataFile,1);//insert teachers
        insertSubject(reapExcelDataFile,2);//insert subjects
        joinTables(reapExcelDataFile,3);// define relation between each other
    }
    public boolean insertPerson(MultipartFile reapExcelDataFile,int sheetNo) throws IOException {

        try
        {
            List<Person> persons = new ArrayList<>();
            XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(sheetNo);
            Person person =null;
            for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {

                switch (sheetNo)
                {
                    case 0: person=new Student();break;
                    case 1: person=new Teacher();break;
                }


                XSSFRow row = worksheet.getRow(i);

                person.setId(row.getCell(0).getStringCellValue().trim());
                person.setName(row.getCell(1).getStringCellValue().trim());
                person.setMobile(row.getCell(2).getStringCellValue().trim());
                person.setEmail(row.getCell(3).getStringCellValue().trim());
                if(stringUtil.isEmpty(person.getMobile())&&stringUtil.isEmpty(person.getEmail()))
                {
                    throw new RuntimeException("Either Email or Mobile has to be set for a student");
                }
                if(stringUtil.isEmpty(person.getId())||stringUtil.isEmpty(person.getName()))
                {
                    throw new RuntimeException("Either Name or RollNumber is missing");
                }
                persons.add(person);
            }
            switch (sheetNo)
            {
                case 0: studentService.saveAll(persons.stream().map(Student.class::cast).collect(Collectors.toList()));
                    break;
                case 1: teacherService.saveAll(persons.stream().map(Teacher.class::cast).collect(Collectors.toList()));
                    break;
            }
        }
        catch (Exception e)
        {
            logger.error("error while inserting "+(sheetNo==0?"student":"teacher"),e);
            return false;
        }
        return true;

    }
    public void joinTables(MultipartFile reapExcelDataFile,int sheetNo) throws IOException {
        try{
            String sectionCode; String subjectCode;
            List<String> teacherList;
            List<String> studentList=new ArrayList<>();
            int semester;String dept;

            XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
            for(int sheetNumber=sheetNo;sheetNumber<workbook.getNumberOfSheets();sheetNumber++) {
                XSSFSheet worksheet = workbook.getSheetAt(sheetNumber);

                dept = worksheet.getRow(0).getCell(1).getStringCellValue().trim();
                semester = (int) worksheet.getRow(1).getCell(1).getNumericCellValue();
                subjectCode = worksheet.getRow(2).getCell(1).getStringCellValue().trim();
                sectionCode = worksheet.getRow(3).getCell(1).getStringCellValue().trim();
                teacherList = Arrays.asList(worksheet.getRow(4).getCell(1).getStringCellValue().trim().split(","));

                for (int i = 5; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    XSSFRow row = worksheet.getRow(i);
                    String studentId = row.getCell(0).getStringCellValue().trim();
                    if (stringUtil.isEmpty(studentId)) {
                        continue;
                    }
                    studentList.add(studentId);
                }

                List<Student> students = studentService.findStudentsByIds(studentList);
                Subject subject = subjectService.findSubjectByCode(subjectCode);
                List<Teacher> teachers = teacherService.findTeacherByIds(teacherList);

                Section section = new Section();
                section.setCode(sectionCode);
                section.setSubject(subject);
                section.setStudents(students);
                section.setTeachers(teachers);
                section = sectionService.saveSection(section);
                Section finalSection = section;
                int finalSemester = semester;
                String finalDept = dept;
                Flux.fromIterable(students).publishOn(Schedulers.parallel()).subscribe(student -> {
                    StudentSubject studentSubject = new StudentSubject();
                    studentSubject.setStudent(student);
                    studentSubject.setSubject(subject);
                    studentSubject = studentSubjectService.save(studentSubject);
                    student.getStudentSubjects().add(studentSubject);
                    student.setSemester(finalSemester);
                    student.setDept(finalDept);
                    student.getSections().add(finalSection);
                    studentService.save(student);
                });

                Flux.fromIterable(teachers).publishOn(Schedulers.parallel()).subscribe(teacher -> {
                    teacher.getSections().add(finalSection);
                    teacherService.save(teacher);
                });
            }
        }
        catch (Exception e)
        {
            logger.error("error while joining",e);
        }
    }
    public void insertSubject(MultipartFile reapExcelDataFile,int sheetNo) throws IOException {

        try
        {
            List<Subject> subjects = new ArrayList<>();
            XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(sheetNo);
            Subject subject;
            for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
                subject=new Subject();
                XSSFRow row = worksheet.getRow(i);

                subject.setCode(row.getCell(0).getStringCellValue().trim());
                subject.setName(row.getCell(1).getStringCellValue().trim());
                subject.setSemester((int)row.getCell(2).getNumericCellValue());
                if(stringUtil.isEmpty(subject.getCode())||stringUtil.isEmpty(subject.getName())||subject.getSemester()==0)
                {
                    throw new RuntimeException("Require all fields to be nonempty");
                }
                subjects.add(subject);
            }
            subjectService.saveAll(subjects);
        }
        catch (Exception e)
        {
            logger.error("error while inserting subjects",e);
        }
    }
}/*
dept
sem
subcode
section
teacher list
student list

   */