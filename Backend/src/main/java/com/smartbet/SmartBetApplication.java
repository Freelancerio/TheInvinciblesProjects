package com.smartbet;

import com.footballapi.LeagueTableResponse;
import com.footballapi.PslFootballService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.smartbet", "com.footballapi"})
public class SmartBetApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartBetApplication.class, args);
    }

    @Bean
    public LeagueTableResponse getMatches(){
        PslFootballService pslFootballService = new PslFootballService();
        return pslFootballService.getStandings();
    }
}