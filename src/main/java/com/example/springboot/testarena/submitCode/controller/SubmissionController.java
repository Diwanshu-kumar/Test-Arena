package com.example.springboot.testarena.submitCode.controller;

import com.example.springboot.testarena.submitCode.dto.CodeSubmissionRequest;
import com.example.springboot.testarena.submitCode.dto.SubmissionStatus;
import com.example.springboot.testarena.submitCode.dto.SubmittedCode;
import com.example.springboot.testarena.submitCode.dto.mySubmission;
import com.example.springboot.testarena.submitCode.service.RunCodeService;
import com.example.springboot.testarena.submitCode.service.ServeUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user/")
public class SubmissionController {

    private final RunCodeService runCodeService;
    private final ServeUserService serveUserService;

    public SubmissionController(RunCodeService runCodeService, ServeUserService serveUserService) {
        this.runCodeService = runCodeService;
        this.serveUserService = serveUserService;
    }

    @PostMapping("submit")
    public SubmissionStatus submitCode(@RequestBody CodeSubmissionRequest codeSubmissionRequest){
        return runCodeService.runOnSystemTestCase(codeSubmissionRequest);
    }

    @PostMapping("run")
    public SubmissionStatus runCode(@RequestBody CodeSubmissionRequest codeSubmissionRequest){
        return runCodeService.runOnSampleTestCase(codeSubmissionRequest);
    }

    @GetMapping("submissions")
    public ResponseEntity<List<mySubmission>> getSubmissions(@RequestParam String username){
        return ResponseEntity.ok(serveUserService.getUserSubmission(username));
    }

    @GetMapping("submissions/code")
    public ResponseEntity<SubmittedCode> getCode(@RequestParam Long submissionId){
        return ResponseEntity.ok(serveUserService.getCode(submissionId));
    }
    @GetMapping("status{submissionId}")
    public SubmissionStatus getStatus(@PathVariable String submissionId){
        return new SubmissionStatus("","","");
    }
}
