package com.example.springboot.testarena.submitCode.controller;

import com.example.springboot.testarena.submitCode.dto.CodeSubmissionRequest;
import com.example.springboot.testarena.submitCode.dto.SubmissionStatus;
import com.example.springboot.testarena.submitCode.service.RunCodeService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user/")
public class SubmissionController {

    private final RunCodeService runCodeService;


    public SubmissionController(RunCodeService runCodeService) {
        this.runCodeService = runCodeService;
    }

    @PostMapping("submit")
    public SubmissionStatus submitCode(@RequestBody CodeSubmissionRequest codeSubmissionRequest){
        return runCodeService.runOnSystemTestCase(codeSubmissionRequest);
    }

    @PostMapping("run")
    public SubmissionStatus runCode(@RequestBody CodeSubmissionRequest codeSubmissionRequest){
        return runCodeService.runOnSampleTestCase(codeSubmissionRequest);
    }

    @GetMapping("status{submissionId}")
    public SubmissionStatus getStatus(@PathVariable String submissionId){
        return new SubmissionStatus("","","");
    }
}
