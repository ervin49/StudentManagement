package com.example.usermanagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cnp;
    private String name;
    private Integer absences;

    @Override
    public String toString() {
        return "cnp is:" + cnp +
                ", name is:" + name +
                ", no. of absences is:" + absences;
    }
}