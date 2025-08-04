package com.example.usermanagement.models;

import jakarta.persistence.*;

@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genGrade")
    @SequenceGenerator(name = "genGrade", initialValue = 200)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    Subject subject;

    private Integer value;

    public void setValue(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}