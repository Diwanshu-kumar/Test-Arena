package com.example.springboot.onlinejudge.submitCode.dto;



public record CodeSubmissionRequest(long userId,long problemId, String  code, String language) {}