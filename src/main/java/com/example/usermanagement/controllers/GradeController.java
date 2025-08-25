package com.example.usermanagement.controllers;

import com.example.usermanagement.DTOs.response.GradeDTO;
import com.example.usermanagement.models.Grade;
import com.example.usermanagement.services.GradeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class GradeController {
    GradeService gradeService;

    @GetMapping("/grades")
    public List<Grade> getAllGrades() {
        return gradeService.getAllGrades();
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

    @GetMapping("/my-grades")
    public ResponseEntity<List<GradeDTO>> getAllGrades(@AuthenticationPrincipal Integer student_id) {
        return ResponseEntity.ok(gradeService.getGradesOfStudent(student_id));
    }

    @GetMapping("/average-of-student/{student_id}")
    public ResponseEntity<String> getAverageOfStudent(@PathVariable Integer student_id) {
        return ResponseEntity.ok("Student's average is: " + gradeService.getAverage(student_id));
    }
}