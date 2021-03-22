package com.ayush.discussionforum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResponse {
    private Long id;
    private String publicStudentId;
    private String answerOfStudent;
    private String text;
    private Integer numberOfFollowups;
    private String duration;
}
