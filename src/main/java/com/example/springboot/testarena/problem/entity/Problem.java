package com.example.springboot.testarena.problem.entity;

import jakarta.persistence.*;

@Entity
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long problemId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String description;

    private String author;

    @Column(columnDefinition = "TEXT")
    private String problemConstraint;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String explanation;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String sampleInput;

    @Column(columnDefinition = "TEXT")
    private String sampleOutput;

    public long getProblemId() {
        return problemId;
    }

    public void setProblemId(long problemId) {
        this.problemId = problemId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
