package com.example.springboot.testarena.problem.repository;


import com.example.springboot.testarena.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByStatus(String status);
}
