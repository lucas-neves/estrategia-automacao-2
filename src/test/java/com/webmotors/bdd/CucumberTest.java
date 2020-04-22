package com.webmotors.bdd;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * @author lucasns
 * @since #1.0
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources",
        plugin = { "pretty", "html:target/selenium-reports"})
public class CucumberTest {

}
