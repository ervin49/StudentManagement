package com.example.usermanagement;

import com.example.usermanagement.models.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserManagementApplication {

    public static void main(String[] args) {
        Student student1 = new Student(1, "834512755", "Mihnea", 5);
        Student student2 = new Student(1, "843538457", "Mihai", 8);
        SpringApplication.run(UserManagementApplication.class, args);
    }
}
