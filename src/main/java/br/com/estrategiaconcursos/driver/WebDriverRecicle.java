package br.com.estrategiaconcursos.driver;

import cucumber.api.Scenario;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * @author lucasns
 * @since #1.0
 */
public class WebDriverRecicle extends WebDriverFactory {
    // -------------------------- OTHERS METHODS --------------------------
    /**
     * Veririfa se o Driver atual esta aberto
     *
     * @return Retorna um Boolean se esta ou não aberto o driver
     */
    @Contract(pure = true)
    public static boolean verificarWebDriverAberto() {
        return webDriverInstance != null;
    }

    /**
     * Recicla o Driver Atual
     */
    public static void recicleWebDriver(Scenario scenario) {
        scenario.embed(((TakesScreenshot)webDriverInstance).getScreenshotAs(OutputType.BYTES), "image/png");
        closeWebDriver();
    }

    // -------------------------- PRIVATE METHODS --------------------------
    /**
     * Recicla o Driver
     */
    private static void closeWebDriver() {
        webDriverMap.forEach((key, webDriver) -> webDriver.quit());
        webDriverMap.clear();
    }
    // -------------------------- END OF OTHERS METHODS --------------------------
}