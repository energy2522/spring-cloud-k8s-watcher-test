package com.demo.k8sconfig.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.k8sconfig.service.HelloService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class HelloController {
    private final HelloService helloService;

    @GetMapping
    public String sayHello() {
        return helloService.sayHello();
    }
}
