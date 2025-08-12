package com.example.usermanagement.services;


import java.util.List;
import java.util.Optional;

import com.example.usermanagement.models.Grade;
import com.example.usermanagement.models.Student;

public interface StudentService {
    void addStudent(Student student);

    void saveAll(List<Student> students);

    List<Student> getAllStudents();

    Optional<Student> getStudentById(Integer id);

    void updateStudentById(Integer id, Student student);

    void deleteStudentById(Integer id);

    Integer mostAbsences();
}