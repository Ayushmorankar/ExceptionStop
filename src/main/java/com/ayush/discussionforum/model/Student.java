package com.ayush.discussionforum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String publicStudentId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    @Nullable
    private String cfUsername;
    private Instant createdDate;
    private boolean enabled;
    @OneToMany
    private List<Answer> answers;
    @OneToMany
    private List<Question> questions;
    private Byte activeSessions;
    private boolean agreedToTerms;
}
