package com.example.usermanagement.controllers;

import com.example.usermanagement.DTOs.request.StudentRequestDTO;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.services.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
public class StudentController {

    StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Student student) {
        try {
            String str = studentService.register(student);
            if (str.equals("Registration successful."))
                return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student already exists");
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody StudentRequestDTO studentRequestDTO) {
        String str = studentService.login(studentRequestDTO);
        if (str.equals("Email or password wrong!"))
            return new ResponseEntity<>(Map.of("Message", str), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(Map.of("Jwt", str), HttpStatus.OK);
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/my-details")
    public Student getMyDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return studentService.getStudentByEmail(email);
        }
        return null;
    }

    @GetMapping(value = "/students/most-absences")
    public ResponseEntity<Map<String, Object>> getMostAbsencesOfAllStudents() {
        Student student = studentService.mostAbsences();
        if (student != null)
            return new ResponseEntity<>(Map.of(
                    "name", student.getName(),
                    "absences", student.getAbsences()), HttpStatus.OK);
        return new ResponseEntity<>(Map.of("message", "There is no student in database"), HttpStatus.NOT_FOUND);
    }

    @PutMapping("/students/update/{student_id}")
    public ResponseEntity<String> updateStudentById(@PathVariable Integer student_id, @RequestBody Student student) {
        try {
            studentService.updateStudentById(student_id, student);
            return ResponseEntity.status(HttpStatus.OK).body("Updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/students/delete-student/{student_id}")
    public ResponseEntity<String> removeStudent(@PathVariable Integer student_id) {
        if (studentService.existsById(student_id)) {
            studentService.deleteStudentById(student_id);
            return new ResponseEntity<>("Removed successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Student does not exist", HttpStatus.NOT_FOUND);
    }
}