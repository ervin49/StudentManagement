package com.example.usermanagement.configs;

import com.example.usermanagement.models.Role;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AdminAccountConfig implements CommandLineRunner {
    private StudentRepository studentRepository;
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        Student admin = Student.builder()
                .email("admin@example.com")
                .password(encoder.encode("admin"))
                .role(Role.ADMIN)
                .build();

        studentRepository.save(admin);
    }
}
