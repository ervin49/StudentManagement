package com.example.usermanagement.repositories;

import com.example.usermanagement.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findStudentByEmail(String email);

    Student findTopByOrderByAbsencesDesc();
}
