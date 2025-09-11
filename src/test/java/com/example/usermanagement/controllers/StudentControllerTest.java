package com.example.usermanagement.controllers;

import com.example.usermanagement.models.Student;
import com.example.usermanagement.services.JwtService;
import com.example.usermanagement.services.StudentService;
import com.example.usermanagement.services.impl.UserDetailsServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class)
class StudentControllerTest {

    @MockitoBean
    StudentService studentService;

    @Autowired
    MockMvc mvc;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    UserDetailsServiceImpl userDetailsService;

    @Test
    void good_register() throws Exception {

        Student student = Student.builder()
                .email("student@example.com")
                .password("parola123")
                .build();
        Gson gson = new Gson();

        when(studentService.register(student)).thenReturn("Registration successful.");
        mvc.perform(post("/register")
                        .content(gson.toJson(student))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isCreated(),
                        content().string("Registration successful."));

        when(studentService.register(student)).thenReturn("Student already exists");
        mvc.perform(post("/register")
                        .content(gson.toJson(student))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isBadRequest(),
                        content().string("Student already exists"));
    }

//    @Test
//    void bad_register() throws Exception {
//        Student badStudent = Student.builder().build();
//        mvc.perform(post("/register"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void login() {
//    }
//
//    @Test
//    void getAllStudents() throws Exception {
//        mvc.perform(get("/students"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void getMyDetails() {
//    }
//
//    @Test
//    void welcome() throws Exception {
//        mvc.perform(get("/"))
//                .andExpect(content().string("Welcome!"));
//    }
//
//    @Test
//    void getMostAbsencesOfAllStudents() {
//    }
//
//    @Test
//    void updateStudentById() {
//    }
//
//    @Test
//    void removeStudent() {
//    }
}