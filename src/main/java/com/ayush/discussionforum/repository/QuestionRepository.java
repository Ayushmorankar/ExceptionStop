package com.ayush.discussionforum.repository;

import com.ayush.discussionforum.model.Question;
import com.ayush.discussionforum.model.Thread;
import com.ayush.discussionforum.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByThread(Thread thread);
    List<Question> findByStudent(Student student);
}
