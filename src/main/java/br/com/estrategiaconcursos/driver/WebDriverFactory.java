package br.com.estrategiaconcursos.driver;

import br.com.estrategiaconcursos.base.Util;
import br.com.estrategiaconcursos.util.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author lucasns
 * @since #1.0
 */
public class WebDriverFactory {
    // ------------------------------ FIELDS ------------------------------
    public static WebDriver webDriverInstance;
    static final Logger log = LoggerFactory.getLogger(WebDriverFactory.class.getSimpleName());
    static final HashMap<String, WebDriver> webDriverMap = new HashMap<>();
    static final String resourcePath = Util.pegarResourcePath("target/Download");

    // -------------------------- OTHERS METHODS --------------------------

    /**
     * Verifica se algum webdriver esta aberto
     */
    @NotNull
    @Contract(pure = true)
    public static Boolean verificarDriverAberto() {
        return (webDriverInstance != null) ?
                verificarJanelaAberta() : false;
    }

    @NotNull
    private static Boolean verificarJanelaAberta() {
        try {
            if (webDriverInstance.getWindowHandles() != null)
                return true;

        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Criando novo Driver Padrão
     */
    public static void criarNovoWebDriver() {
        criarNovoWebDriver(Data.get("browser"));
    }

    /**
     * Pegando a instancia do MAP e colocando na variavel de instacia atual
     *
     * @param instance Nome da insgtancia
     */
    public static void getWebDriverInstance(String instance) {
        webDriverInstance = webDriverMap.get(instance);
    }

    /**
     * Verificnado qual driver pegar
     *
     * @param instance Nome da instancia
     */
    public static void criarNovoWebDriver(@NotNull String instance) {

        log.debug("Pegar driver padrão 'Firefox'");
        if (instance.equals(""))
            setBrowser(instance, FirefoxDriver.getDriver());

        log.debug("Pegando driver do Firefox");
        if (instance.equals("firefox"))
            setBrowser(instance, FirefoxDriver.getDriver());

        if (instance.equals("chrome"))
            setBrowser(instance, ChromeDriver.getDriver());

        if (instance.equals("chromeheadless"))
            setBrowser(instance, ChromeDriver.getDriver(true));
    }
    // -------------------------- PRIVATE METHODS --------------------------
    /**
     * Verificando se existe algum driver aberto com o nome da instacia se não abre um novo
     *
     * @param instance  Nome da instancia
     * @param webDriver Driver para colocar no Map
     */
    private static void setBrowser(String instance, WebDriver webDriver) {
        if (getKey(instance) != null)
            getWebDriverInstance(instance);
        else {
            webDriverMap.put(instance, webDriver);
            getWebDriverInstance(instance);
        }
    }

    /**
     * Retorna o key se ele existe
     *
     * @param key Nome da key
     * @return Retorna um String com o nome da key  se exitir
     */
    @Nullable
    private static String getKey(String key) {
        for (String aKey : webDriverMap.keySet()) {
            if ((aKey).equals(key))
                return aKey;
        }
        return null;
    }
    // -------------------------- END OF OTHERS METHODS --------------------------
}