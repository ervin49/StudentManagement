package com.example.usermanagement.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Data
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
}