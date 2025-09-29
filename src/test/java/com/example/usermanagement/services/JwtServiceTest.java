package com.example.usermanagement.services;

import com.example.usermanagement.models.Role;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.services.impl.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class JwtServiceTest {

    JwtService jwtService;
    UserDetailsImpl userDetails;
    Student student;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @Autowired
    JwtServiceTest(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .role(Role.USER)
                .email("student@example.com")
                .build();

        userDetails = UserDetailsImpl.builder()
                .student(student)
                .build();
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void should_generate_token() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractClaim(token, Claims::getSubject);

        assertThat(token)
                .isNotNull();
        assertThat(username)
                .isEqualTo("student@example.com");
        assertThat(jwtService.isTokenExpired(token))
                .isFalse();
        assertThat(jwtService.isJwt(token))
                .isTrue();
    }

    @Test
    void when_token_is_expired_return_false() {
        String expiredToken = Jwts.builder()
                .setSubject("student1@example.com")
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("1982c65d2592ba48322bd8b605f03d76fda133fc64e66a7cda1b353633b5bc33")))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .compact();

        Boolean check = jwtService.isTokenValid(expiredToken, userDetails);

        assertThat(check).isFalse();
    }

    @Test
    void when_token_has_wrong_subject_return_false() {
        String tokenWithWrongSubject = Jwts.builder()
                .setSubject("student@example.com")
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("1982c65d2592ba48322bd8b605f03d76fda133fc64e66a7cda1b353633b5bc33")))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .compact();

        Boolean check = jwtService.isTokenValid(tokenWithWrongSubject, userDetails);

        assertThat(check).isFalse();
    }
}