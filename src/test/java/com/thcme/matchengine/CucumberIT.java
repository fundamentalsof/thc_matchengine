// src/test/java/com/thcme/matchengine/CucumberIT.java
package com.thcme.matchengine;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.thcme.matchengine.stepdefs,com.thcme.matchengine")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty,html:target/cucumber-reports.html")
public class CucumberIT {

    @Test
    void dummyTest() {
        // This is a dummy test to verify Failsafe runs at least one test
        assert true;
    }
}