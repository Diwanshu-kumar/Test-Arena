package com.example.springboot.onlinejudge.problem.repository;

import com.example.springboot.onlinejudge.problem.entity.SampleTestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleTestCaseRepository extends JpaRepository<SampleTestCase, Long> {

}
