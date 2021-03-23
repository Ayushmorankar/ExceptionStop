package com.ayush.discussionforum.service;

import com.ayush.discussionforum.dto.QuestionRequest;
import com.ayush.discussionforum.dto.QuestionResponse;
import com.ayush.discussionforum.model.Answer;
import com.ayush.discussionforum.model.Question;
import com.ayush.discussionforum.model.Thread;
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
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final StudentRepository studentRepository;
    private final AuthService authService;

    @Transactional
    public void createQuestion(QuestionRequest questionRequest){
        Student student = authService.getCurrentStudent();
        Question question = mapRequestToQuestion(questionRequest);
        questionRepository.save(question);
        student.getQuestions().add(question);
    }

    @Transactional(readOnly = true)
    public QuestionResponse getById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(404)));
        return mapQuestionToResponse(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getByThread(Thread thread) {

        List<Question> questionsList = questionRepository.findByThread(thread);
        return questionsList.stream()
                .map(this::mapQuestionToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestionsOfCurrentStudent() {

        Student currentStudent = authService.getCurrentStudent();

        return questionRepository.findByStudent(currentStudent)
                .stream()
                .map(this::mapQuestionToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getByStudent(String publicStudentId) {

        Student student = studentRepository.findByPublicStudentId(publicStudentId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(404), "Student not found"));
        return student.getQuestions().stream()
                .map(this::mapQuestionToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAnswered(Long questionId, Long answerId) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(404), "Question not found"));
        Student currentStudent = authService.getCurrentStudent();

        if(question.getStudent().getUsername().equals(currentStudent.getUsername()) &&
                question.getAnsweredBy() == null){
            Answer answer = answerRepository.findById(answerId)
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.valueOf(404), "Answer not found"));
            question.setAnsweredBy(answer.getStudent().getUsername());
        }
        else throw new ResponseStatusException(HttpStatus.valueOf(403));
    }

    private Question mapRequestToQuestion(QuestionRequest questionRequest){
        return Question.builder()
                .text(questionRequest.getText())
                .thread(questionRequest.getThread())
                .createdDate(Instant.now())
                .student(authService.getCurrentStudent())
                .build();
    }

    private QuestionResponse mapQuestionToResponse(Question question){
        return QuestionResponse.builder()
                .answeredBy(question.getAnsweredBy())
                .askedBy(question.getStudent().getUsername())
                .numberOfAnswers(question.getAnswers().size())
                .text(question.getText())
                .id(question.getId())
                .duration(getDuration(question))
                .publicStudentId(question.getStudent().getPublicStudentId())
                .build();
    }

    private String getDuration(Question question){
        return TimeAgo.using(question.getCreatedDate().toEpochMilli());
    }
}
