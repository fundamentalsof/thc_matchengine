package com.thcme.matchengine;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.thcme.matchengine", "com.thcme.matchengine.stepdefs"},
    plugin = {"pretty", "html:target/cucumber-reports.html"}
)
public class CucumberTest {
}