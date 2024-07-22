package com.example.springboot.onlinejudge.problem.repository;

import com.example.springboot.onlinejudge.problem.entity.SystemTestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemTestCaseRepository extends JpaRepository<SystemTestCase, Long> {
}
