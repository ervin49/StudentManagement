package com.example.usermanagement.services;

import com.example.usermanagement.models.Grade;
import com.example.usermanagement.repositories.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeServiceImpl implements GradeService {
    GradeRepository gradeRepository;

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
}
