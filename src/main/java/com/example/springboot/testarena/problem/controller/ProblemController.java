package com.example.springboot.testarena.problem.controller;

import com.example.springboot.testarena.problem.dto.ProblemSubmissionRequest;
import com.example.springboot.testarena.problem.service.ProblemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/problem/")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @PostMapping("newProblem")
    public ResponseEntity<String > addProblem(@RequestBody ProblemSubmissionRequest problemSubmissionRequest) {
        problemService.addNewProblem(problemSubmissionRequest);
        return ResponseEntity.ok("Problem added");
    }

}
