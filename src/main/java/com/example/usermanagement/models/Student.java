package com.example.usermanagement.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

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
    @Column(unique = true,nullable = false)
    private String cnp;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer absences;
    @Column(nullable = false)
    private Integer age;
    @Column(nullable = false)
    private String password;
}