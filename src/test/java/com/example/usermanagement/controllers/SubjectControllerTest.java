package com.example.usermanagement.controllers;

import com.example.usermanagement.DTOs.request.StudentRequestDTO;
import com.example.usermanagement.models.Role;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.repositories.SubjectRepository;
import com.example.usermanagement.services.StudentService;
import com.example.usermanagement.services.SubjectService;
import io.restassured.RestAssured;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class SubjectControllerTest {

    @LocalServerPort
    private Integer port;

    private String jwt;

    SubjectService subjectService;
    SubjectRepository subjectRepository;
    StudentService studentService;

    @Autowired
    public SubjectControllerTest(SubjectService subjectService, SubjectRepository subjectRepository, StudentService studentService) {
        this.subjectService = subjectService;
        this.subjectRepository = subjectRepository;
        this.studentService = studentService;
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        subjectRepository.deleteAll();
        Student admin = Student.builder()
                .email("admin@example.com")
                .role(Role.ADMIN)
                .password("parola")
                .build();

        studentService.register(admin);
        jwt = studentService.login(new StudentRequestDTO("admin@example.com", "parola"));
    }

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void should_return_all_subjects() {
        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .get("/subjects")
                .then()
                .statusCode(200);
    }

    @Test
    void getSpecificSubject() {
    }

    @Test
    void addSubject() {
    }

    @Test
    void updateSubject() {
    }

    @Test
    void deleteSubject() {
    }
}