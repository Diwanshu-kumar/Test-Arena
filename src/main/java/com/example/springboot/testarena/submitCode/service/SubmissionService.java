package com.example.springboot.testarena.submitCode.service;

import com.example.springboot.testarena.submitCode.dto.CodeSubmissionRequest;
import com.example.springboot.testarena.submitCode.dto.SubmissionStatus;
import com.example.springboot.testarena.submitCode.entity.Submission;
import com.example.springboot.testarena.submitCode.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.*;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SubmissionStatus processSubmission(CodeSubmissionRequest request) {
        Submission submission = new Submission();
        submission.setUserId(request.userId());
        submission.setProblemId(request.problemId());
        submission.setCode(request.code());
        submission.setLanguage(request.language());
        submission.setStatus("processing");
        submission.setSubmissionTime(LocalDateTime.now());
        submissionRepository.save(submission);


        String result;
        Future<String> future = executor.submit(() -> executeCode(submission));
        try {
            result = future.get(3, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            result = "Time Limit Exceeded \n";
            future.cancel(true);
        } catch (Exception e) {
            result = "Execution error: " + e.getMessage() + "\n";
        }
        String[] listResult = result.split("\\r?\\n");

        Arrays.stream(listResult).forEach(System.out::println);

        submission.setExecutionTime(listResult[listResult.length - 1]);
        submission.setStatus(listResult[0]);
        submission.setResult(submission.getStatus().equalsIgnoreCase("Compilation successful") ? result : "error");
        submissionRepository.save(submission);

        return new SubmissionStatus("completed", result,"0");
    }

    private String executeCode(Submission submission) throws IOException, InterruptedException {
        String language = submission.getLanguage();
        String code = submission.getCode();

        String dockerCommand = getDockerCommand(language);
        Process process = Runtime.getRuntime().exec(dockerCommand);
        process.getOutputStream().write(code.getBytes());
        process.getOutputStream().close();

        int exitCode = process.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        return output.toString();
    }

    private static String getDockerCommand(String language) {
        String dockerImage;
        if ("python".equalsIgnoreCase(language)) {
            dockerImage = "python-runner";
        } else if ("java".equalsIgnoreCase(language)) {
            dockerImage = "java-runner";
        } else if ("cpp".equalsIgnoreCase(language)) {
            dockerImage = "cpp-runner";
        } else {
            throw new IllegalArgumentException("Unsupported language: " + language);
        }

        return String.format("docker run --rm -i %s", dockerImage);
    }
}
