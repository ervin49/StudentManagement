package com.example.usermanagement.services;

import com.example.usermanagement.models.Student;
import com.example.usermanagement.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public void register(Student student) {
        student.setPassword(encoder.encode(student.getPassword()));
        studentRepository.save(student);
    }

    public void saveAll(List<Student> students) {
        studentRepository.saveAll(students);
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

    public Integer mostAbsences(StringBuilder name) {
        Integer maxAbsences = 0;
        for (int id = 1; id <= studentRepository.findAll().size(); id++) {
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

}