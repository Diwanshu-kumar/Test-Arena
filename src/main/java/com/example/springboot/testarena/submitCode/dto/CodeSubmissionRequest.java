package com.example.springboot.testarena.submitCode.dto;



public record CodeSubmissionRequest(String username,long problemId, String  code, String language) {}