package com.example.readerai.controller;

import com.example.readerai.dto.statistics.GradeByParticipant;
import com.example.readerai.dto.statistics.ReadingStatsByDay;
import com.example.readerai.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/participants/avgGrade")
    public ResponseEntity<List<GradeByParticipant>> getParticipantsGrade() {
        return ResponseEntity.ok(statisticsService.getParticipantsGrade());
    }

    @GetMapping("/participants/daily")
    public ResponseEntity<List<ReadingStatsByDay>> getDailyReadingStatistics() {
        return ResponseEntity.ok(statisticsService.getReadingStatsByDay());
    }
}
