package com.example.springboot.testarena.submitCode.service;

import com.example.springboot.testarena.problem.entity.Problem;
import com.example.springboot.testarena.problem.entity.SystemTestCase;
import com.example.springboot.testarena.problem.repository.ProblemRepository;
import com.example.springboot.testarena.problem.repository.SampleTestCaseRepository;
import com.example.springboot.testarena.problem.repository.SystemTestCaseRepository;
import com.example.springboot.testarena.submitCode.dto.CodeSubmissionRequest;
import com.example.springboot.testarena.submitCode.dto.SubmissionStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

@Service
public class RunCodeService {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ProblemRepository problemRepository;
    private final SystemTestCaseRepository systemTestCaseRepository;

    public RunCodeService(ProblemRepository problemRepository, SampleTestCaseRepository sampleTestCaseRepository, SystemTestCaseRepository systemTestCaseRepository) {
        this.problemRepository = problemRepository;
        this.systemTestCaseRepository = systemTestCaseRepository;
    }


    final private String  dirName = "codeInputOutput";

    // Get the absolute path of the current project directory
    final private Path currentProjectPath = Paths.get("").toAbsolutePath();

    // Define the full path for the new directory
    final private Path newDirPath = currentProjectPath.resolve(dirName);

    public SubmissionStatus runOnSystemTestCase(CodeSubmissionRequest codeSubmissionRequest) {

        List<SystemTestCase> systemTestCase =null;
        Problem problem = problemRepository.findById(codeSubmissionRequest.problemId()).orElse(null);

        if(problem != null) {
            systemTestCase = systemTestCaseRepository.findByProblem(problem);
        }
        StringBuilder result = new StringBuilder();
        int maximumExecutionTime = 0;
        if(systemTestCase != null) {
            for(SystemTestCase testCase : systemTestCase) {
                RunningResult runningResult = runOnSingleTestFile(codeSubmissionRequest,testCase.getInput(),testCase.getExpectedOutput());
                result.append(runningResult.verdict());
                maximumExecutionTime = Math.max(maximumExecutionTime, runningResult.executionTime());
            }
        }
        return new SubmissionStatus(result.toString(),"",String.valueOf(maximumExecutionTime));
    }

    public record RunningResult(String verdict,int executionTime){}



    public RunningResult runOnSingleTestFile(CodeSubmissionRequest codeSubmissionRequest, String input, String expectedOutput){


        createFilesForCodeInputAndOutput(codeSubmissionRequest, newDirPath,input,expectedOutput);


        String result;
        Future<String> future = executor.submit(this::executeCode);
        try {
            result = future.get(3, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            result = "Time Limit Exceeded \n";
            future.cancel(true);
        } catch (Exception e) {
            result = "Execution error: " + e.getMessage() + "\n";
        }
        Path output = Paths.get(newDirPath.toFile().getAbsolutePath() + "/output.txt");
        Path expectedOutputPath = Paths.get(newDirPath.toFile().getAbsolutePath() + "/expectedOutput.txt");
        boolean isEqual = compareFiles(output, expectedOutputPath);

        return new RunningResult((isEqual?"AC\n":"WA\n"),getExecutionTime());
    }

    private void createFilesForCodeInputAndOutput(CodeSubmissionRequest codeSubmissionRequest, Path newDirPath,String input,String expectedOutput) {
        try {
            deleteFilesInDirectory(newDirPath.toFile());
            Files.createDirectories(newDirPath);
            // Create the directory if it doesn't exist

            Path codeFilePath = newDirPath.resolve("Main.java");
            Files.createFile(codeFilePath);
            Files.writeString(codeFilePath, codeSubmissionRequest.code());

            Path inputFilePath = newDirPath.resolve("input.txt");
            Files.createFile(inputFilePath);
            Files.writeString(inputFilePath, input);

            Path outputFilePath = newDirPath.resolve("expectedOutput.txt");
            Files.createFile(outputFilePath);
            Files.writeString(outputFilePath, expectedOutput);

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private int getExecutionTime() {

        Path executionTimeFilePath = newDirPath.resolve("executionTime.txt");

        try {
            Scanner scanner = new Scanner(executionTimeFilePath);
            if(scanner.hasNextLine()) {
                return Integer.parseInt(scanner.nextLine().trim().split(" ")[2]);
            }
        }catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        return 3000;
    }

    private boolean compareFiles(Path output, Path expectedOutput) {
        boolean result;
        try {

            Scanner scOutput = new Scanner(output);
            Scanner scExpected = new Scanner(expectedOutput);

            while (scOutput.hasNextLine() && scExpected.hasNextLine()) {
                String line = scOutput.nextLine().trim();
                String lineExpected = scExpected.nextLine().trim();
                if (!line.equals(lineExpected)) {
                    return false;
                }

            }
            result = !scOutput.hasNextLine() && !scExpected.hasNextLine();
            scOutput.close();
            scExpected.close();
        }catch (Exception e) {
            return false;
        }
        return result;
    }

    private void deleteFilesInDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFilesInDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
    }
    private String  executeCode() {

        String currentDir = Paths.get("").toAbsolutePath().toString();
        String codeInputOutputPath = currentDir + "/codeInputOutput";
        String runScriptPath = currentDir + "/docker/java-runner/run.sh";

        // Construct the Docker command
        String[] command = {
                "docker", "run","--rm",
                "-v", codeInputOutputPath + ":/app",
                "-v", runScriptPath + ":/app/run.sh",
                "java-runner"
        };

        // Run the Docker command
        runDockerCommand(command);
        return null;
    }


    public static void runDockerCommand(String[] command) {
        try {
            // Use ProcessBuilder to run the command
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Capture the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
