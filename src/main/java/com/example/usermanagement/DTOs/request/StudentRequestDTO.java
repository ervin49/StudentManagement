package com.example.usermanagement.DTOs.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequestDTO {
    @NotBlank(message = "Username can't be empty")
    private String email;

    @NotBlank(message = "Password can't be empty")
    private String password;
}
