package com.ayush.discussionforum.service;

import com.ayush.discussionforum.dto.AnswerResponse;
import com.ayush.discussionforum.dto.ProfileResponse;
import com.ayush.discussionforum.dto.QuestionResponse;
import com.ayush.discussionforum.model.Answer;
import com.ayush.discussionforum.model.Question;
import com.ayush.discussionforum.model.Student;
import com.ayush.discussionforum.repository.StudentRepository;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProfileService {

    private final StudentRepository studentRepository;

    public ProfileResponse getById(String publicStudentId){

        Student student = studentRepository.findByPublicStudentId(publicStudentId)
                .orElseThrow(()-> new UsernameNotFoundException("Profile not found"));

        List<AnswerResponse> answerResponseList = getAnswers(student);
        List<QuestionResponse> questionResponseList = getQuestions(student);

        return ProfileResponse.builder()
                .username(student.getUsername())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .answers(answerResponseList)
                .questions(questionResponseList)
                .numberOfAnswersGiven(answerResponseList.size())
                .numberOfQuestionsAsked(questionResponseList.size())
                .publicStudentId(student.getPublicStudentId())
                .build();
    }

    private List<AnswerResponse> getAnswers(Student student){
        return student.getAnswers().stream()
                .map(this::mapAnswerToResponse)
                .collect(Collectors.toList());
    }

    private List<QuestionResponse> getQuestions(Student student){
        return student.getQuestions().stream()
                .map(this::mapQuestionToResponse)
                .collect(Collectors.toList());
    }

    private AnswerResponse mapAnswerToResponse(Answer answer){
        return AnswerResponse.builder()
                .answerOfStudent(answer.getStudent().getUsername())
                .id(answer.getId())
                .text(answer.getText())
                .numberOfFollowups(answer.getFollowups().size())
                .duration(getDuration(answer))
                .publicStudentId(answer.getStudent().getPublicStudentId())
                .build();
    }

    private String getDuration(Answer answer){
        return TimeAgo.using(answer.getCreatedDate().toEpochMilli());
    }

    private QuestionResponse mapQuestionToResponse(Question question){
        return QuestionResponse.builder()
                .answeredBy(question.getAnsweredBy())
                .askedBy(question.getStudent().getUsername())
                .numberOfAnswers(question.getAnswers().size())
                .text(question.getText())
                .id(question.getId())
                .duration(getDuration(question))
                .publicStudentId(question.getStudent().getPublicStudentId())
                .build();
    }

    private String getDuration(Question question){
        return TimeAgo.using(question.getCreatedDate().toEpochMilli());
    }
}
