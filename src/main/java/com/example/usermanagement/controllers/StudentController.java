package com.example.usermanagement.controllers;

import com.example.usermanagement.DTOs.request.StudentRequestDTO;
import com.example.usermanagement.DTOs.response.LoginResponseDTO;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.services.StudentService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class StudentController {

    StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Student student) {
        try {
            String str = studentService.register(student);
            if (str.equals("Registration Successful"))
                return new ResponseEntity<>(str, HttpStatus.CREATED);
            return new ResponseEntity<>(str, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody StudentRequestDTO studentRequestDTO) {
        LoginResponseDTO response = studentService.login(studentRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/students")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/my-details")
    public Student getMyDetails(@AuthenticationPrincipal Student student) {
        return student;
    }

    @GetMapping("/")
    public String welcome() {
        return "Welcome!";
    }

//    @GetMapping("/most-absences")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String getMostAbsencesOfAllStudents() {
//        StringBuilder name = new StringBuilder();
//        Integer absences = studentService.mostAbsences(name);
//        return "Student " + name + " has most absences: " + absences;
//    }

    @PutMapping("/students/update/{student_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> updateStudentById(@PathVariable Integer student_id, @RequestBody Student student) {
        try {
            studentService.updateStudentById(student_id, student);
            return ResponseEntity.ok().body(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/students/delete-student/{student_id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Student> removeStudent(@PathVariable Integer student_id) {
        try {
            studentService.deleteStudentById(student_id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}