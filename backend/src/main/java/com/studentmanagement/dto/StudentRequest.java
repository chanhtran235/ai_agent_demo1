package com.studentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record StudentRequest(
        @NotBlank @Size(max = 30) @Pattern(regexp = "[A-Za-z0-9-]+", message = "must contain only letters, numbers, and hyphens") String studentCode,
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Email @Size(max = 254) String email,
        @Size(max = 30) String phone,
        @Past(message = "must be a date in the past") LocalDate dateOfBirth,
        @Size(max = 500) String address) {
}
