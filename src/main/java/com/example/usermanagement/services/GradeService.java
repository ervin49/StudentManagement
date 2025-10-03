package com.example.usermanagement.services;

import com.example.usermanagement.models.Grade;
import com.example.usermanagement.models.Subject;
import com.example.usermanagement.repositories.GradeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GradeService {
    GradeRepository gradeRepository;

    public void addGrade(Grade grade) {
        gradeRepository.save(grade);
    }

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public Optional<Grade> getSpecificGrade(Integer grade_id) {
        return gradeRepository.findById(grade_id);
    }

    public void updateGrade(Integer grade_id, Grade grade) {
        gradeRepository.deleteById(grade_id);
        gradeRepository.save(grade);
    }

    public void deleteGrade(Integer grade_id) {
        gradeRepository.deleteById(grade_id);
    }

    public List<Grade> getGradesOfStudent(Integer student_id) {
        return gradeRepository.findGradesByStudent_Id(student_id);
    }

    public Double getAverage(Integer student_id) {
        double sum = 0.0;
        int totalCredits = 0;
        List<Grade> grades = gradeRepository.findGradesByStudent_Id(student_id);
        for (Grade grade : grades) {
            Subject subject = grade.getSubject();
            int credits = subject.getCredits();
            sum += grade.getValue() * credits;
            totalCredits += credits;
        }
        if (totalCredits == 0)
            return 0.0;

        return (double) Math.round((sum / totalCredits) * 100) / 100;
    }
}

