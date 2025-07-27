package com.example.usermanagement.models;

import jakarta.persistence.*;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public String CNP;
    public String name;
    public Integer absente;

    public Student(String CNP, String name, Integer absente) {
        this.CNP = CNP;
        this.name = name;
        this.absente = absente;
    }

    public Student(){}

    public String getCNP(){
        return this.CNP;
    }

    public Integer getId() {
        return this.id;
    }

    public void setCNP(String CNP){
        this.CNP = CNP;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Integer getAbsente(){
        return this.absente;
    }

    public void setAbsente(Integer absente){
        this.absente = absente;
    }
}
