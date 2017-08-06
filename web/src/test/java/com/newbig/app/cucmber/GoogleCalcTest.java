package com.newbig.app.cucmber;

import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/com/newbig/app/cucmber/feature",
    glue = "com.newbig.app.cucmber.steps",
    plugin = {
        "pretty",
        "html:target/cucumber",
    }

)
public class GoogleCalcTest {

}
