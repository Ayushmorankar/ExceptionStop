package com.ayush.discussionforum.repository;

import com.ayush.discussionforum.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);
    Optional<Student> findByPublicStudentId(String publicStudentId);
}
