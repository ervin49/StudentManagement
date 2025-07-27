package com.example.usermanagement.services;


import java.util.List;
import java.util.Optional;

import com.example.usermanagement.models.Student;
public interface StudentService {
    public void addStudent(Student student);

    public List<Student> getAllStudents();

    public Optional<Student> getStudentById(Integer id);

    public void updateStudentById(Integer id,Student student);

    public void deleteStudentById(Integer id);
}
