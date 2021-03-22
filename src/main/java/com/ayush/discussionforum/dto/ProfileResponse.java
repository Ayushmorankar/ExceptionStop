package com.ayush.discussionforum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private String publicStudentId;
    private String firstName;
    private String lastName;
    private String username;
    private Integer numberOfQuestionsAsked;
    private Integer numberOfAnswersGiven;
    private List<AnswerResponse> answers;
    private List<QuestionResponse> questions;
}
