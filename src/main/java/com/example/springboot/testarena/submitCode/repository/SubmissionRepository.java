package com.example.springboot.testarena.submitCode.repository;

import com.example.springboot.testarena.submitCode.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

}
