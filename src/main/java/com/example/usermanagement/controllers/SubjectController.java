package com.example.usermanagement.controllers;

import com.example.usermanagement.models.Subject;
import com.example.usermanagement.services.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class SubjectController {

    SubjectService subjectService;

    @GetMapping("/subjects")
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/subjects/{subject_id}")
    public Optional<Subject> getSpecificSubject(@PathVariable Integer subject_id) {
        return subjectService.getSpecificSubject(subject_id);
    }

    @PostMapping("/subjects/add")
    public ResponseEntity<Subject> addSubject(@RequestBody Subject subject) {
        try {
            subjectService.addSubject(subject);
            return ResponseEntity.ok().body(subject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/subjects/update-subject/{subject_id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Integer subject_id, @RequestBody Subject subject) {
        try {
            subjectService.updateSubject(subject_id, subject);
            return ResponseEntity.ok().body(subject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/subjects/delete-subject/{subject_id}")
    public ResponseEntity<Subject> deleteSubject(@PathVariable Integer subject_id) {
        try {
            subjectService.deleteSubject(subject_id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
