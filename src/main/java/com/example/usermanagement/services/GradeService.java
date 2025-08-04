package com.example.usermanagement.services;

import com.example.usermanagement.models.Grade;

import java.util.List;
import java.util.Optional;

public interface GradeService {
    void addGrade(Grade grade);

    List<Grade> getAllGrades();

    Optional<Grade> getSpecificGrade(Integer grade_id);

    void updateGrade(Integer grade_id, Grade grade);

    void deleteGrade(Integer grade_id);
}
