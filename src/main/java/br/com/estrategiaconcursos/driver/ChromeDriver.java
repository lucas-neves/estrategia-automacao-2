package br.com.estrategiaconcursos.driver;

import br.com.estrategiaconcursos.util.Data;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lucasns
 * @since #1.0
 */
class ChromeDriver extends WebDriverFactory {
    // ------------------------------ FIELDS ------------------------------
    static {
        driverUpdate();
    }

    // ------------------------------ OTHERS METHODS ------------------------------
    @NotNull
    static WebDriver getDriver() {
        return getDriver(false);
    }

    /**
     * Cria um novo Driver para Chrome
     *
     * @return Retorna um Driver para Chrome
     */
    @NotNull
    static WebDriver getDriver(Boolean headless) {
        log.debug("adicionando opções ao browser");
        ChromeOptions options = getChromeOptions(headless, getStringObjectMap());

        log.debug("colocando os capabilitires nos options");
        options.merge(getCapabilities(options));

        log.debug("Criando instancia do Chrome");
        return new org.openqa.selenium.chrome.ChromeDriver(getService(), options);
    }

    // -------------------------- PRIVATE METHODS --------------------------
    private static void driverUpdate() {
        if (Data.get("DriverUpdate").equals("y"))
            WebDriverManager.chromedriver().setup();

        else WebDriverManager.chromedriver().forceCache().setup();
    }

    @NotNull
    private static Map<String, Object> getStringObjectMap() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", resourcePath);
        return prefs;
    }

    @NotNull
    private static ChromeOptions getChromeOptions(Boolean headless, Map<String, Object> prefs) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            Data.set("browser", "chromeheadless");

            options.addArguments("headless");
            options.addArguments("window-size=1920x1080");

        } else Data.set("browser", "chrome");

        options.addArguments("--no-sandbox");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.setExperimentalOption("prefs", prefs);
        return options;
    }

    @NotNull
    private static DesiredCapabilities getCapabilities(ChromeOptions options) {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();

        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability("requireWindowFocus", true);
        capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        return capabilities;
    }

    private static ChromeDriverService getService() {
        return new ChromeDriverService.Builder().usingAnyFreePort().build();
    }
    // -------------------------- END OF OTHERS METHODS --------------------------
}