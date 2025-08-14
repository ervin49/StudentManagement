package com.example.usermanagement.services;

import com.example.usermanagement.models.Subject;
import com.example.usermanagement.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    SubjectRepository subjectRepository;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public void addSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    @Override
    public void saveAll(List<Subject> subjects) {
        subjectRepository.saveAll(subjects);
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Optional<Subject> getSpecificSubject(Integer subject_id) {
        return subjectRepository.findById(subject_id);
    }

    @Override
    public void updateSubject(Integer subject_id, Subject subject) {
        subjectRepository.deleteById(subject_id);
        subjectRepository.save(subject);
    }

    @Override
    public void deleteSubject(Integer subject_id) {
        subjectRepository.deleteById(subject_id);
    }
}
