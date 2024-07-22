package com.example.springboot.onlinejudge.problem.dto;

import java.util.List;



public class ProblemSubmissionRequest {

    private String title;

    private String description;

    private String author;

    private String problemConstraint;

    private String explanation;

    private List<TestCase> sampleTestCase;

    private List<TestCase> systemTestCase;


    public ProblemSubmissionRequest(String title, String description, String author, String problemConstraint, String explanation, List<TestCase> sampleTestCase, List<TestCase> systemTestCase) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.problemConstraint = problemConstraint;
        this.explanation = explanation;
        this.sampleTestCase = sampleTestCase;
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

    public List<TestCase> getSampleTestCase() {
        return sampleTestCase;
    }

    public void setSampleTestCase(List<TestCase> sampleTestCase) {
        this.sampleTestCase = sampleTestCase;
    }

    public List<TestCase> getSystemTestCase() {
        return systemTestCase;
    }

    public void setSystemTestCase(List<TestCase> systemTestCase) {
        this.systemTestCase = systemTestCase;
    }
}
