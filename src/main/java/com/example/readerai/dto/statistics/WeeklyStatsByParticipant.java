package com.example.readerai.dto.statistics;


import com.example.readerai.dto.ParticipantDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyStatsByParticipant {
    private ParticipantDTO participant;
    private Map<String, WeeklyReadingStats> weeklyStats;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklyReadingStats {
        private float totalReadingTimeMinutes;
        private int totalPagesRead;
        private float totalRatingSum;
        private int ratingCount;

        public float getAverageRating() {
            return ratingCount > 0 ? totalRatingSum / ratingCount : 0f;
        }
    }
}
