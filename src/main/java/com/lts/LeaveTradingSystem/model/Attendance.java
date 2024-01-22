package com.lts.LeaveTradingSystem.model;

import lombok.Data;

@Data
public class Attendance
{
    private String subjectCode;
    private String subjectName;
    private int classesTaken;
    private int classesAttended;
    private String percentage;
}