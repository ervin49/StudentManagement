package com.example.usermanagement.DTOs;

import com.example.usermanagement.models.Grade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class GradeDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    public GradeDTO(Grade grade) {
        this.value = grade.getValue();
        this.subject = grade.getSubject().getName();
    }

    private Integer value;
    private String subject;
}