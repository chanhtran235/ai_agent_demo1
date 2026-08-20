package com.studentmanagement.repository;

import com.studentmanagement.entity.Student;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);
    Optional<Student> findByStudentCode(String studentCode);
    Optional<Student> findByEmail(String email);
}
