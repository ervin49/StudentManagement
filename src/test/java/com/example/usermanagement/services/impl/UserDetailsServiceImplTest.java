package com.example.usermanagement.services.impl;

import com.example.usermanagement.models.Student;
import com.example.usermanagement.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    StudentRepository studentRepository;

    @Test
    public void should_throw_when_student_is_null() {
        when(studentRepository.findStudentByEmail(any(String.class))).thenReturn(null);


        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("marian@gmail.com");
        });

        assertThat(exception).isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    public void return_details() {
        Student student = Student.builder()
                .email("student@example.com")
                .password("parola123")
                .build();
        String email = "student@example.com";
        when(studentRepository.findStudentByEmail(email)).thenReturn(student);

        UserDetails details = userDetailsService.loadUserByUsername(email);

        assertThat(details)
                .hasFieldOrPropertyWithValue("student", student)
                .isNotNull();
    }
}