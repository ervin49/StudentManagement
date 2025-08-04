package com.example.usermanagement.models;

import jakarta.persistence.*;
import org.hibernate.annotations.IdGeneratorType;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", initialValue = 100)
    private Integer id;

    private String name;

    public void setId(Integer id) {
        this.id = id;
    }

    public Subject() {
    }

    public Subject(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
