package com.example.usermanagement.controllers;

import com.example.usermanagement.models.Grade;
import com.example.usermanagement.services.GradeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class GradeController {
    GradeServiceImpl gradeService;

    @Autowired
    public GradeController(GradeServiceImpl gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/grades")
    public List<Grade> getAllGrades() {
        return gradeService.getAllGrades();
    }

    @GetMapping("/grades/{grade_id}")
    public Optional<Grade> getGrade(@PathVariable Integer grade_id) {
        return gradeService.getSpecificGrade(grade_id);
    }

    @PostMapping("/add-grade")
    public ResponseEntity<Grade> addGrade(@RequestBody Grade grade) {
        gradeService.addGrade(grade);
        return ResponseEntity.ok().body(grade);
    }

    @PutMapping("/update-grade/{grade_id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Integer grade_id, @RequestBody Grade grade) {
        gradeService.updateGrade(grade_id, grade);
        return ResponseEntity.ok().body(grade);
    }

    @DeleteMapping("/delete-grade/{grade_id}")
    public ResponseEntity<Grade> deleteGrade(@PathVariable Integer grade_id) {
        try {
            gradeService.deleteGrade(grade_id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}