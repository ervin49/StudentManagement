package com.example.usermanagement.services;

import com.example.usermanagement.DTOs.GradeDTO;
import com.example.usermanagement.models.Grade;
import com.example.usermanagement.repositories.GradeRepository;
import com.example.usermanagement.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeServiceImpl implements GradeService {
    GradeRepository gradeRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    public GradeServiceImpl(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Override
    public void addGrade(Grade grade) {
        gradeRepository.save(grade);
    }

    @Override
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    @Override
    public Optional<Grade> getSpecificGrade(Integer grade_id) {
        return gradeRepository.findById(grade_id);
    }

    @Override
    public void updateGrade(Integer grade_id, Grade grade) {
        gradeRepository.deleteById(grade_id);
        gradeRepository.save(grade);
    }

    @Override
    public void deleteGrade(Integer grade_id) {
        gradeRepository.deleteById(grade_id);
    }

    @Override
    public List<GradeDTO> getGradesOfStudent(Integer student_id) {
        return gradeRepository.findGradesByStudent_Id(student_id);
    }

    public Double getAverage(Integer student_id) {
        Double sum = 0.0, count = 0.0;
        List<GradeDTO> grades = gradeRepository.findGradesByStudent_Id(student_id);
        for (GradeDTO grade : grades) {
            Integer subject_id = subjectRepository.getSubjectByName(grade.getSubject()).getId();
            if (subjectRepository.findById(subject_id).isPresent()) {
                sum += grade.getValue() * subjectRepository.findById(subject_id).get().getCredits();
                count += subjectRepository.findById(subject_id).get().getCredits();
            }
        }
        return sum / count;
    }
}

