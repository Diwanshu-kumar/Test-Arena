package com.example.springboot.testarena.problem.entity;

import jakarta.persistence.*;

@Entity
@Table
public class SampleTestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int Id;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    public Problem problem;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String input;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String expectedOutput;
    public SampleTestCase() {}

    public SampleTestCase(Problem problem, String input, String expectedOutput) {
        this.problem = problem;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }
}
