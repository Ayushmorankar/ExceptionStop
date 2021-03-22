package com.ayush.discussionforum.controller;

import com.ayush.discussionforum.dto.QuestionRequest;
import com.ayush.discussionforum.dto.QuestionResponse;
import com.ayush.discussionforum.model.Thread;
import com.ayush.discussionforum.service.QuestionService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("v1/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    @ApiOperation(value = "Create question")
    @PostMapping()
    public ResponseEntity<?> createQuestion(@RequestBody @Valid QuestionRequest questionRequest){
        questionService.createQuestion(questionRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get question by its id")
    @GetMapping("/{id}")
    public QuestionResponse getQuestionById(@PathVariable Long id){
        return questionService.getById(id);
    }

    @ApiOperation(value = "Get all questions by threads(subTopics)")
    @GetMapping("by-thread/{thread}")
    public List<QuestionResponse> getByThread(@PathVariable Thread thread){
        return questionService.getByThread(thread);
    }

    @ApiOperation(value = "Get all questions asked by student by student public id")
    @GetMapping("by-student/{publicStudentId}")
    public List<QuestionResponse> getByStudent(@PathVariable String publicStudentId){
        return questionService.getByStudent(publicStudentId);
    }

    @ApiOperation(value = "Get all questions asked by student requesting")
    @GetMapping("/my-questions")
    public List<QuestionResponse> getQuestionsOfCurrentStudent(){
        return questionService.getQuestionsOfCurrentStudent();
    }

    @ApiOperation(value = "Mark an answer as the best explanation to the question asked",
    notes = "Only student who asked the question is allowed to do this operation")
    @GetMapping("/mark-answered/{questionId}/{answerId}")
    public void markAnswered(@PathVariable Long answerId, @PathVariable Long questionId){
        questionService.markAnswered(questionId, answerId);
    }
}
