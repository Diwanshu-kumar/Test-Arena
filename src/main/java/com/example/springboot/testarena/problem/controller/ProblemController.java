package com.example.springboot.testarena.problem.controller;

import com.example.springboot.testarena.problem.dto.ProblemSubmissionRequest;
import com.example.springboot.testarena.problem.entity.Problem;
import com.example.springboot.testarena.problem.service.ProblemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/problem/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @PostMapping("user/newProblem")
    public ResponseEntity<String > addProblem(@RequestBody ProblemSubmissionRequest problemSubmissionRequest) {
        problemService.addNewProblem(problemSubmissionRequest);
        return ResponseEntity.ok("Problem added");
    }

    @GetMapping("user/problems")
    public ResponseEntity<List<Problem>> getAllProblems() {
        return ResponseEntity.ok(problemService.getAllProblem("approved"));
    }

    @GetMapping("details")
    public ResponseEntity<Problem> getProblemDescription(@RequestParam long problemId) {
        Problem problem = problemService.getProblemById(problemId);
        if (problem == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(problem);
    }

    @GetMapping("admin/problems")
    public ResponseEntity<List<Problem>> getAllAdminProblems(@RequestParam String status) {
        return ResponseEntity.ok(problemService.getAllProblem(status));
    }

    @DeleteMapping("admin/delete")
    public ResponseEntity<String> deleteProblem(@RequestParam Long problemId) {
        return ResponseEntity.ok(problemService.deleteProblem(problemId));
    }

    @PutMapping("admin/update")
    public ResponseEntity<String> updateProblem(
            @RequestParam  long problemId, @RequestParam String status) {
        return ResponseEntity.ok(problemService.updateProblem(problemId,status));
    }
}
