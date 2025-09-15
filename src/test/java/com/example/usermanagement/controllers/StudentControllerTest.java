package com.example.usermanagement.controllers;

import com.example.usermanagement.DTOs.request.StudentRequestDTO;
import com.example.usermanagement.models.Role;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.repositories.StudentRepository;
import com.example.usermanagement.services.JwtService;
import com.example.usermanagement.services.StudentService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
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
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class StudentControllerTest {

    @LocalServerPort
    private Integer port;

    private String jwt;

    StudentRepository studentRepository;
    StudentService studentService;
    JwtService jwtService;

    @Autowired
    public StudentControllerTest(StudentRepository studentRepository, StudentService studentService, JwtService jwtService) {
        this.studentRepository = studentRepository;
        this.studentService = studentService;
        this.jwtService = jwtService;
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
        studentRepository.deleteAll();

        Student admin = Student.builder()
                .email("admin@example.com")
                .name("Admin")
                .role(Role.ADMIN)
                .absences(2)
                .password("parola")
                .build();

        studentService.register(admin);
        jwt = studentService.login(new StudentRequestDTO("admin@example.com", "parola"));
    }


    @Test
    void should_register_student() {
        Student student = Student.builder()
                .email("student@example.com")
                .password("parola123")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(student)
                .port(port)
                .when()
                .post("/register")
                .then()
                .statusCode(201)
                .body(equalTo("Registration successful."));
    }

    @Test
    void when_student_already_exists_return_bad_request() {
        Student student = Student.builder()
                .email("student@example.com")
                .password("parola123")
                .build();
        studentService.register(student);

        given()
                .contentType(ContentType.JSON)
                .body(student)
                .port(port)
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .body(equalTo("Student already exists"));
    }

    @Test
    void when_successful_login_return_jwt() {
        Student student = Student.builder()
                .email("student@example.com")
                .password("parola123")
                .role(Role.USER)
                .build();
        studentService.register(student);

        StudentRequestDTO studentDto = StudentRequestDTO.builder()
                .email("student@example.com")
                .password("parola123")
                .build();

        String response = given()
                .contentType(ContentType.JSON)
                .body(studentDto)
                .port(port)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract().response().path("Jwt");

        assertThat(jwtService.isJwt(response)).isEqualTo(true);
    }

    @Test
    void when_bad_login_return_email_or_password_wrong() {
        Student student = Student.builder()
                .email("student@example.com")
                .password("parola123")
                .build();
        studentService.register(student);

        StudentRequestDTO studentWithWrongPassword = StudentRequestDTO.builder()
                .email("student@example.com")
                .password("parola124")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(studentWithWrongPassword)
                .port(port)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .body("Message", equalTo("Email or password wrong!"));
    }

    @Test
    void should_return_all_students() {
        Student student = Student.builder()
                .email("student@example.com")
                .password("parola123")
                .build();
        studentService.register(student);

        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .get("/students")
                .then()
                .statusCode(200)
                .body("[0].email", equalTo("admin@example.com"),
                        "[1].email", equalTo("student@example.com"));
    }

    @Test
    void should_return_student_details() {
        Student student = Student.builder()
                .name("Dragos")
                .email("dragos@gmail.com")
                .age(21)
                .role(Role.USER)
                .absences(7)
                .password("parola")
                .build();

        studentService.register(student);

        String newJwt = studentService.login(new StudentRequestDTO("dragos@gmail.com", "parola"));

        given()
                .header("Authorization", "Bearer " + newJwt)
                .when()
                .get("/my-details")
                .then()
                .statusCode(200)
                .body("email", equalTo("dragos@gmail.com"),
                        "absences", equalTo(7),
                        "age", equalTo(21),
                        "name", equalTo("Dragos"));
    }

    @Test
    void should_return_most_absences_of_all_students() {
        Student student1 = Student.builder()
                .email("student1@example.com")
                .password("parola")
                .name("Ion")
                .absences(10)
                .build();

        Student student2 = Student.builder()
                .email("student2@example.com")
                .password("parola")
                .name("Raluca")
                .absences(4)
                .build();

        Student student3 = Student.builder()
                .email("student3@example.com")
                .password("parola")
                .absences(14)
                .name("Andrei")
                .build();

        studentService.register(student1);
        studentService.register(student2);
        studentService.register(student3);
        RestAssured.defaultParser = Parser.JSON;

        given()
                .header("Authorization", "Bearer " + jwt)
                .contentType(ContentType.JSON)
                .when()
                .get("/students/most-absences")
                .then()
                .statusCode(200)
                .body("absences", equalTo(14),
                        "name", equalTo("Andrei"));
    }

    @Test
    void should_update_student() {
        Student oldStudent = Student.builder()
                .email("old@example.com")
                .password("parola")
                .build();

        Student newStudent = Student.builder()
                .email("new@example.com")
                .password("parola")
                .build();

        studentService.register(oldStudent);

        given()
                .contentType(ContentType.JSON)
                .body(newStudent)
                .header("Authorization", "Bearer " + jwt)
                .when()
                .put("/students/update/{student_id}", 1)
                .then()
                .statusCode(200)
                .body(equalTo("Updated successfully"));
    }

    @Test
    void should_remove_student() {
        Student student = Student.builder()
                .email("student@example.com")
                .password("parola")
                .build();

        studentService.register(student);
        System.out.println(student.getId());

        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .delete("/students/delete-student/{student_id}", 13)
                .then()
                .statusCode(200)
                .body(equalTo("Removed successfully"));

        given()
                .header("Authorization", "Bearer " + jwt)
                .when()
                .delete("/students/delete-student/{student_id}", 13)
                .then()
                .statusCode(404)
                .body(equalTo("Student does not exist"));
    }
}