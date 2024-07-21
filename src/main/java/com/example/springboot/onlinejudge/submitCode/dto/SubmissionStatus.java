package com.example.springboot.onlinejudge.submitCode.dto;


import java.util.Date;

public record SubmissionStatus(long submissionId, String status , String result) {}