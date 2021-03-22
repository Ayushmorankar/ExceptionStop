package com.ayush.discussionforum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowupResponse {
    private String publicStudentId;
    private String username;
    private String text;
    private Long answerId;
    private String duration;
}
