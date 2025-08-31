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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    public void when_student_exists_return_error_message(){
        Student student = Student.builder()
                .email("student@example.com")
                .password(encoder.encode("parola123"))
                .build();
        when(studentRepository.findStudentByEmail(any(String.class))).thenReturn(new Student());

        String result = studentService.register(student);
        assertThat(result).isEqualTo("Email already taken!");
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

    @Test
    public void return_all_students(){
        studentService.getAllStudents();
        verify(studentRepository,times(1)).findAll();
    }

    @Test
    public void update_student(){
        studentService.updateStudentById(1,new Student());
        verify(studentRepository,times(1)).deleteById(1);
        verify(studentRepository,times(1)).save(any(Student.class));
    }

    @Test
    public void delete_student(){
        studentService.deleteStudentById(1);
        verify(studentRepository,times(1)).deleteById(1);
    }

    @Test
    public void get_by_email(){
        String email = "student@example.com";
        studentService.getStudentByEmail(email);
        verify(studentRepository,times(1)).findStudentByEmail(email);
    }

    @Test
    public void return_max_absences(){
        Student student1 = Student.builder()
                .email("student@example.com")
                .name("Ion")
                .password("parola123")
                .absences(3)
                .build();
        Student student2 = Student.builder()
                .email("student@example.com")
                .name("Marcel")
                .absences(4)
                .password("parola123")
                .build();
        Student student3 = Student.builder()
                .email("student@example.com")
                .name("Matei")
                .absences(10)
                .password("parola123")
                .build();
        Student student4 = Student.builder()
                .email("student@example.com")
                .name("Ioana")
                .absences(2)
                .password("parola123")
                .build();
        StringBuilder name = new StringBuilder();

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1,student2,student3,student4));
        when(studentRepository.findById(2)).thenReturn(Optional.of(student2));
        when(studentRepository.findById(3)).thenReturn(Optional.of(student3));
        when(studentRepository.findById(4)).thenReturn(Optional.of(student4));

        Integer result = studentService.mostAbsences(name);

        assertThat(result).isEqualTo(10);
        assertThat(name.toString()).isEqualTo("Matei");
    }

    @Test
    public void when_less_than_two_students_return_null(){
        //first student in the database will always be the
        //admin account which is generated at startup
        //so the id starts at 2
        Student student1 = Student.builder()
                .email("student@example.com")
                .name("Ion")
                .password("parola123")
                .absences(3)
                .build();
        List<Student> students = List.of(student1);
        when(studentRepository.findAll()).thenReturn(students);

        Integer result = studentService.mostAbsences(new StringBuilder());

        assertThat(result).isNull();
    }
}
