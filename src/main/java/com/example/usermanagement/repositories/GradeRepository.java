package com.example.usermanagement.repositories;

import com.example.usermanagement.DTOs.GradeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.usermanagement.models.Grade;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {

    List<GradeDTO> findGradesByStudent_Id(Integer studentId);
}
