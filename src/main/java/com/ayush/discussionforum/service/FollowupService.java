package com.ayush.discussionforum.service;

import com.ayush.discussionforum.dto.FollowupRequest;
import com.ayush.discussionforum.dto.FollowupResponse;
import com.ayush.discussionforum.model.Answer;
import com.ayush.discussionforum.model.Followup;
import com.ayush.discussionforum.model.Student;
import com.ayush.discussionforum.repository.AnswerRepository;
import com.ayush.discussionforum.repository.FollowupRepository;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@AllArgsConstructor
@Service
public class FollowupService {

    private final FollowupRepository followupRepository;
    private final AuthService authService;
    private final AnswerRepository answerRepository;

    @Transactional
    public void createFollowup(FollowupRequest followupRequest){
        Answer answer = answerRepository.findById(followupRequest.getAnswerId()).
                orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(404)));
        Followup followup = mapRequestToFollowup(followupRequest, answer);
        followupRepository.save(followup);
        answer.getFollowups().add(followup);
    }

    public List<FollowupResponse> getByAnswerId(Long answerId){
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(404)));
        return followupRepository.findByAnswer(answer)
                .stream()
                .map(this::mapFollowupToResponse)
                .collect(Collectors.toList());
    }

    private Followup mapRequestToFollowup(FollowupRequest followupRequest, Answer answer){

        Student student = authService.getCurrentStudent();

        return Followup.builder()
                .student(student)
                .answer(answer)
                .text(followupRequest.getText())
                .createdDate(Instant.now())
                .build();
    }

    private FollowupResponse mapFollowupToResponse(Followup followup){
        Student student = followup.getStudent();
        return FollowupResponse.builder()
                .publicStudentId(student.getPublicStudentId())
                .text(followup.getText())
                .answerId(followup.getAnswer().getId())
                .username(student.getUsername())
                .duration(getDuration(followup))
                .build();
    }

    private String getDuration(Followup followup){
        return TimeAgo.using(followup.getCreatedDate().toEpochMilli());
    }
}
