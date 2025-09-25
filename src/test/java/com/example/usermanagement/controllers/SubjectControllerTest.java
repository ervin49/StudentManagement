package com.example.usermanagement.controllers;

import com.example.usermanagement.DTOs.request.StudentRequestDTO;
import com.example.usermanagement.models.Role;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.models.Subject;
import com.example.usermanagement.repositories.StudentRepository;
import com.example.usermanagement.repositories.SubjectRepository;
import com.example.usermanagement.services.StudentService;
import com.example.usermanagement.services.SubjectService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
import static org.hamcrest.Matchers.equalTo;
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
    StudentRepository studentRepository;

    @Autowired
    public SubjectControllerTest(SubjectService subjectService, SubjectRepository subjectRepository, StudentService studentService, StudentRepository studentRepository) {
        this.subjectService = subjectService;
        this.subjectRepository = subjectRepository;
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        studentRepository.deleteAll();
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
        Subject math = Subject.builder()
                .name("Maths")
                .build();
        Subject physics = Subject.builder()
                .credits(5)
                .build();

        subjectService.addSubject(math);
        subjectService.addSubject(physics);

        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .get("/subjects")
                .then()
                .statusCode(200)
                .body("[0].name", equalTo("Maths"),
                        "[1].credits", equalTo(5));
    }

    @Test
    void getSpecificSubject() {
        Subject math = Subject.builder()
                .name("Maths")
                .build();
        subjectService.addSubject(math);
        Integer mathId = math.getId();

        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .get("/subjects/{subject_id}", mathId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Maths"));
    }

    @Test
    void should_add_subject() {
        Subject math = Subject.builder()
                .name("Maths")
                .build();

        given()
                .header("Authorization", "Bearer " + jwt)
                .contentType(ContentType.JSON)
                .body(math)
                .when()
                .post("/subjects/add")
                .then()
                .statusCode(201)
                .body(equalTo("Subject added successfully"));
    }

    @Test
    void updateSubject() {
        Subject math = Subject.builder()
                .name("Maths")
                .build();

        subjectService.addSubject(math);
        Integer mathId = math.getId();

        Subject chemistry = Subject.builder()
                .name("Chemistry")
                .build();

        given()
                .header("Authorization", "Bearer " + jwt)
                .contentType(ContentType.JSON)
                .body(chemistry)
                .when()
                .put("/subjects/update-subject/{subject_id}", mathId)
                .then()
                .statusCode(200)
                .body(equalTo("Subject updated successfully"));
    }

    @Test
    void deleteSubject() {
        Subject math = Subject.builder()
                .name("Maths")
                .build();

        subjectService.addSubject(math);
        Integer subjectId = math.getId();

        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .delete("/subjects/delete-subject/{subject_id}", subjectId)
                .then()
                .statusCode(200)
                .body(equalTo("Subject deleted successfully"));

        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .delete("/subjects/delete-subject/{subject_id}", subjectId)
                .then()
                .statusCode(404)
                .body(equalTo("Subject doesn't exist"));
    }
}