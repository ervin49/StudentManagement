package com.example.usermanagement.services;

import com.example.usermanagement.models.Student;
import com.example.usermanagement.repositories.GradeRepository;
import com.example.usermanagement.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class GradeServiceTest {
    @InjectMocks
    private GradeService gradeService;

    @Mock
    private GradeRepository gradeRepository;

    public void method(){

    }

}
