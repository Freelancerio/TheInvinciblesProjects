package com.smartbet;

import com.footballapi.LeagueTableResponse;
import com.footballapi.PslFootballService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
    public CommandLineRunner displayStandings(PslFootballService pslFootballService) {
        return args -> {
            LeagueTableResponse standings = pslFootballService.getStandings();
            
            for(int i = 0; i < standings.getTable().size(); i++) {
                System.out.println(standings.getTable().get(i));
            }
        };
    }
}
