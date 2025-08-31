package com.example.usermanagement.services;

import com.example.usermanagement.DTOs.request.StudentRequestDTO;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.repositories.StudentRepository;
import com.example.usermanagement.services.impl.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    public void when_register_return_message() {
        Student student = Student.builder()
                .email("student@example.com")
                .password(encoder.encode("parola123"))
                .build();

        String result = studentService.register(student);
        assertThat(result).isEqualTo("Registration successful.");
    }

    @Test
    public void when_login_return_token() {
        Student student = Student.builder()
                .email("student@example.com")
                .password(encoder.encode("parola123"))
                .build();
        StudentRequestDTO studentRequestDTO = StudentRequestDTO.builder()
                .email("student@example.com")
                .password("parola123")
                .build();

        when(studentRepository.findStudentByEmail(studentRequestDTO.getEmail())).thenReturn(student);
        when(jwtService.generateToken(any(UserDetailsImpl.class))).thenReturn("token123");

        String result = studentService.login(studentRequestDTO);

        assertThat(result)
                .isNotEqualTo("Email or password wrong!")
                .isEqualTo("token123")
                .isNotNull();

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void authenticate_badCredentials() {
        StudentRequestDTO dto = StudentRequestDTO.builder()
                .email("bad@email.com")
                .password("badpassword")
                .build();
        doThrow(new BadCredentialsException("bad")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        String result = studentService.login(dto);

        assertThat(result).isEqualTo("Email or password wrong!");
    }
}
