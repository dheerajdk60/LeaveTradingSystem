package com.lts.LeaveTradingSystem.entity;

import com.lts.LeaveTradingSystem.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
public class Student implements Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String name;
    private String email;
    private String mobile;
    private Boolean isActive= true;
    private Integer semester;
    private int coupon;
    private String dept;
    private float balance;
    @OneToMany(mappedBy = "student")
    private List<StudentSubject> studentSubjects;

    @ManyToMany
    List<Section> sections;

    public Student()
    {
        studentSubjects= new ArrayList<>();
        sections= new ArrayList<>();
    }

}
