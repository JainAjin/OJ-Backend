package com.OJ.OnlineJudge.Submission.Service;

import com.OJ.OnlineJudge.Submission.Entity.Submission;
import com.OJ.OnlineJudge.Submission.Model.JudgeResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class JudgeService {
    private static final Path APP_WORKSPACE_PATH = Paths.get(System.getProperty("java.io.tmpdir"), "judging-workspace");
    private static final String DOCKER_WORKSPACE_PATH = "/app";

    //Path Creation
    public Path prepareWorkspace(Submission submission) throws IOException {
        Path workspacePath = APP_WORKSPACE_PATH.resolve("sub-" + submission.getId());
        Files.createDirectories(workspacePath);
        Path sourceFilePath = workspacePath.resolve(getSourceFileName(submission.getLanguage().toString()));
        Files.writeString(sourceFilePath, submission.getSourceCode());
        log.info("Workspace created for submission {}: {}", submission.getId(), workspacePath);
        return workspacePath;
    }
    private String getSourceFileName(String language) {
        return "java".equalsIgnoreCase(language) ? "Main.java" : "main.cpp";
    }
    String image;
    //Code Compilation
    public JudgeResponse compile(Path workspacePath, String language) throws IOException, InterruptedException {
        String[] command;
        // This is the crucial part that links the server folder to the container folder.
        String volumeMount = workspacePath.toString() + ":" + DOCKER_WORKSPACE_PATH;
        log.info("Volume mount is {}", volumeMount);

        switch (language.toLowerCase()) {
            case "cpp":
                command = new String[]{ "docker", "run", "--rm", "--workdir", DOCKER_WORKSPACE_PATH, "-v",
                        volumeMount, "gcc:latest", "g++", "main.cpp", "-o", "main"};
                image = "ubuntu:latest";
                break;
            case "java":
                command = new String[]{ "docker", "run", "--rm", "--workdir", DOCKER_WORKSPACE_PATH, "-v",
                        volumeMount, "openjdk:17-jdk-slim", "javac", "Main.java"};
                image = "openjdk:17-jdk-slim";
                break;
            default:
                return new JudgeResponse("Error", "", "Unsupported language: " + language);
        }

        log.info("Executing compile command: {}", String.join(" ", command));
        return executeProcess(command, 15); // 15-second timeout for compilation
    }
    private JudgeResponse executeProcess(String[] command, long timeoutSeconds) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();
        try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream())))
        {
            String line;
            while ((line = outputReader.readLine()) != null) {
                output.append(line).append("\n");
            }
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
        }
        boolean finishedInTime = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
        if (!finishedInTime) {
            process.destroyForcibly();
            log.warn("Process timed out and was destroyed forcibly.");
            return new JudgeResponse("Falied", "", "Process timed out after " + timeoutSeconds + " seconds.");
        }

        int exitCode = process.exitValue();
        if (exitCode != 0) {
            log.error("Process exited with non-zero code {}: {}", exitCode, errorOutput);
            return new JudgeResponse("Failed", output.toString(), errorOutput.toString());
        }

        return new JudgeResponse("Success", output.toString(), errorOutput.toString());
    }

    //Code Execution
    public JudgeResponse execute(Path workspacePath, String language, String input, long timeLimitMs, long memoryLimitKb) throws IOException, InterruptedException {
        Files.writeString(workspacePath.resolve("input.txt"), input);

        String memoryLimit = memoryLimitKb + "m";
        String timeLimitSec = String.valueOf(timeLimitMs / 1000.0);
        String volumeMount = workspacePath.toString() + ":" + DOCKER_WORKSPACE_PATH;

        String executionCommand = getExecutionCommand(language);
        String shellCommand = String.format("timeout %s %s < input.txt > output.txt", timeLimitSec, executionCommand);

        String[] command = new String[]{
                "docker", "run", "--rm",
                "--workdir", DOCKER_WORKSPACE_PATH,
                "--memory=" + memoryLimit, "--memory-swap=" + memoryLimit,
                "-v", volumeMount,
                image,
                "sh", "-c", shellCommand
        };

        log.info("Executing run command: {}", String.join(" ", command));
        JudgeResponse result = executeProcess(command, (timeLimitMs / 1000) + 2);

        if (result.isSuccess()) {
            String output = Files.readString(workspacePath.resolve("output.txt"));
            return new JudgeResponse("Success", output, result.getMessage());
        } else {
            return result;
        }
    }
    private String getExecutionCommand(String language) {
        return "java".equalsIgnoreCase(language) ? "java Main" : "./main";
    }

    // Cleaning the temp Directory
    public void cleanupWorkspace(Path workspacePath) {
        try {
            Files.walk(workspacePath).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(java.io.File::delete);
            log.info("Workspace cleaned up: {}", workspacePath);
        } catch (IOException e) {
            log.error("Failed to cleanup workspace: {}", workspacePath, e);
        }
    }}
