package com.example.springboot.testarena.submitCode.dto;

import java.time.LocalDateTime;

public record mySubmission(long submissionId, long problemId, String  executionTime,
                           String language, String status, LocalDateTime submissionTime) {
}
