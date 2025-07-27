package com.example.usermanagement.controllers;

import com.example.usermanagement.models.Student;
import com.example.usermanagement.services.StudentServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {

    @Autowired
    StudentServiceImpl studentImpl;

    public StudentController(StudentServiceImpl studentImpl) {
        this.studentImpl = studentImpl;
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentImpl.getAllStudents();
    }

    @PostMapping("/create")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        studentImpl.addStudent(student);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Student> removeStudent(@PathVariable Integer id) {
        try{
            studentImpl.deleteStudentById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/students/{id}")
    public Optional<Student> getSpecificStudent(@PathVariable Integer id){
        return studentImpl.getStudentById(id);
    }

    @GetMapping("/greatestAbsente")
    public void getGreatestAbsente(){
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Student> updateStudentById(@PathVariable Integer id, @RequestBody Student student){
        try{
            studentImpl.updateStudentById(id, student);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
