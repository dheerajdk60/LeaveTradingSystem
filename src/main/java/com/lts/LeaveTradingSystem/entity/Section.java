package com.lts.LeaveTradingSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
3sem    3sem    3sem
maths   maths   maths
puranik puranik vimala,puranik
A       B       C
1       4       7
2       5       8
3       6       9

 */
@Entity
@Data
@AllArgsConstructor
public class Section
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String code;

    @ManyToOne
    Subject subject;
    @ManyToMany(mappedBy = "section")
    List<Student> students;
    @ManyToMany(mappedBy = "section")
    List<Teacher> teachers;
    int totalClasses;

    List<LocalDateTime> lastClassTaken;
    public Section()
    {
        lastClassTaken=new ArrayList<>();
    }

}
