package com.example.usermanagement.services;

import com.example.usermanagement.DTOs.request.StudentRequestDTO;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.repositories.StudentRepository;
import com.example.usermanagement.services.impl.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {
    StudentRepository studentRepository;
    PasswordEncoder encoder;
    AuthenticationManager authenticationManager;
    JwtService jwtService;

    public String register(Student student) {
        Student studentExists = studentRepository.findStudentByEmail(student.getEmail());
        if (studentExists != null)
            return "Email already taken!";

        student.setPassword(encoder.encode(student.getPassword()));
        studentRepository.save(student);

        return "Registration successful.";
    }

    public String login(StudentRequestDTO studentRequestDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    studentRequestDTO.getEmail(), studentRequestDTO.getPassword()
            ));

            Student student = studentRepository.findStudentByEmail(studentRequestDTO.getEmail());
            UserDetailsImpl userDetails = UserDetailsImpl.builder().student(student).build();

            return jwtService.generateToken(userDetails);
        }catch (BadCredentialsException e) {
            return "Email or password wrong!";
        }
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void updateStudentById(Integer id, Student student) {
        studentRepository.deleteById(id);
        studentRepository.save(student);
    }

    public void deleteStudentById(Integer id) {
        studentRepository.deleteById(id);
    }

    public Student getStudentByEmail(String email) {
        return studentRepository.findStudentByEmail(email);
    }

    public Integer mostAbsences(StringBuilder name) {
        int maxAbsences = 0;
        if(studentRepository.findAll().size() >= 2) {
            for (Integer id = 2; id <= studentRepository.findAll().size(); id++) {
                if (studentRepository.findById(id).isPresent()) {
                    Integer currAbsences = studentRepository.findById(id).get().getAbsences();
                    if (maxAbsences < currAbsences) {
                        maxAbsences = currAbsences;
                        name.setLength(0);
                        name.append(studentRepository.findById(id).get().getName());
                    }
                }
            }
            return maxAbsences;
        }
        return null;
    }
}