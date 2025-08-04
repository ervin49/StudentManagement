package com.example.usermanagement.controllers;

import com.example.usermanagement.models.Student;
import com.example.usermanagement.services.StudentServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {

    StudentServiceImpl studentImpl;

    @Autowired
    public StudentController(StudentServiceImpl studentImpl) {
        this.studentImpl = studentImpl;
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentImpl.getAllStudents();
    }

    @PostMapping("/add-student")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        studentImpl.addStudent(student);
        return ResponseEntity.ok().body(student);
    }

    @DeleteMapping("/delete-student/{student_id}")
    public ResponseEntity<Student> removeStudent(@PathVariable Integer student_id) {
        try {
            studentImpl.deleteStudentById(student_id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/students/{student_id}")
    public Optional<Student> getSpecificStudent(@PathVariable Integer student_id) {
        return studentImpl.getStudentById(student_id);
    }

    @GetMapping("/most-absences")
    public Integer getMostAbsencesOfAllStudents() {
        return studentImpl.mostAbsences();
    }

    @PutMapping("/update/{student_id}")
    public ResponseEntity<Student> updateStudentById(@PathVariable Integer student_id, @RequestBody Student student) {
        try {
            studentImpl.updateStudentById(student_id, student);
            return ResponseEntity.ok().body(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/grades/{student_id}")
    public List<Integer> getAllGradesById(@PathVariable Integer student_id) {
        List<Integer> note = new ArrayList<>();
        return note;
    }

    @GetMapping("/grades/{id}/{subject}")
    public List<Integer> getGradesByIdAndSubject(@PathVariable Integer id, @PathVariable String subject) {
        List<Integer> note = new ArrayList<>();
        return note;
    }
}