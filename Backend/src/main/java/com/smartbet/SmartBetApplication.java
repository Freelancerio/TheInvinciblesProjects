package com.smartbet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
<<<<<<< HEAD
@EnableScheduling  // Enable scheduled tasks
@EnableAsync       // Enable async execution
@ComponentScan(basePackages = {"com.smartbet", "com.footballapi"}) // Scan both packages
=======
@EnableScheduling
@EnableAsync     
@ComponentScan(basePackages = {"com.smartbet", "com.footballapi"})
>>>>>>> 3f151e00f7c6f3d4897c3ce182d2aedf2c37a412
public class SmartBetApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartBetApplication.class, args);
    }
}