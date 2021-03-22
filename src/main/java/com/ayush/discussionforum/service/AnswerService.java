package com.ayush.discussionforum.service;

import com.ayush.discussionforum.dto.AnswerRequest;
import com.ayush.discussionforum.dto.AnswerResponse;
import com.ayush.discussionforum.model.Answer;
import com.ayush.discussionforum.model.Question;
import com.ayush.discussionforum.model.Student;
import com.ayush.discussionforum.repository.AnswerRepository;
import com.ayush.discussionforum.repository.QuestionRepository;
import com.ayush.discussionforum.repository.StudentRepository;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository  questionRepository;
    private final StudentRepository studentRepository;
    private final AuthService authService;


    @Transactional
    public void createAnswer(AnswerRequest answerRequest){

        Question question = questionRepository.findById(answerRequest.getQuestionId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(404), "Question not found"));

        Student currentStudent = authService.getCurrentStudent();

        if(question.getStudent().getUsername().equals(currentStudent.getUsername())){
            throw new ResponseStatusException(HttpStatus.valueOf(403), "You  cannot answer");
        }

        Optional<Answer> answerOptional = answerRepository
                .queryAnswerByQuestionAndStudent(question, currentStudent);

        if(answerOptional.isPresent()) throw new ResponseStatusException(HttpStatus.valueOf(403),
                "You have already answered");


        Answer answer = mapRequestToAnswer(answerRequest);
        answerRepository.save(answer);

        question.getAnswers().add(answer);
        currentStudent.getAnswers().add(answer);
    }

    public void updateAnswer(Long studentId, Long answerId){
        //
    }

    @Transactional(readOnly = true)
    public AnswerResponse getById(Long id){
        Answer answer = answerRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found"));
        return mapAnswerToResponse(answer);
    }

    @Transactional(readOnly = true)
    public List<AnswerResponse> getByQuestion(Long questionId){
        Question question = questionRepository.findById(questionId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        return question.getAnswers()
                .stream()
                .map(this::mapAnswerToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AnswerResponse> getByStudent(String publicStudentId){
        Student student = studentRepository.findByPublicStudentId(publicStudentId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        return student.getAnswers().stream()
                .map(this::mapAnswerToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AnswerResponse> getAnswersOfCurrentStudent(){
        Student currentStudent = authService.getCurrentStudent();
        List<Answer> answerList = answerRepository.findByStudent(currentStudent);
        return answerList.stream()
                .map(this::mapAnswerToResponse)
                .collect(Collectors.toList());
    }

    private Answer mapRequestToAnswer(AnswerRequest answerRequest){

        Question question = questionRepository.findById(answerRequest.getQuestionId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(404), "Question not found"));

        return Answer.builder()
                .text(answerRequest.getAnswer())
                .createdDate(Instant.now())
                .student(authService.getCurrentStudent())
                .question(question)
                .build();
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
}
