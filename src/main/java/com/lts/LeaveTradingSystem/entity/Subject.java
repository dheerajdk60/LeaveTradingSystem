package com.lts.LeaveTradingSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String code;
    private int semester;
    @OneToMany(mappedBy = "subject")
    private List<String> sections;

    @JsonIgnore
    @OneToMany(mappedBy = "subject")
    private List<StudentSubject> studentSubjects;

}
