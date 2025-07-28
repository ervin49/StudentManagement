package com.example.usermanagement.services;

import com.example.usermanagement.models.Student;
import com.example.usermanagement.repositories.StudentRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.naming.NameNotFoundException;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    @Override
    public void addStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudentById(Integer id) {
        return studentRepository.findById(id);
    }

    @Override
    public void updateStudentById(Integer id, Student student) {
        studentRepository.deleteById(id);
        studentRepository.save(student);
    }

    @Override
    public void deleteStudentById(Integer id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Integer greatestAbsente(){
        Integer maxAbsente = 0;
        for (int id = 1; id < 1000; id++){
            if(studentRepository.findById(id).get().getAbsente() > maxAbsente)
                maxAbsente = studentRepository.findById(id).get().getAbsente();
        }
        return maxAbsente;
    }
}
