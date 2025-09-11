package com.example.usermanagement.services;

import com.example.usermanagement.models.Subject;
import com.example.usermanagement.repositories.SubjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @InjectMocks
    SubjectService subjectService;

    @Mock
    SubjectRepository subjectRepository;

    Subject subject = Subject.builder()
            .id(1)
            .name("Math")
            .build();

    @Test
    void addSubject() {
        subjectService.addSubject(subject);

        verify(subjectRepository, times(1)).save(subject);
    }

    @Test
    void getAllSubjects() {
        subjectService.getAllSubjects();

        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    void getSpecificSubject() {
        subjectService.getSpecificSubject(1);

        verify(subjectRepository).findById(1);
    }

    @Test
    void updateSubject() {
        Subject newSubject = Subject.builder()
                .id(2)
                .name("Physics")
                .build();

        subjectService.updateSubject(1, newSubject);

        verify(subjectRepository, times(1)).deleteById(1);
        verify(subjectRepository, times(1)).save(newSubject);
    }

    @Test
    void deleteSubject() {
        subjectService.deleteSubject(1);
        verify(subjectRepository, times(1)).deleteById(1);
    }
}