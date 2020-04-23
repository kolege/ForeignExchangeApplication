package com.kolege.assignment;

import com.kolege.assignment.service.CurrencyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class AssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentApplication.class, args);
    }

    @Bean
    CommandLineRunner runOnStart(CurrencyService currencyService) {
        return args -> {
            // get units and write to db
            currencyService.saveUnits();
        };
    }

}
