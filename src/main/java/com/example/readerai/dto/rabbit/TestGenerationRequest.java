package com.example.readerai.dto.rabbit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestGenerationRequest {

    private String fileName;

    private Long testId;

    private int startPage;

    private int endPage;

    private int questionCount;
}
