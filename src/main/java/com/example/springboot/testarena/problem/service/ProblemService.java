package com.example.springboot.testarena.problem.service;


import com.example.springboot.testarena.problem.dto.ProblemSubmissionRequest;
import com.example.springboot.testarena.problem.dto.TestCase;
import com.example.springboot.testarena.problem.entity.Problem;
import com.example.springboot.testarena.problem.entity.SystemTestCase;
import com.example.springboot.testarena.problem.repository.ProblemRepository;
import com.example.springboot.testarena.problem.repository.SystemTestCaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemService {


    private final ProblemRepository problemRepository;
    private final SystemTestCaseRepository systemTestCaseRepository;

    public ProblemService(ProblemRepository problemRepository, SystemTestCaseRepository systemTestCaseRepository) {
        this.problemRepository = problemRepository;
        this.systemTestCaseRepository = systemTestCaseRepository;
    }

    public void addNewProblem(ProblemSubmissionRequest problemSubmissionRequest) {
        Problem problem = addProblem(problemSubmissionRequest);

        addTestCase(problemSubmissionRequest, problem.getProblemId());

    }

    private void addTestCase(ProblemSubmissionRequest problemSubmissionRequest, long problemId) {
        Problem problem = problemRepository.findById(problemId).orElse(null);
        if (problem == null) {return;}
//        for(TestCase testCase : problemSubmissionRequest.getSampleTestCase()){
//            SampleTestCase sampleTestCase = new SampleTestCase(problem, testCase.input(), testCase.expectedOutput());
//            sampleTestCaseRepository.save(sampleTestCase);
//        }

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
        problem.setSampleInput(problemSubmissionRequest.getSampleInput());
        problem.setSampleOutput(problemSubmissionRequest.getSampleOutput());

        problem.setStatus("pending");

        problemRepository.save(problem);
        return problem;
    }

    public List<Problem> getAllProblem(String status){
        System.out.println(status);
        return problemRepository.findByStatus(status);
    }

    public String deleteProblem(long problemId){
        Problem problem = problemRepository.findById(problemId).orElse(null);
        List<SystemTestCase> systemTestCase =systemTestCaseRepository.findByProblem(problem);
        if(!systemTestCase.isEmpty()){
            systemTestCaseRepository.deleteAll(systemTestCase);
        }
        if(problem != null){
            problemRepository.delete(problem);
            return "deleted successfully";
        }
        return "problem not found";
    }

    public String updateProblem(long problemId,String status){
        Problem problem = problemRepository.findById(problemId).orElse(null);
        if(problem != null){
            problem.setStatus(status);
            problemRepository.save(problem);
            return "updated successfully";
        }
        return "problem not found";
    }
}
