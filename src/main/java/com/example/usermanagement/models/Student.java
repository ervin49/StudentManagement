package com.example.usermanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email
    @Column(unique = true, length = 50, nullable = false)
    private String email;
    @Column(unique = true)
    private String cnp;
    @Column(nullable = false)
    private String password;
    private String name;
    private Integer absences;
    private Integer age;
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role role = Role.USER;
}