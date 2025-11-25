package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/Features",
        glue = "stepDefinitions",
        // --- CONFIGURE REPORTING PLUGINS FOR XRAY ---
        // The "json:..." plugin generates the report required by Xray
        plugin = {"pretty", "json:target/cucumber-report/cucumber.json", "html:target/cucumber-report/cucumber.html"},
        monochrome = true
)
public class TestRunner {
    // This file remains empty
}