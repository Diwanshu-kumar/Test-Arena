package com.example.springboot.testarena.submitCode.service;

import com.example.springboot.testarena.problem.entity.Problem;
import com.example.springboot.testarena.problem.entity.SystemTestCase;
import com.example.springboot.testarena.problem.repository.ProblemRepository;
import com.example.springboot.testarena.problem.repository.SystemTestCaseRepository;
import com.example.springboot.testarena.submitCode.dto.CodeSubmissionRequest;
import com.example.springboot.testarena.submitCode.dto.CodeSubmissionWithCustomTestcase;
import com.example.springboot.testarena.submitCode.dto.SubmissionStatus;
import com.example.springboot.testarena.submitCode.entity.Submission;
import com.example.springboot.testarena.submitCode.repository.SubmissionRepository;
import com.example.springboot.testarena.user.entity.User;
import com.example.springboot.testarena.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class RunCodeService {


    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    public record RunningResult(String verdict,int runningType, int executionTime){}

    private final Lock lock = new ReentrantLock();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ProblemRepository problemRepository;
    private final SystemTestCaseRepository systemTestCaseRepository;

    public RunCodeService(ProblemRepository problemRepository, SystemTestCaseRepository systemTestCaseRepository, SubmissionRepository submissionRepository, UserRepository userRepository) {
        this.problemRepository = problemRepository;
        this.systemTestCaseRepository = systemTestCaseRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
    }


    final private String  dirName = "codeInputOutput";

    // Get the absolute path of the current project directory
    final private Path currentProjectPath = Paths.get("").toAbsolutePath();

    // Define the full path for the new directory
    final private Path newDirPath = currentProjectPath.resolve(dirName);

    public SubmissionStatus runOnSystemTestCase(CodeSubmissionRequest codeSubmissionRequest) {
        User user = userRepository.findByUsername(codeSubmissionRequest.username());
        Submission submission = new Submission();
        submission.setUserId(user.getId());
        submission.setProblemId(codeSubmissionRequest.problemId());
        submission.setCode(codeSubmissionRequest.code());
        submission.setLanguage(codeSubmissionRequest.language());
        submission.setStatus("processing");
        submission.setSubmissionTime(LocalDateTime.now());

        List<SystemTestCase> systemTestCase =null;
        Problem problem = problemRepository.findById(codeSubmissionRequest.problemId()).orElse(null);

        if(problem != null) {
            systemTestCase = systemTestCaseRepository.findByProblem(problem);
        }
        StringBuilder result = new StringBuilder();
        int maximumExecutionTime = 0;
        int errorType =0;

        lock.lock();
        if (systemTestCase != null) {
            for (SystemTestCase testCase : systemTestCase) {
                RunningResult runningResult = runOnSingleTestFile(codeSubmissionRequest.code(),codeSubmissionRequest.language(), testCase.getInput(), testCase.getExpectedOutput());
                result.append(runningResult.verdict());
                errorType = Math.max(runningResult.runningType(), errorType);
                maximumExecutionTime = Math.max(maximumExecutionTime, runningResult.executionTime());
            }
        }
        lock.unlock();

        submission.setResult(result.toString());
        submission.setStatus((errorType==0)?"AC":(errorType==1)?"WA":"RE");
        submission.setExecutionTime(maximumExecutionTime+"");
        submissionRepository.save(submission);
        return new SubmissionStatus(submission.getStatus(),result.toString(),String.valueOf(maximumExecutionTime));
    }

    public SubmissionStatus runOnSampleTestCase(CodeSubmissionRequest codeSubmissionRequest) {
        Problem problem = problemRepository.findById(codeSubmissionRequest.problemId()).orElse(null);
        String sampleInput, sampleExpectedOutput;
        if(problem != null) {
            sampleInput = problem.getSampleInput();
            sampleExpectedOutput = problem.getSampleOutput();
            return getSubmissionStatus(codeSubmissionRequest.code(),codeSubmissionRequest.language(), sampleInput, sampleExpectedOutput);
        }else{
            return new SubmissionStatus("problem not exist","","0");
        }

    }

    public SubmissionStatus runOnCustomTestCase(CodeSubmissionWithCustomTestcase codeSubmissionWithCustomTestcase) {
        if(codeSubmissionWithCustomTestcase.customInput() != null) {
            return getSubmissionStatus(codeSubmissionWithCustomTestcase.code(),codeSubmissionWithCustomTestcase.language(),
                    codeSubmissionWithCustomTestcase.customInput(), "");
        }else{
            return new SubmissionStatus("custom testcase is empty","","0");
        }
    }

    private SubmissionStatus getSubmissionStatus(String code, String language , String sampleInput, String sampleExpectedOutput) {
        lock.lock();

        RunningResult  runningResult = runOnSingleTestFile(code,language, sampleInput, sampleExpectedOutput);
        SubmissionStatus submissionStatus;
        if(runningResult.runningType()<=1){
            Path output = Paths.get(newDirPath.toFile().getAbsolutePath() + "/output.txt");
            String sampleOutput = readFile(output);
            submissionStatus = new SubmissionStatus("",sampleOutput,String.valueOf(runningResult.executionTime()));
        }else{
            Path error = Paths.get(newDirPath.toFile().getAbsolutePath() + "/error.txt");
            String errorOutput = readFile(error);
            submissionStatus =  new SubmissionStatus("",errorOutput,String.valueOf(runningResult.executionTime()));
        }

        lock.unlock();
        return submissionStatus;
    }

    private String readFile(Path path) {
        try {
            Scanner scanner = new Scanner(path.toFile());
            StringBuilder result = new StringBuilder();
            while(scanner.hasNextLine()) {
                result.append(scanner.nextLine()).append("\n");
            }
            return result.toString();
        }catch (FileNotFoundException ignored) {

        }
        return "file not found";
    }

    private RunningResult runOnSingleTestFile(String code, String language , String input, String expectedOutput){


        createFilesForCodeInputAndOutput(code,language, newDirPath,input);


        String result;
        Future<String> future = executor.submit(()->executeCode(language.toLowerCase()));
        try {
            result = future.get(getTimeLimit(language), TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            result = "Time Limit Exceeded \n";
            future.cancel(true);
        } catch (Exception e) {
            result = "Execution error: " + e.getMessage() + "\n";
        }


        if(!result.equalsIgnoreCase("compilation successful")){
            return new RunningResult(result+"\n",2,3000);
        }


        Path output = Paths.get(newDirPath.toFile().getAbsolutePath() + "/output.txt");
        boolean isEqual = compareFiles(output, expectedOutput);

        return new RunningResult((isEqual?"AC\n":"WA\n"),(isEqual?0:1),getExecutionTime());
    }

    private long getTimeLimit(String language) {
        return switch (language){
            case "java" -> 3;
            case "python" -> 10;
            case "cpp","c++"->2;
            default -> 1;
        };
    }

    private void createFilesForCodeInputAndOutput(String code,String language , Path newDirPath,String input) {
        try {
            deleteFilesInDirectory(newDirPath.toFile());
            Files.createDirectories(newDirPath);
            // Create the directory if it doesn't exist
            String languageExtension = getLanguageExtension(language);

            Path codeFilePath = newDirPath.resolve("Main."+languageExtension);
            Files.createFile(codeFilePath);
            Files.writeString(codeFilePath, code);

            Path inputFilePath = newDirPath.resolve("input.txt");
            Files.createFile(inputFilePath);
            Files.writeString(inputFilePath, input);

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private String getLanguageExtension(String language) {
        return switch (language.toLowerCase()) {
            case "java" -> "java";
            case "python" -> "py";
            case "c++", "cpp" -> "cpp";
            default -> "txt";
        };
    }

    private int getExecutionTime() {

        Path executionTimeFilePath = newDirPath.resolve("executionTime.txt");

        try {
            Scanner scanner = new Scanner(executionTimeFilePath);
            if(scanner.hasNextLine()) {
                return Integer.parseInt(scanner.nextLine().trim());
            }
        }catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        return 3000;
    }

    private boolean compareFiles(Path output, String  expectedOutput) {
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
    private String  executeCode(String language) {

        String currentDir = Paths.get("").toAbsolutePath().toString();
        String codeInputOutputPath = currentDir + "/codeInputOutput";
        String runScriptPath = currentDir + "/docker/"+language+"-runner/run.sh";

        // Construct the Docker command
        String[] command = {
                "docker", "run","--rm",
                "-v", codeInputOutputPath + ":/app",
                "-v", runScriptPath + ":/app/run.sh",
                language+"-runner"
        };

        // Run the Docker command
        return runDockerCommand(command);
    }


    public static String  runDockerCommand(String[] command) {
        try {
            // Use ProcessBuilder to run the command
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Capture the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder processOutput = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                processOutput.append(line);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
//            System.out.println("Exited with code: " + exitCode);
            process.destroy();
            return processOutput.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "server error";
    }
}
