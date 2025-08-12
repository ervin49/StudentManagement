package com.example.usermanagement.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email
    private String email;
    @Column(unique = true)
    private String cnp;
    private String name;
    private Integer absences;
    private Integer age;

    @Override
    public String toString() {
        return "cnp is: " + cnp +
                ", name is: " + name +
                ", email is: " + email +
                ", no. of absences is: " + absences +
                ", age is: " + age;
    }
}