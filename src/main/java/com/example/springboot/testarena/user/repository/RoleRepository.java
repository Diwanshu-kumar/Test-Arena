package com.example.springboot.testarena.user.repository;

import com.example.springboot.testarena.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {}