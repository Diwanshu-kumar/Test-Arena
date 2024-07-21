package com.example.springboot.onlinejudge.submitCode.repository;

import com.example.springboot.onlinejudge.submitCode.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

}
