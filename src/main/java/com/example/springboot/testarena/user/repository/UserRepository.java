package com.example.springboot.testarena.user.repository;

import com.example.springboot.testarena.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}