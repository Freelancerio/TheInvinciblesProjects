package com.smartbet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableScheduling
@EnableAsync     
@ComponentScan(basePackages = {"com.smartbet", "com.footballapi"})
public class SmartBetApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartBetApplication.class, args);
    }
}