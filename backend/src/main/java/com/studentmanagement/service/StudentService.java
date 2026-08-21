package com.studentmanagement.service;

import com.studentmanagement.dto.PageResponse;
import com.studentmanagement.dto.StudentRequest;
import com.studentmanagement.dto.StudentResponse;
import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.DuplicateResourceException;
import com.studentmanagement.exception.ResourceNotFoundException;
import com.studentmanagement.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public PageResponse<StudentResponse> getAllStudents(Pageable pageable) {
        return toPageResponse(studentRepository.findAll(pageable));
    }

    public StudentResponse getStudentById(Long id) {
        return toResponse(findStudent(id));
    }

    public PageResponse<StudentResponse> searchStudents(String name, Pageable pageable) {
        return toPageResponse(studentRepository.findByFullNameContainingIgnoreCase(name.trim(), pageable));
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        ensureUnique(request, null);
        Student student = new Student();
        applyRequest(student, request);
        return toResponse(studentRepository.save(student));
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = findStudent(id);
        ensureUnique(request, id);
        applyRequest(student, request);
        return toResponse(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.delete(findStudent(id));
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " was not found"));
    }

    private void ensureUnique(StudentRequest request, Long currentId) {
        studentRepository.findByStudentCode(request.studentCode()).filter(s -> !s.getId().equals(currentId))
                .ifPresent(s -> { throw new DuplicateResourceException("Student code already exists"); });
        studentRepository.findByEmail(request.email()).filter(s -> !s.getId().equals(currentId))
                .ifPresent(s -> { throw new DuplicateResourceException("Email already exists"); });
    }

    private void applyRequest(Student student, StudentRequest request) {
        student.setStudentCode(request.studentCode());
        student.setFullName(request.fullName());
        student.setEmail(request.email());
        student.setPhone(request.phone());
        student.setDateOfBirth(request.dateOfBirth());
        student.setAddress(request.address());
    }

    private PageResponse<StudentResponse> toPageResponse(Page<Student> page) {
        return new PageResponse<>(page.getContent().stream().map(this::toResponse).toList(),
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(student.getId(), student.getStudentCode(), student.getFullName(),
                student.getEmail(), student.getPhone(), student.getDateOfBirth(), student.getAddress());
    }
}
