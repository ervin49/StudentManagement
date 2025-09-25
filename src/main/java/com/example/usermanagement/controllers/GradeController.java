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
    public List<Grade> getAllGrades() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Student student = studentService.getStudentByEmail(email);
            return gradeService.getGradesOfStudent(student.getId());
        }
        return List.of();
    }

    @GetMapping("/grades/{grade_id}")
    public Optional<Grade> getGrade(@PathVariable Integer grade_id) {
        return gradeService.getSpecificGrade(grade_id);
    }


    @PostMapping("/grades/add")
    public ResponseEntity<String> addGrade(@RequestBody Grade grade) {
        gradeService.addGrade(grade);
        return ResponseEntity.ok().body("Grade added");
    }

    @PutMapping("/grades/update-grade/{grade_id}")
    public ResponseEntity<String> updateGrade(@PathVariable Integer grade_id, @RequestBody Grade grade) {
        gradeService.updateGrade(grade_id, grade);
        return ResponseEntity.ok().body("Grade updated");
    }

    @DeleteMapping("/grades/delete-grade/{grade_id}")
    public ResponseEntity<Grade> deleteGrade(@PathVariable Integer grade_id) {
        try {
            gradeService.deleteGrade(grade_id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/average-of-student/{student_id}")
    public ResponseEntity<String> getAverageOfStudent(@PathVariable Integer student_id) {
        return ResponseEntity.ok("Student's average is: " + gradeService.getAverage(student_id));
    }
}