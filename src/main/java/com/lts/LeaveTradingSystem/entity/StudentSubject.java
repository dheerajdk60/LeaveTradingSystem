package com.lts.LeaveTradingSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
public class StudentSubject {
    @Id
    long id;

    @ManyToOne(cascade = CascadeType.ALL)
    Student student;
    @ManyToOne(cascade = CascadeType.ALL)
    Subject subject;
    int total;
    int absent;
    List<LocalDateTime> absentDates;
    public StudentSubject()
    {
        absentDates = new LinkedList<>();
    }
}
