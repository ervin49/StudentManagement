package com.example.usermanagement.controllers;

import com.example.usermanagement.models.Grade;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.services.GradeService;
import com.example.usermanagement.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class GradeController {
    private final GradeService gradeService;
    private final StudentService studentService;

    @GetMapping("/my-grades")
    public ResponseEntity<List<Grade>> getAllGrades() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Student student = studentService.getStudentByEmail(email);
            return ResponseEntity.ok(gradeService.getGradesOfStudent(student.getId()));
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/grades/{grade_id}")
    public ResponseEntity<Grade> getGrade(@PathVariable Integer grade_id) {
        Optional<Grade> grade = gradeService.getSpecificGrade(grade_id);
        if (grade.isPresent())
            return new ResponseEntity<>(gradeService.getSpecificGrade(grade_id).get(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/grades/add")
    public ResponseEntity<String> addGrade(@RequestBody Grade grade) {
        gradeService.addGrade(grade);
        return new ResponseEntity<>("Grade added successfully", HttpStatus.CREATED);
    }

    @PutMapping("/grades/update-grade/{grade_id}")
    public ResponseEntity<String> updateGrade(@PathVariable Integer grade_id, @RequestBody Grade grade) {
        gradeService.updateGrade(grade_id, grade);
        return new ResponseEntity<>("Grade updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/grades/delete-grade/{grade_id}")
    public ResponseEntity<String> deleteGrade(@PathVariable Integer grade_id) {
        if (gradeService.getSpecificGrade(grade_id).isPresent()) {
            gradeService.deleteGrade(grade_id);
            return new ResponseEntity<>("Grade deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Grade doesn't exist", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/grades/average/{student_id}")
    public ResponseEntity<String> getAverageOfStudent(@PathVariable Integer student_id) {
        return ResponseEntity.ok("Student's average is: " + gradeService.getAverage(student_id));
    }
}