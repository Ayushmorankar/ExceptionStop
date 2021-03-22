package com.ayush.discussionforum.dto;

import com.ayush.discussionforum.model.Thread;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class QuestionRequest {
    @NotBlank(message = "Cannot be empty")
    private String text;
    private Thread thread;
}
