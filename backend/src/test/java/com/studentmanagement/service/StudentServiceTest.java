package com.studentmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.studentmanagement.dto.StudentRequest;
import com.studentmanagement.dto.StudentResponse;
import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.DuplicateResourceException;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.StudentRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void createStudentPersistsAndReturnsStudent() {
        StudentRequest request = request();
        when(studentRepository.findByStudentCode(request.studentCode())).thenReturn(Optional.empty());
        when(studentRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        StudentResponse result = studentService.createStudent(request);

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        assertThat(captor.getValue().getFullName()).isEqualTo("Ada Lovelace");
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo("ada@example.com");
    }

    @Test
    void createStudentRejectsDuplicateCode() {
        Student existing = student(5L);
        when(studentRepository.findByStudentCode("STU-001")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> studentService.createStudent(request()))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Student code already exists");
    }

    @Test
    void getStudentByIdThrowsWhenStudentDoesNotExist() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deleteStudentDeletesExistingStudent() {
        Student existing = student(3L);
        when(studentRepository.findById(3L)).thenReturn(Optional.of(existing));

        studentService.deleteStudent(3L);

        verify(studentRepository).delete(existing);
    }

    private StudentRequest request() {
        return new StudentRequest("STU-001", "Ada Lovelace", "ada@example.com", "+1 555 0100",
                LocalDate.of(2000, 12, 10), "123 Example Street");
    }

    private Student student(Long id) {
        Student student = new Student();
        student.setId(id);
        student.setStudentCode("STU-001");
        student.setFullName("Ada Lovelace");
        student.setEmail("ada@example.com");
        return student;
    }
}
