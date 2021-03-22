package com.ayush.discussionforum.controller;

import com.ayush.discussionforum.dto.AnswerRequest;
import com.ayush.discussionforum.dto.AnswerResponse;
import com.ayush.discussionforum.service.AnswerService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("v1/api/answers")
public class AnswerController {

    private final AnswerService answerService;

    @ApiOperation(value = "Create an answer")
    @PostMapping()
    public ResponseEntity<?> createAnswer(@RequestBody @Valid AnswerRequest answerRequest){
        answerService.createAnswer(answerRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get answer by its id")
    @GetMapping("/{answerId}")
    public AnswerResponse getById(@PathVariable Long answerId){
        return answerService.getById(answerId);
    }

    @ApiOperation(value = "Get all the answers of a questions by its id")
    @GetMapping("/by-question/{questionId}")
    public List<AnswerResponse> getByQuestion(@PathVariable Long questionId){
        return answerService.getByQuestion(questionId);
    }

    @ApiOperation(value = "Get all answers by a student by public id of the student")
    @GetMapping("/by-student/{publicStudentId}")
    public List<AnswerResponse> getByStudent(@PathVariable String publicStudentId){
        return answerService.getByStudent(publicStudentId);
    }

    @ApiOperation(value = "Get all answers of student requesting it")
    @GetMapping("/my-answers")
    public List<AnswerResponse> getAnswersOfCurrentStudent(){
        return answerService.getAnswersOfCurrentStudent();
    }
}
