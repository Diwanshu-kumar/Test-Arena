package com.example.springboot.onlinejudge.submitCode.controller;

import com.example.springboot.onlinejudge.submitCode.dto.CodeSubmissionRequest;
import com.example.springboot.onlinejudge.submitCode.dto.SubmissionStatus;
import com.example.springboot.onlinejudge.submitCode.repository.SubmissionRepository;
import com.example.springboot.onlinejudge.submitCode.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/")
public class SubmissionController {

    @Autowired
    SubmissionService submissionService;
    @Autowired
    SubmissionRepository submissionRepository;

    @PostMapping("submit")
    public SubmissionStatus submitCode(@RequestBody CodeSubmissionRequest codeSubmissionRequest){
        System.out.println(codeSubmissionRequest);
        var result = submissionService.processSubmission(codeSubmissionRequest);
        System.out.println(result);
        return result;
    }

    @PostMapping("run")
    public SubmissionStatus runCode(@RequestBody CodeSubmissionRequest codeSubmissionRequest){
        System.out.println("run : "+ codeSubmissionRequest);
        return new SubmissionStatus(1,"","");
    }

    @GetMapping("status{submissionId}")
    public SubmissionStatus getStatus(@PathVariable String submissionId){
        return new SubmissionStatus(1,"","");
    }
}
