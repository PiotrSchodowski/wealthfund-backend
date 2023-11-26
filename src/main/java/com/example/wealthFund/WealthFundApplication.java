package com.example.wealthFund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WealthFundApplication {
    public static void main(String[] args) {
        SpringApplication.run(WealthFundApplication.class, args);


    }
}
