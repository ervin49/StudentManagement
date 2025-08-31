package com.example.usermanagement.services;

import com.example.usermanagement.models.Role;
import com.example.usermanagement.models.Student;
import com.example.usermanagement.services.impl.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    Student admin = Student.builder()
            .email("admin@example.com")
            .role(Role.valueOf("ADMIN"))
            .password("parola123")
            .build();
    Student student = Student.builder()
            .email("student@example.com")
            .role(Role.valueOf("USER"))
            .password("parola123")
            .build();
    UserDetailsImpl studentDetails = new UserDetailsImpl(student);
    UserDetailsImpl adminDetails = new UserDetailsImpl(admin);

    @Test
    public void generate_token(){
        String studentToken = jwtService.generateToken(studentDetails);
        String adminToken = jwtService.generateToken(adminDetails);

        Claims studentClaims = Jwts.parserBuilder()
                .setSigningKey(jwtService.getSignInKey())
                .build()
                .parseClaimsJws(studentToken)
                .getBody();

        Claims adminClaims = Jwts.parserBuilder()
                .setSigningKey(jwtService.getSignInKey())
                .build()
                .parseClaimsJws(adminToken)
                .getBody();

        assertThat(studentToken)
                .isNotNull()
                .isNotEmpty();
        assertThat(adminToken)
                .isNotNull()
                .isNotEmpty();
        assertThat(studentClaims.getSubject()).isEqualTo("student@example.com");

        assertThat(studentClaims.get("role")).isEqualTo("USER");
        assertThat(adminClaims.getSubject()).isEqualTo("admin@example.com");
        assertThat(adminClaims.get("role")).isEqualTo("ADMIN");
    }

    @Test
    public void validate_token(){
       String token;
       Boolean result = jwtService.isTokenValid(token,studentDetails);

       assertThat(result).isTrue();
    }
}