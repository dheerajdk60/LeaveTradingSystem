package com.lts.LeaveTradingSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MarketHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String studentId;
    String studentName;
    int semester;
    float amount;
    String action;

}
