package com.lts.LeaveTradingSystem.entity;

import com.lts.LeaveTradingSystem.model.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher implements Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id;

    String name;
    String email;
    String mobile;

    @ManyToMany
    List<Section> sections;


}
