package com.studentmanagement.dto;

import java.time.LocalDate;

public record StudentResponse(Long id, String studentCode, String fullName, String email,
                              String phone, LocalDate dateOfBirth, String address) {
}
