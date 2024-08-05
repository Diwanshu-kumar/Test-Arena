package com.example.springboot.testarena.submitCode.repository;

import com.example.springboot.testarena.submitCode.dto.SubmittedCode;
import com.example.springboot.testarena.submitCode.dto.mySubmission;
import com.example.springboot.testarena.submitCode.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<mySubmission> findByUserId(Long userId);

    SubmittedCode findBySubmissionId(Long submissionId);
}
