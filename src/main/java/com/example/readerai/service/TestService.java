package com.example.readerai.service;

import com.example.readerai.config.RabbitMQConfig;
import com.example.readerai.converter.QuestionConverter;
import com.example.readerai.converter.TestConverter;
import com.example.readerai.dto.CompleteStatus;
import com.example.readerai.dto.ProgressDTO;
import com.example.readerai.dto.QuestionDTO;
import com.example.readerai.dto.TestDTO;
import com.example.readerai.dto.rabbit.TestGenerationRequest;
import com.example.readerai.dto.rabbit.TestGenerationResponse;
import com.example.readerai.entity.Answer;
import com.example.readerai.entity.Question;
import com.example.readerai.entity.Test;
import com.example.readerai.exception.NotFoundException;
import com.example.readerai.repository.AnswerRepository;
import com.example.readerai.repository.QuestionRepository;
import com.example.readerai.repository.TestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestService {

    private final RabbitTemplate rabbitTemplate;

    private final TestRepository testRepository;

    private final TestConverter testConverter;

    private final ProgressService progressService;

    private final QuestionRepository questionRepository;

    private final QuestionConverter questionConverter;

    private final AnswerRepository answerRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TestDTO createTest(TestDTO testDTO) {
        try {
            Long bookId = testDTO.getProgress().getBook().getId();
            Long participantId = testDTO.getProgress().getParticipant().getId();

            ProgressDTO progressDTO = progressService.getProgressByBookIdAndParticipantId(bookId, participantId);

            testDTO.setProgress(progressDTO);
            testDTO.setCompleted(CompleteStatus.NOT_STARTED);
            Test savedTest = testRepository.save(testConverter.fromDTO(testDTO));

            requestTestGeneration(
                    progressDTO.getBook().getFileName(),
                    savedTest.getId(),
                    testDTO.getStartPage(),
                    testDTO.getEndPage(),
                    testDTO.getQuestionsAmount()
            );

            return testConverter.toDTO(savedTest);
        } catch (Exception e) {
            log.error("Failed to create test: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create test", e);
        }
    }

    public void requestTestGeneration(String fileName, Long testId, int startPage, int endPage, int questionCount) {
        try {
            TestGenerationRequest request = new TestGenerationRequest();
            request.setFileName(fileName);
            request.setTestId(testId);
            request.setStartPage(startPage);
            request.setEndPage(endPage);
            request.setQuestionCount(questionCount);

            log.info("Sending request for test generation for test: {}", testId);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY,
                    objectMapper.writeValueAsString(request)
            );
        } catch (Exception e) {
            log.error("Failed sending request for test generation for test: {}", e.getMessage(), e);
            throw new RuntimeException("Failed sending request for test generation", e);
        }
    }

    @Transactional
    public void saveTestResult(TestGenerationResponse response) {
        try {
            log.info("Saving test generation results for test: {}", response.getTestId());
            Test test = testRepository.findById(response.getTestId())
                    .orElseThrow(() -> {
                        log.error("Test with id: {} not found", response.getTestId());
                        return new NotFoundException("Test with id: " + response.getTestId() + " not found");
                    });

            List<Question> questions = new ArrayList<>();
            for (QuestionDTO questionDTO : response.getQuestions()) {
                Question question = questionConverter.fromDTO(questionDTO);
                question.setTest(test);

                List<Answer> answers = new ArrayList<>(question.getAnswers());
                question.setAnswers(null);
                questionRepository.save(question);


                List<Answer> savedAnswers = new ArrayList<>();
                for (Answer answer : answers) {
                    answer.setQuestion(question);
                    answerRepository.save(answer);
                    savedAnswers.add(answer);
                }

                question.setAnswers(savedAnswers);
                questions.add(question);
            }

            test.setQuestions(questions);

            testRepository.save(test);

            log.info("Test generation results saved successfully for test: {}", response.getTestId());
        } catch (Exception e) {
            log.error("Failed to save test generation results for test: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save test generation results", e);
        }
    }
}