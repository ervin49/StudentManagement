package com.example.usermanagement.services;

import com.example.usermanagement.DTOs.request.StudentRequestDTO;
import com.example.usermanagement.DTOs.response.LoginResponseDTO;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.repositories.StudentRepository;
import com.example.usermanagement.services.impl.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            return "Student with email: " + student.getEmail() + " already exists!";

        student.setPassword(encoder.encode(student.getPassword()));
        studentRepository.save(student);

        return "Registration successful";
    }

    public LoginResponseDTO login(StudentRequestDTO studentRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                studentRequestDTO.getEmail(), studentRequestDTO.getPassword()
        ));

        Student student = studentRepository.findStudentByEmail(studentRequestDTO.getEmail());
        UserDetails userDetails = new UserDetailsImpl(student);

        String jwtToken = jwtService.generateToken(new HashMap<>(), userDetails);

        return new LoginResponseDTO(student.getEmail(), jwtToken);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Integer id) {
        return studentRepository.findById(id);
    }

    public void updateStudentById(Integer id, Student student) {
        studentRepository.deleteById(id);
        studentRepository.save(student);
    }

    public void deleteStudentById(Integer id) {
        studentRepository.deleteById(id);
    }

//    public Integer mostAbsences(StringBuilder name) {
//        Integer maxAbsences = 0;
//        for (int id = 1; id <= studentRepository.findAll().size(); id++) {
//            if (studentRepository.findById(id).isPresent()) {
//                Integer currAbsences = studentRepository.findById(id).get().getAbsences();
//                if (maxAbsences < currAbsences) {
//                    maxAbsences = currAbsences;
//                    name.setLength(0);
//                    name.append(studentRepository.findById(id).get().getName());
//                }
//            }
//        }
//        return maxAbsences;
//    }

}