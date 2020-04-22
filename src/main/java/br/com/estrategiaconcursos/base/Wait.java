package br.com.estrategiaconcursos.base;

import br.com.estrategiaconcursos.util.Data;
import br.com.estrategiaconcursos.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static br.com.estrategiaconcursos.driver.WebDriverFactory.webDriverInstance;
import static junit.framework.TestCase.fail;

/**
 * @author lucasns
 * @since #1.0
 */
public class Wait {
    // ------------------------------ FIELDS ------------------------------
    private static final Logger log = LoggerFactory.getLogger(Wait.class.getSimpleName());
    private static final Integer DEFAULT_TIMEOUT = Integer.valueOf(Data.get("DEFAULT_TIMEOUT"));
    public static Integer tempoDeEspera = 0;
    private static Integer numeroDeTimeOuts = 0;
    private static String urlAtual;
    private static String elemento;

    //--------------------------------- OTHER METHODS ------------------------------------
    //--------------------------------- SetWait ------------------------------------

    public static void setWait() {
        setWait(1000);
    }

    public static void setWait(Integer wait) {
        esperar(wait);
    }

    public static Boolean setWait(By locator) {
        return setWait(() -> presenceOfAllElementsLocatedBy(locator));
    }

    public static Boolean setWait(String timeOutMessage, By locator) {
        return setWait(timeOutMessage, () -> presenceOfAllElementsLocatedBy(locator));
    }

    @SafeVarargs
    public static Boolean setWait(Integer timeout, Supplier<Boolean>... waitConditions) {
        return waitConditions(timeout, waitConditions, false);
    }

    @SafeVarargs
    public static Boolean setWaitDisappear(Integer timeout, Supplier<Boolean>... waitConditions) {
        return waitConditions(timeout, waitConditions, true);
    }

    @SafeVarargs
    public static Boolean setWait(String timeOutMessage, Integer timeout, Supplier<Boolean>... waitConditions) {
        return waitConditions(timeOutMessage, timeout, waitConditions, false);
    }

    @SafeVarargs
    public static Boolean setWaitDisappear(String timeOutMessage, Integer timeout, Supplier<Boolean>... waitConditions) {
        return waitConditions(timeOutMessage, timeout, waitConditions, true);
    }

    @SafeVarargs
    public static Boolean setWait(Supplier<Boolean>... waitConditions) {
        return waitConditions(waitConditions, false);
    }

    @SafeVarargs
    public static Boolean setWaitDisappear(Supplier<Boolean>... waitConditions) {
        return waitConditions(waitConditions, true);
    }

    @SafeVarargs
    public static Boolean setWait(String timeOutMessage, Supplier<Boolean>... waitConditions) {
        return waitConditions(timeOutMessage, waitConditions, false);
    }

    @SafeVarargs
    public static Boolean setWaitDisappear(String timeOutMessage, Supplier<Boolean>... waitConditions) {
        return waitConditions(timeOutMessage, waitConditions, true);
    }

    private static Boolean waitConditions(Supplier<Boolean>[] waitConditions, Boolean waitType) {
        return waitConditions(null, DEFAULT_TIMEOUT, waitConditions, waitType);
    }

    private static Boolean waitConditions(String timeOutMessage, Supplier<Boolean>[] waitConditions, Boolean waitType) {
        return waitConditions(timeOutMessage, DEFAULT_TIMEOUT, waitConditions, waitType);
    }

    private static Boolean waitConditions(Integer timeout, Supplier<Boolean>[] waitConditions, Boolean waitType) {
        return waitConditions(null, timeout, waitConditions, waitType);
    }

    private static Boolean waitConditions(String timeOutMessage, Integer timeout, Supplier<Boolean>[] waitConditions, Boolean waitType) {
        elemento = null;
        Boolean conditionReturn = null;
        esperar(100);//@Leoncio@: Não retirar, isso não deixa o Selenium querer proceguir antes do necessário.

        if (waitType != null) {

            for (Supplier<Boolean> condition : waitConditions)
                conditionReturn = wait(timeOutMessage, timeout, condition, waitType);
        }

        return conditionReturn;
    }

    @NotNull
    private static Boolean wait(Supplier<Boolean> condition, Boolean waitType) {
        return wait(null, DEFAULT_TIMEOUT, condition, waitType);
    }

    @NotNull
    private static Boolean wait(Integer timeout, Supplier<Boolean> condition, Boolean waitType) {
        return wait(null, timeout, condition, waitType);
    }

    @NotNull
    private static Boolean wait(String timeOutMessage, Supplier<Boolean> condition, Boolean waitType) {
        return (timeOutMessage == null) ?
                wait(null, DEFAULT_TIMEOUT, condition, waitType) :
                wait(timeOutMessage, DEFAULT_TIMEOUT, condition, waitType);
    }

    private static Boolean wait(String timeOutMessage, Integer timeout, Supplier<Boolean> condition, Boolean waitType) {

        boolean value = waitFor(timeout, condition, waitType);

        if (!value) {
            ++numeroDeTimeOuts;
            logTimeOutMessage(timeOutMessage, timeout, waitType);

        } else
            numeroDeTimeOuts = 0;

        if (numeroDeTimeOuts == 3)
            throw new RuntimeException("TimeOut mais de 4x... ");

        return value;
    }

    public static boolean waitFor(Supplier<Boolean> condition) {
        return waitFor(DEFAULT_TIMEOUT, condition, false);
    }

    public static boolean waitFor(Supplier<Boolean> condition, Boolean waitType) {
        return waitFor(DEFAULT_TIMEOUT, condition, waitType);
    }

    public static boolean waitFor(Integer timeout, Supplier<Boolean> condition) {
        return waitFor(timeout, condition, false);
    }

    public static boolean waitFor(Integer timeout, Supplier<Boolean> condition, Boolean waitType) {
        int count = 0;
        Integer loop = 100;

        boolean value;

        if (waitType) {
            while (value = condition.get() && !loop.equals(count++))
                esperar(timeout / loop);
        } else {
            while (!(value = condition.get()) && !loop.equals(count++))
                esperar(timeout / loop);
        }

        return value;
    }

    private static void logTimeOutMessage(String timeOutMessage, Integer timeout, Boolean waitType) {
        if (!waitType) {

            if (timeOutMessage == null) {
                timeOutMessage = "Elemento não encontrato";
                Data.set("elementNotFound", elemento);
            }

            if (elemento == null)
                elemento = "Condição não requer elemento";

            log.warn("\n\n" +
                    "     Mensagem: " + StringUtils.colored(timeOutMessage, StringUtils.Cores.PRETO, null, StringUtils.Formatacao.NEGRITO) + "\n" +
                    "     Ultimo elemento: " + StringUtils.colored(elemento, StringUtils.Cores.PRETO, null, StringUtils.Formatacao.NEGRITO) + "\n\n" +

                    "     Timeout: " + StringUtils.colored(timeout.toString(), StringUtils.Cores.PRETO, null, StringUtils.Formatacao.NEGRITO) + "\n" +
                    "     Timeout seguidos: " + StringUtils.colored(numeroDeTimeOuts.toString(), StringUtils.Cores.PRETO, null, StringUtils.Formatacao.NEGRITO));
        }
    }

    //--------------------------------- Conditions ------------------------------------
    @NotNull
    public static Boolean alertIsPresent() {
        try {
            return ExpectedConditions.alertIsPresent().apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        } catch (Exception e) {
            log.error("Alerta nao encontrado na tela\n", e);
            return false;
        }
    }

    @NotNull
    public static Boolean elementToBeClickable(By locator) {
        try {
            elemento = locator.toString();
            return ExpectedConditions.elementToBeClickable(locator).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        } catch (Exception e) {
            log.error("Elemento nao e clicavel no locator passado " + locator.toString() + "\n", e);
            return false;
        }
    }

    @NotNull
    public static Boolean elementToBeClickable(WebElement webElement) {
        try {
            elemento = webElement.toString();
            return ExpectedConditions.elementToBeClickable(webElement).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        } catch (Exception e) {
            log.error("Elemento nao e clicavel no elemento passado " + webElement.toString() + "\n", e);
            return false;
        }
    }

    @NotNull
    public static Boolean frameToBeAvailableAndSwitchToIt(By locator) {
        try {
            elemento = locator.toString();
            return ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        } catch (Exception e) {
            log.error("Frame nao valido, nao e possivel trocar para ele ou o locator nao tem o frame para trocar " + locator.toString() + "\n", e);
            return false;
        }
    }

    @NotNull
    public static Boolean invisibilityOfElementLocated(By locator) {
        try {
            elemento = locator.toString();
            return ExpectedConditions.invisibilityOfElementLocated(locator).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (ElementNotVisibleException e) {
            log.error("O elemento nao esta visivel no locator" + locator.toString() + "\n", e);
            return false;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        }
    }

    @NotNull
    public static Boolean presenceOfAllElementsLocatedBy(By locator) {
        try {
            elemento = locator.toString();
            return ExpectedConditions.presenceOfAllElementsLocatedBy(locator).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (InvalidSelectorException ise) {
            log.debug("Seletor inválido");
            return false;
        } catch (ElementNotVisibleException e) {
            log.error("O elemento nao esta visivel no locator" + locator.toString() + "\n", e);
            return false;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        }
    }

    @NotNull
    public static Boolean presenceOfElementLocated(By locator) {
        try {
            elemento = locator.toString();
            return ExpectedConditions.presenceOfElementLocated(locator).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (ElementNotVisibleException e) {
            log.error("O elemento nao esta visivel no locator" + locator.toString() + "\n", e);
            return false;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        }
    }

    @NotNull
    public static Boolean visibilityOfAllElementsLocatedBy(By locator) {
        try {
            elemento = locator.toString();
            return ExpectedConditions.visibilityOfAllElementsLocatedBy(locator).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (ElementNotVisibleException e) {
            log.error("O elemento nao esta visivel no locator" + locator.toString() + "\n", e);
            return false;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        }
    }

    @NotNull
    public static Boolean visibilityOfElementLocated(By locator) {
        try {
            elemento = locator.toString();
            return ExpectedConditions.visibilityOfElementLocated(locator).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        }  catch (ElementNotVisibleException e) {
            log.error("O elemento nao esta visivel no locator" + locator.toString() + "\n", e);
            return false;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        }
    }

    @NotNull
    public static Boolean visibilityOf(WebElement webElement) {
        try {
            elemento = webElement.toString();
            return ExpectedConditions.visibilityOf(webElement).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (ElementNotVisibleException e) {
            log.error("O elemento nao esta visivel no locator" + webElement.toString() + "\n", e);
            return false;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        }
    }

    @NotNull
    public static Boolean visibilityOfAllElements(List<WebElement> webElementList) {
        try {
            return ExpectedConditions.visibilityOfAllElements(webElementList).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        } catch (ElementNotVisibleException e) {
            log.error("Os elementos nao estao visiveis pela lista de elementos passado\n", e);
            return false;
        }
    }

    @NotNull
    public static Boolean urlAtualContains(String url) {
        try {
            return Page.pegarURLAtual().contains(url);
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (Exception e) {
            log.error("A url nao contem o o pedacode url passado " + url + "\n", e);
            return false;
        }
    }

    @NotNull
    public static Boolean urlContains(String url) {
        try {
            return ExpectedConditions.urlContains(url).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        } catch (Exception e) {
            log.error("A url nao contem o o pedacode url passado " + url + "\n", e);
            return false;
        }
    }

    @NotNull
    public static Boolean urlToBe(String url) {
        try {
            return ExpectedConditions.urlToBe(url).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (NullPointerException npe) {
            log.debug("Nao foi encontrada a url");
            return false;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        } catch (UnhandledAlertException ua) {
            return false;
        } catch (Exception e) {
            log.debug("A url nao eh a url passada " + url + "\n", e);
            return false;
        }
    }

    @NotNull
    public static Boolean elementToBeSelected(By locator) {
        try {
            elemento = locator.toString();
            return ExpectedConditions.elementToBeSelected(locator).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        } catch (Exception e) {
            log.error("Elemento nao pode ser selecionando com o locator " + locator.toString() + "\n", e);
            return false;
        }
    }

    @NotNull
    public static Boolean elementToBeSelected(WebElement element) {
        try {
            elemento = element.toString();
            return ExpectedConditions.elementToBeSelected(element).apply(webDriverInstance) != null;
        } catch (TimeoutException te) {
            log.error("Erro do ChromeDriver de TimeOut");
            throw te;
        } catch (NoSuchElementException no) {
            log.debug("Elemento nao encontrado");
            return false;
        } catch (Exception e) {
            log.error("Elemento nao pode ser selecionado pelo elemento passado " + element.toString() + "\n", e);
            return false;
        }
    }
    //--------------------------------- Verifies ------------------------------------
    /**
     * Verifica o carregamento das paginas
     */
    public static void checkURL() {
        urlAtual = Page.pegarURLAtual();
        if ("data:,".equals(urlAtual))
            fail();

        if (!Objects.equals(Data.get("urlAtual"), urlAtual))
            Data.set("urlAtual", urlAtual);

        urlAtual = Page.pegarURLAtual();
    }
    //--------------------------------- Sleep ------------------------------------

    /**
     * Espera por algum tempo.
     *
     * @param mSegundos Tempo a ser esperado em milisegundos.
     */
    private static void esperar(Integer mSegundos) {
        log.debug("Esperando por alguns segundos(" + mSegundos + ")...");
        tempoDeEspera = tempoDeEspera + mSegundos;
        try {
            Thread.sleep(mSegundos);
        } catch (InterruptedException e) {
            log.error("Não foi possível espera " + mSegundos + "... " + e);
        }
    }
}