package com.example.usermanagement.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    private String CNP;
    private String name;
    private Integer absences;

    public Student(String CNP, String name, Integer absences) {
        this.CNP = CNP;
        this.name = name;
        this.absences = absences;
    }

    public Student() {
    }

    public String getCNP() {
        return this.CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAbsences() {
        return this.absences;
    }

    public void setAbsente(Integer absences) {
        this.absences = absences;
    }
}