package com.ayush.discussionforum.repository;

import com.ayush.discussionforum.model.Answer;
import com.ayush.discussionforum.model.Followup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowupRepository extends JpaRepository<Followup, Long> {
    List<Followup> findByAnswer(Answer answer);
}
