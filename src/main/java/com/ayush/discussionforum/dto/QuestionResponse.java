package com.ayush.discussionforum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {
    private Long id;
    private String publicStudentId;
    private String askedBy;
    private String text;
    private String answeredBy;
    private String duration;
    private Integer numberOfAnswers;
}
