package com.example.usermanagement.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "genSubject")
    @JsonProperty
    @SequenceGenerator(name = "genSubject", initialValue = 100)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer credits;
}
