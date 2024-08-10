package com.example.springboot.testarena.problem.dto;

import java.util.List;



public class ProblemSubmissionRequest {

    private String title;

    private String difficulty;

    private String description;

    private String author;

    private String problemConstraint;

    private String explanation;

    private String sampleInput;
    private String sampleOutput;

    private List<TestCase> systemTestCase;


    public ProblemSubmissionRequest(String title, String difficulty, String description, String author, String problemConstraint, String explanation, String sampleInput, String sampleOutput, List<TestCase> systemTestCase) {
        this.title = title;
        this.difficulty = difficulty;
        this.description = description;
        this.author = author;
        this.problemConstraint = problemConstraint;
        this.explanation = explanation;
        this.sampleInput = sampleInput;
        this.sampleOutput = sampleOutput;
        this.systemTestCase = systemTestCase;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getProblemConstraint() {
        return problemConstraint;
    }

    public void setProblemConstraint(String problemConstraint) {
        this.problemConstraint = problemConstraint;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public List<TestCase> getSystemTestCase() {
        return systemTestCase;
    }

    public void setSystemTestCase(List<TestCase> systemTestCase) {
        this.systemTestCase = systemTestCase;
    }

    public String getSampleInput() {
        return sampleInput;
    }

    public void setSampleInput(String sampleInput) {
        this.sampleInput = sampleInput;
    }

    public String getSampleOutput() {
        return sampleOutput;
    }

    public void setSampleOutput(String sampleOutput) {
        this.sampleOutput = sampleOutput;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
