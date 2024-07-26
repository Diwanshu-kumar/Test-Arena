package com.example.springboot.testarena.problem.repository;

import com.example.springboot.testarena.problem.entity.Problem;
import com.example.springboot.testarena.problem.entity.SystemTestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemTestCaseRepository extends JpaRepository<SystemTestCase, Long> {
    List<SystemTestCase> findByProblem(Problem problem);
}
