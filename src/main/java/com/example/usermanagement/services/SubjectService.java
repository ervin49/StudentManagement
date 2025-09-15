package com.example.usermanagement.services;

import com.example.usermanagement.models.Subject;
import com.example.usermanagement.repositories.SubjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubjectService {

    SubjectRepository subjectRepository;

    public void addSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> getSpecificSubject(Integer subject_id) {
        return subjectRepository.findById(subject_id);
    }

    public void updateSubject(Integer subject_id, Subject subject) {
        subjectRepository.deleteById(subject_id);
        subjectRepository.save(subject);
    }

    public void deleteSubject(Integer subject_id) {
        subjectRepository.deleteById(subject_id);
    }

    public boolean existsById(Integer subjectId) {
        return subjectRepository.existsById(subjectId);
    }
}
