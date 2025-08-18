package com.example.usermanagement.controllers;

import com.example.usermanagement.models.Student;
import com.example.usermanagement.services.StudentService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class StudentController {

    StudentService studentService;

    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping("/add-students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addStudent(@RequestBody List<Student> students) {
        studentService.saveAll(students);
        return ResponseEntity.ok("Students created successfully");
    }


    @DeleteMapping("/delete-student/{student_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> removeStudent(@PathVariable Integer student_id) {
        try {
            studentService.deleteStudentById(student_id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/students/{student_id}")
    public String getSpecificStudent(@PathVariable Integer student_id) {
        if (studentService.getStudentById(student_id).isEmpty())
            return "No such student.";
        return studentService.getStudentById(student_id).toString();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            studentService.addStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/most-absences")
    @PreAuthorize("hasRole('ADMIN')")
    public String getMostAbsencesOfAllStudents() {
        StringBuilder name = new StringBuilder();
        Integer absences = studentService.mostAbsences(name);
        return "Student " + name + " has most absences: " + absences;
    }

    @PutMapping("/update/{student_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> updateStudentById(@PathVariable Integer student_id, @RequestBody Student student) {
        try {
            studentService.updateStudentById(student_id, student);
            return ResponseEntity.ok().body(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}