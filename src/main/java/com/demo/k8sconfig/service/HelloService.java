package com.demo.k8sconfig.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RefreshScope //This is required for the bean to be recreated, so the properties used in this class are refreshed
public class HelloService {
    private final Map<String, String> languageHelloMap;

    @Value("${app.lang}")
    private String lang;

    @Value("${app.user.name}")
    private String name;

    public HelloService() {
        this.languageHelloMap = new HashMap<>();

        languageHelloMap.put("en", "Hello, %s!");
        languageHelloMap.put("es", "Hola, %s!");
        languageHelloMap.put("de", "Hi, %s!");
    }

    public String sayHello() {
        return Optional.ofNullable(languageHelloMap.get(lang)).map(hello -> String.format(hello, name)).orElse("Invalid language");
    }

    /**
     * This method reacts to the refresh event and prints properties value.
     */
    @EventListener(RefreshScopeRefreshedEvent.class)
    public void reactOnRefresh(RefreshScopeRefreshedEvent event) {
        log.info("^^^^^^^^^^^^^^^^^^^^^");
        log.info("^^^^^^^^^^^^^^^^^^^^^");
        log.info("refresh has happened");
        log.info("^^^^^^^^^^^^^^^^^^^^^");
        log.info("^^^^^^^^^^^^^^^^^^^^^");
        log.info("lang = " + lang);
        log.info("name = " + name);
    }

}
