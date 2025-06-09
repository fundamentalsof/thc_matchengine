package com.thcme.matchengine;

import com.thcme.matchengine.stepdefs.OrderSubmissionSteps;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@CucumberContextConfiguration
@SpringBootTest
public class CucumberSpringConfiguration {


    @Bean
    public OrderSubmissionSteps orderSubmissionSteps() {
        return new OrderSubmissionSteps();
    }
}