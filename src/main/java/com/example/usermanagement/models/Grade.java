package com.example.usermanagement.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(generator = "genGrade")
    @SequenceGenerator(name = "genGrade", initialValue = 200)
    @JsonProperty
    private Integer id;

    private Integer value;

    @ManyToOne
    @JoinColumn(name = "student_id")
    Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    Subject subject;
}