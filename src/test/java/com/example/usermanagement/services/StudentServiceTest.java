package com.example.usermanagement.services;

import com.example.usermanagement.DTOs.request.StudentRequestDTO;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;

    @Test
    public void when_register_return_message(){
        Student student = Student.builder()
                .email("student@example.com")
                .password(encoder.encode("parola123"))
                .build();

        String result = studentService.register(student);
        assertThat(result).isEqualTo("Registration successful.");
    }

    @Test
    public void when_login_return_token(){
        Student student = Student.builder()
                .email("student@example.com")
                .password(encoder.encode("parola123"))
                .build();
        studentService.register(student);

        StudentRequestDTO studentRequestDTO = StudentRequestDTO.builder()
                .email("student@example.com")
                .password("parola123")
                .build();

        String result = studentService.login(studentRequestDTO);

        assertThat(result)
                .isNotEqualTo("Email or password wrong!")
                .isNotNull();
    }
}
