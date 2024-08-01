package com.example.springboot.testarena.user.service;

import com.example.springboot.testarena.user.entity.Role;
import com.example.springboot.testarena.user.entity.RoleName;
import com.example.springboot.testarena.user.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(RoleName.ROLE_USER));
            roleRepository.save(new Role(RoleName.ROLE_ADMIN));
        }
    }
}
