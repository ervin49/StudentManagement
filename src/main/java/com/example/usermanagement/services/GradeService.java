package com.example.usermanagement.services;

import com.example.usermanagement.DTOs.response.GradeDTO;
import com.example.usermanagement.models.Grade;
import com.example.usermanagement.repositories.GradeRepository;
import com.example.usermanagement.repositories.SubjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GradeService {
    GradeRepository gradeRepository;
    SubjectRepository subjectRepository;

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

    public List<GradeDTO> getGradesOfStudent(Integer student_id) {
        return gradeRepository.findGradesByStudent_Id(student_id);
    }

    public Double getAverage(Integer student_id) {
        Double sum = 0.0, count = 0.0;
        List<GradeDTO> grades = gradeRepository.findGradesByStudent_Id(student_id);
        for (GradeDTO grade : grades) {
            String subjectName = grade.getSubject();
            Integer subject_id = subjectRepository.getSubjectByName(subjectName).getId();
            if (subjectRepository.findById(subject_id).isPresent()) {
                Integer currCredits = subjectRepository.findById(subject_id).get().getCredits();
                sum += grade.getValue() * currCredits;
                count += currCredits;
            }
        }
        Double result = Double.parseDouble(String.format("%.2f", sum / count));
        return result;
    }
}

