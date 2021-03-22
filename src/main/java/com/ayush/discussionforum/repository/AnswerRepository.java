package com.ayush.discussionforum.repository;

import com.ayush.discussionforum.model.Answer;
import com.ayush.discussionforum.model.Question;
import com.ayush.discussionforum.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> queryAnswerByQuestionAndStudent(Question question, Student student);
    List<Answer> findByStudent(Student student);
}
