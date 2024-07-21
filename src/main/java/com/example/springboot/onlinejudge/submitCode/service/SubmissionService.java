package com.example.springboot.onlinejudge.submitCode.service;

import com.example.springboot.onlinejudge.submitCode.dto.CodeSubmissionRequest;
import com.example.springboot.onlinejudge.submitCode.dto.SubmissionStatus;
import com.example.springboot.onlinejudge.submitCode.entity.Submission;
import com.example.springboot.onlinejudge.submitCode.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        submissionRepository.save(submission);

        String result= "testing";
        Future<String> future = executor.submit(() -> executeCode(submission));
        try {
            result = future.get(3, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            result = "Time Limit Exceeded";
            future.cancel(true);
        } catch (Exception e) {
            result = "Execution error: " + e.getMessage();
        }

        submission.setStatus("completed");
        submission.setResult(result);
        submissionRepository.save(submission);

        return new SubmissionStatus(submission.getSubmissionId(), "completed", result);
    }

    private String executeCode(Submission submission) throws IOException, InterruptedException {
        String language = submission.getLanguage();
        String code = submission.getCode();

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

        String dockerCommand = String.format("docker run --rm -i %s", dockerImage);

        Process process = Runtime.getRuntime().exec(dockerCommand);
        process.getOutputStream().write(code.getBytes());
        process.getOutputStream().close();

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            return "Error executing code";
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        return output.toString();
    }
}
