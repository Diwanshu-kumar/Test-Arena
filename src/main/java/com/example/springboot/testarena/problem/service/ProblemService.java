package com.example.springboot.testarena.problem.service;


import com.example.springboot.testarena.problem.dto.ProblemSubmissionRequest;
import com.example.springboot.testarena.problem.dto.TestCase;
import com.example.springboot.testarena.problem.entity.Problem;
import com.example.springboot.testarena.problem.entity.SampleTestCase;
import com.example.springboot.testarena.problem.entity.SystemTestCase;
import com.example.springboot.testarena.problem.repository.ProblemRepository;
import com.example.springboot.testarena.problem.repository.SampleTestCaseRepository;
import com.example.springboot.testarena.problem.repository.SystemTestCaseRepository;
import org.springframework.stereotype.Service;

@Service
public class ProblemService {


    private final ProblemRepository problemRepository;
    private final SampleTestCaseRepository sampleTestCaseRepository;
    private final SystemTestCaseRepository systemTestCaseRepository;

    public ProblemService(ProblemRepository problemRepository, SampleTestCaseRepository sampleTestCaseRepository, SystemTestCaseRepository systemTestCaseRepository) {
        this.problemRepository = problemRepository;
        this.sampleTestCaseRepository = sampleTestCaseRepository;
        this.systemTestCaseRepository = systemTestCaseRepository;
    }

    public void addNewProblem(ProblemSubmissionRequest problemSubmissionRequest) {
        Problem problem = addProblem(problemSubmissionRequest);

        addTestCase(problemSubmissionRequest, problem.getProblemId());

    }

    private void addTestCase(ProblemSubmissionRequest problemSubmissionRequest, long problemId) {
        Problem problem = problemRepository.findById(problemId).orElse(null);
        if (problem == null) {return;}
        for(TestCase testCase : problemSubmissionRequest.getSampleTestCase()){
            SampleTestCase sampleTestCase = new SampleTestCase(problem, testCase.input(), testCase.expectedOutput());
            sampleTestCaseRepository.save(sampleTestCase);
        }

        for(TestCase testCase : problemSubmissionRequest.getSystemTestCase()){
            SystemTestCase systemTestCase = new SystemTestCase(problem, testCase.input(), testCase.expectedOutput());
            systemTestCaseRepository.save(systemTestCase);
        }
    }

    private Problem addProblem(ProblemSubmissionRequest problemSubmissionRequest) {
        Problem problem = new Problem();
        problem.setTitle(problemSubmissionRequest.getTitle());
        problem.setDescription(problemSubmissionRequest.getDescription());
        problem.setAuthor(problemSubmissionRequest.getAuthor());
        problem.setProblemConstraint(problemSubmissionRequest.getProblemConstraint());
        problem.setExplanation(problemSubmissionRequest.getExplanation());
        problem.setStatus("pending");

        problemRepository.save(problem);
        return problem;
    }
}
