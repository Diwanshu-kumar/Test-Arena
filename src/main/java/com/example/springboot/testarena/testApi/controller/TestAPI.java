package com.example.springboot.testarena.testApi.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class TestAPI {

    @GetMapping("/test")
    public String test(){
        return "Welcome to Test Arena. Backend - API is working ";
    }

}
