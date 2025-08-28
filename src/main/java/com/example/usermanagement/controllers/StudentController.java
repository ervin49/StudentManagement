package com.example.usermanagement.controllers;

import com.example.usermanagement.DTOs.request.StudentRequestDTO;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.services.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
            if (str.equals("Registration successful."))
                return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student already exists");
        } catch (Exception e) {
            log.error("e: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody StudentRequestDTO studentRequestDTO) {
        String str = studentService.login(studentRequestDTO);
        if(str.equals("Email or password wrong!"))
            return new ResponseEntity<>(str,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(str, HttpStatus.OK);
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/my-details")
    public Student getMyDetails(){
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            String email = authentication.getName();
            return studentService.getStudentByEmail(email);
        }
        return null;
    }

    @GetMapping("/")
    public String welcome() {
        return "Welcome!";
    }

    @GetMapping("/students/most-absences")
    public String getMostAbsencesOfAllStudents() {
        StringBuilder name = new StringBuilder();
        Integer absences = studentService.mostAbsences(name);
        if(absences != null)
            return "Student " + name + " has most absences: " + absences;
        return "There is no student in the database!";
    }

    @PutMapping("/students/update/{student_id}")
    public ResponseEntity<String> updateStudentById(@PathVariable Integer student_id, @RequestBody Student student) {
        try {
            studentService.updateStudentById(student_id, student);
            return ResponseEntity.status(HttpStatus.OK).body("Update successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/students/delete-student/{student_id}")
    public ResponseEntity<Student> removeStudent(@PathVariable Integer student_id) {
        try {
            studentService.deleteStudentById(student_id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}