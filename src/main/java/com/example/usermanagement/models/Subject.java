package com.example.usermanagement.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "genSubject")
    @JsonProperty
    @SequenceGenerator(name = "genSubject", initialValue = 100)
    private Integer id;

    @Column(unique = true)
    private String name;
    private Integer credits;
}
