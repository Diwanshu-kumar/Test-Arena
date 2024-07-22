package com.example.springboot.testarena.submitCode.dto;



public record CodeSubmissionRequest(long userId,long problemId, String  code, String language) {}