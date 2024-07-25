package com.example.springboot.testarena.submitCode.service;

import com.example.springboot.testarena.submitCode.dto.CodeSubmissionRequest;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.*;

@Service
public class RunCodeService {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public String runCode(CodeSubmissionRequest codeSubmissionRequest){

        createNecessaryFiles(codeSubmissionRequest);
        return null;
    }

    private boolean createNecessaryFiles(CodeSubmissionRequest codeSubmissionRequest){

        String dirName = "codeInputOutput";

        // Get the absolute path of the current project directory
        Path currentProjectPath = Paths.get("").toAbsolutePath();

        // Define the full path for the new directory
        Path newDirPath = currentProjectPath.resolve(dirName);

        createFilesForCodeInputAndOutput(codeSubmissionRequest, newDirPath);

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
        System.out.println("hello");
        System.out.println(newDirPath.toFile().listFiles().length);
        // Create the files in the specified directory
//            System.out.println(Arrays.toString(newDirPath.toFile().listFiles()));
        Path output = Paths.get(newDirPath.toFile().getAbsolutePath() + "/output.txt");
        Path expectedOutput = Paths.get(newDirPath.toFile().getAbsolutePath() + "/expectedOutput.txt");
        boolean isEqual = compareFiles(output,expectedOutput);
        System.out.println(isEqual + "\n");

        return false;
    }

    private void createFilesForCodeInputAndOutput(CodeSubmissionRequest codeSubmissionRequest, Path newDirPath) {
        try {
            deleteDirectory(newDirPath.toFile());
            Files.createDirectories(newDirPath);
            System.out.println("Directory created at: " + newDirPath);
            // Create the directory if it doesn't exist

            Path codeFilePath = newDirPath.resolve("Main.java");
            Files.createFile(codeFilePath);
            Files.writeString(codeFilePath, codeSubmissionRequest.code());

            Path inputFilePath = newDirPath.resolve("input.txt");
            Files.createFile(inputFilePath);
            Files.writeString(inputFilePath, "4 2 3 1 4 3 2 3 ");

            Path outputFilePath = newDirPath.resolve("expectedOutput.txt");
            Files.createFile(outputFilePath);
            Files.writeString(outputFilePath, "1 2 3 4 4 3 2");

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
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

    private void deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        path.delete();
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
