package com.example.springboot.testarena.submitCode.service;

import com.example.springboot.testarena.submitCode.dto.SubmittedCode;
import com.example.springboot.testarena.submitCode.dto.mySubmission;
import com.example.springboot.testarena.submitCode.repository.SubmissionRepository;
import com.example.springboot.testarena.user.entity.User;
import com.example.springboot.testarena.user.repository.UserRepository;
import com.example.springboot.testarena.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServeUserService {

    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;

    ServeUserService(UserRepository userRepository, SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
    }

    public List<mySubmission> getUserSubmission(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            return null;
        }
        return submissionRepository.findByUserId(user.getId());
    }

    public SubmittedCode getCode(Long submissionId) {
        return submissionRepository.findBySubmissionId(submissionId);
    }
}
