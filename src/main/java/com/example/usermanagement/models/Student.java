package com.example.usermanagement.models;

import jakarta.persistence.*;

@Entity
public class Student {
    @Id
    public String cnp;
    public String name;
    public Integer absente;

    public Student(String name, Integer absente) {
        this.name = name;
        this.absente = absente;
    }

    public Student(){}

    public String getCNP(){
        return this.cnp;
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
