package com.studentmanagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentmanagement.dto.PageResponse;
import com.studentmanagement.dto.StudentRequest;
import com.studentmanagement.dto.StudentResponse;
import com.studentmanagement.exception.GlobalExceptionHandler;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.service.StudentService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StudentController.class)
@Import(GlobalExceptionHandler.class)
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    void getAllStudentsReturnsPage() throws Exception {
        StudentResponse student = response();
        when(studentService.getAllStudents(any())).thenReturn(new PageResponse<>(List.of(student), 0, 10, 1, 1));

        mockMvc.perform(get("/api/students?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("Ada Lovelace"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void createStudentReturnsCreatedStudent() throws Exception {
        StudentRequest request = request();
        when(studentService.createStudent(request)).thenReturn(response());

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.studentCode").value("STU-001"));
    }

    @Test
    void createStudentReturnsBadRequestForInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentCode\":\"\",\"fullName\":\"\",\"email\":\"invalid\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    void getStudentReturnsNotFoundError() throws Exception {
        when(studentService.getStudentById(77L)).thenThrow(new ResourceNotFoundException("Student with id 77 was not found"));

        mockMvc.perform(get("/api/students/77"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student with id 77 was not found"));
    }

    @Test
    void deleteStudentReturnsNoContent() throws Exception {
        doNothing().when(studentService).deleteStudent(eq(1L));

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
    }

    private StudentRequest request() {
        return new StudentRequest("STU-001", "Ada Lovelace", "ada@example.com", "+1 555 0100",
                LocalDate.of(2000, 12, 10), "123 Example Street");
    }

    private StudentResponse response() {
        return new StudentResponse(1L, "STU-001", "Ada Lovelace", "ada@example.com", "+1 555 0100",
                LocalDate.of(2000, 12, 10), "123 Example Street");
    }
}
