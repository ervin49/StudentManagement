package com.example.usermanagement.controllers;

import com.example.usermanagement.DTOs.request.StudentRequestDTO;
import com.example.usermanagement.models.Grade;
import com.example.usermanagement.models.Role;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.models.Subject;
import com.example.usermanagement.repositories.GradeRepository;
import com.example.usermanagement.repositories.StudentRepository;
import com.example.usermanagement.repositories.SubjectRepository;
import com.example.usermanagement.services.GradeService;
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

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GradeControllerTest {

    @LocalServerPort
    private Integer port;
    private String jwt;
    private Student student;
    private Subject math;

    @Autowired
    GradeService gradeService;
    GradeRepository gradeRepository;
    StudentService studentService;
    SubjectService subjectService;
    StudentRepository studentRepository;
    SubjectRepository subjectRepository;

    @Autowired
    GradeControllerTest(GradeService gradeService, GradeRepository gradeRepository, StudentService studentService, SubjectService subjectService, StudentRepository studentRepository, SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
        this.subjectService = subjectService;
        this.gradeService = gradeService;
        this.gradeRepository = gradeRepository;
        this.studentService = studentService;
        this.studentRepository = studentRepository;
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

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        gradeRepository.deleteAll();
        subjectRepository.deleteAll();
        studentRepository.deleteAll();

        Student admin = Student.builder()
                .email("admin@example.com")
                .role(Role.ADMIN)
                .password("parola123")
                .build();

        student = Student.builder()
                .email("student@example.com")
                .role(Role.USER)
                .password("parola")
                .build();

        math = Subject.builder()
                .name("Math")
                .credits(5)
                .build();

        studentService.register(student);
        subjectService.addSubject(math);
        studentService.register(admin);
        jwt = studentService.login(new StudentRequestDTO("admin@example.com", "parola123"));
    }

    @Test
    void should_return_all_grades_of_current_logged_student() {
        String studentJwt = studentService.login(new StudentRequestDTO("student@example.com", "parola"));

        Grade grade1 = Grade.builder()
                .student(student)
                .subject(math)
                .value(5)
                .build();

        Grade grade2 = Grade.builder()
                .student(student)
                .subject(math)
                .value(10)
                .build();

        gradeService.addGrade(grade1);
        gradeService.addGrade(grade2);

        given()
                .header("Authorization", "Bearer " + studentJwt)
                .when()
                .get("/my-grades")
                .then()
                .statusCode(200)
                .body("[0].value", equalTo(5),
                        "[0].subject.name", equalTo("Math"),
                        "[1].value", equalTo(10),
                        "[1].subject.credits", equalTo(5));
    }

    @Test
    void should_get_specific_grade() {
        Grade grade = Grade.builder()
                .value(10)
                .student(student)
                .build();

        gradeService.addGrade(grade);
        Integer gradeId = grade.getId();

        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .get("/grades/{grade_id}", gradeId)
                .then()
                .body("value", equalTo(10),
                        "student.email", equalTo("student@example.com"));
    }

    @Test
    void should_add_grade() {
        Grade grade = Grade.builder()
                .value(10)
                .subject(math)
                .student(student)
                .build();

        given()
                .header("Authorization", "Bearer " + jwt)
                .contentType(ContentType.JSON)
                .body(grade)
                .when()
                .post("/grades/add")
                .then()
                .statusCode(201)
                .body(equalTo("Grade added successfully"));

        gradeService.addGrade(grade);
        Integer gradeId = grade.getId();

        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .get("/grades/{grade_id}", gradeId)
                .then()
                .statusCode(200)
                .body("value", equalTo(10),
                        "subject.name", equalTo("Math"),
                        "student.email", equalTo("student@example.com"));

        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .get("/grades/{grade_id}", 10)//this id does not exist at the moment
                .then()
                .statusCode(404)
                .body(equalTo(""));
    }

    @Test
    void should_update_grade() {
        Grade grade = Grade.builder()
                .value(10)
                .subject(math)
                .student(student)
                .build();

        Grade newGrade = Grade.builder()
                .value(9)
                .subject(math)
                .student(student)
                .build();

        gradeRepository.save(grade);
        Integer gradeId = grade.getId();

        given()
                .header("Authorization", "Bearer " + jwt)
                .contentType(ContentType.JSON)
                .body(newGrade)
                .when()
                .put("/grades/update-grade/{grade_id}", gradeId)
                .then()
                .statusCode(200)
                .body(equalTo("Grade updated successfully"));
    }

    @Test
    void should_delete_grade() {
        Grade grade = Grade.builder()
                .value(10)
                .subject(math)
                .student(student)
                .build();

        gradeService.addGrade(grade);
        Integer gradeId = grade.getId();

        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .delete("/grades/delete-grade/{grade_id}", gradeId)
                .then()
                .statusCode(200)
                .body(equalTo("Grade deleted successfully"));

        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .delete("/grades/delete-grade/{grade_id}", gradeId)
                .then()
                .statusCode(404)
                .body(equalTo("Grade doesn't exist"));
    }

    @Test
    void should_get_average_grade_of_student() {
        Subject chemistry = Subject.builder()
                .name("Chemistry")
                .credits(4)
                .build();

        Grade mathGrade = Grade.builder()
                .value(10)
                .subject(math)
                .student(student)
                .build();

        Grade chemistryGrade = Grade.builder()
                .value(9)
                .subject(chemistry)
                .student(student)
                .build();

        Integer studentId = student.getId();
        subjectService.addSubject(chemistry);
        gradeService.addGrade(chemistryGrade);
        gradeService.addGrade(mathGrade);

        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .get("/grades/average/{student_id}", studentId)
                .then()
                .statusCode(200)
                .body(equalTo("Student's average is: 9.56"));
    }
}