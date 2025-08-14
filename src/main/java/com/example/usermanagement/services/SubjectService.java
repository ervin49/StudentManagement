package com.example.usermanagement.services;

import java.util.List;
import java.util.Optional;

import com.example.usermanagement.models.Subject;

public interface SubjectService {
    public void addSubject(Subject subject);

    public void saveAll(List<Subject> subjects);

    public List<Subject> getAllSubjects();

    public Optional<Subject> getSpecificSubject(Integer subject_id);

    public void updateSubject(Integer subject_id, Subject subject);

    public void deleteSubject(Integer subject_id);
}
