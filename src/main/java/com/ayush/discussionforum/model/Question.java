package com.ayush.discussionforum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Thread thread;
    @Lob
    private String text;
    private Instant createdDate;
    @Nullable
    private String answeredBy;
    @OneToMany
    private List<Answer> answers;
    @ManyToOne
    private Student student;
}
