package com.example.readerai.dto.statistics;

import com.example.readerai.dto.ParticipantDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingStatsByDay {
    private ParticipantDTO participant;
    private Map<LocalDate, DailyReadingStats> dailyStats;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyReadingStats {
        private float totalReadingTimeMinutes;
        private Integer totalPagesRead;
    }
}