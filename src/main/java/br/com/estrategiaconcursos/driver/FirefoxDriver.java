package br.com.estrategiaconcursos.driver;

import br.com.estrategiaconcursos.util.Data;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;

/**
 * @author lucasns
 * @since #1.0
 */
class FirefoxDriver extends WebDriverFactory {
    // ------------------------------ FILDS ------------------------------
    static {
        driverUpdate();
    }

    // ------------------------------ OTHERS METHODS ------------------------------

    /**
     * Cria um novo Driver do Firefox
     *
     * @return Retorna um Driver para FireFox
     */
    @NotNull
    static WebDriver getDriver() {
        log.debug("Criando FirefoxDriver");
        Data.set("browser", "firefox");


        log.debug("Criando FirefoxProfile");
        return new org.openqa.selenium.firefox.FirefoxDriver(getService(), getFirefoxProfile());
    }

    // -------------------------- PRIVATE METHODS --------------------------
    private static void driverUpdate() {
        if (Data.get("DriverUpdate").equals("y"))
            WebDriverManager.firefoxdriver().setup();

        else WebDriverManager.firefoxdriver().forceCache().setup();
    }

    @NotNull
    private static FirefoxOptions getFirefoxProfile() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addPreference("browser.download.folderList", 2);
        firefoxOptions.addPreference("browser.download.manager.showWhenStarting", false);
        firefoxOptions.addPreference("browser.download.dir", resourcePath);
        firefoxOptions.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream,text/xml");
        return firefoxOptions;
    }

    private static GeckoDriverService getService() {
        return new GeckoDriverService.Builder().usingAnyFreePort().build();
    }
    // -------------------------- END OF OTHERS METHODS --------------------------
}