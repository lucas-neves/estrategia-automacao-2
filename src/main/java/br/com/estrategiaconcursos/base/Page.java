package br.com.estrategiaconcursos.base;

import br.com.estrategiaconcursos.driver.WebDriverRecicle;
import br.com.estrategiaconcursos.util.Data;
import br.com.estrategiaconcursos.util.NumberUtils;
import br.com.estrategiaconcursos.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static br.com.estrategiaconcursos.base.Wait.*;
import static br.com.estrategiaconcursos.driver.WebDriverFactory.webDriverInstance;
import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;

/**
 * @author lucasns
 * @since #1.0
 */
public class Page {
    // ------------------------------ FIELDS ------------------------------
    protected static final Logger log = LoggerFactory.getLogger(Page.class.getSimpleName());
    public static String screenshotPath = "";

    // -------------------------- OTHER METHODS --------------------------

    /**
     * Pega uma lista de elemento apartir do localizador
     *
     * @param locator localizador do WebElement
     * @return retorna uma lista de elemento referenciado pelo localizador
     */
    private static List<WebElement> getElements(By locator) {
        log.debug("retorna uma lista de elemento referenciado pelo localizador...");
        return webDriverInstance.findElements(locator);
    }

    /**
     * Retorna o valor da URL que esta navegendo no momento;
     *
     * @return String da URL;
     */
    public static String pegarURLAtual() {
        log.debug("Pegando URL Atual...");
        return webDriverInstance.getCurrentUrl();
    }

    /**
     * Checks if a given element exists in page
     *
     * @param locator The element selector by *name*
     * @return true if element exists or false otherwise
     */
    public static boolean existsElement(By locator) {
        log.debug("Verifica se existe o elemento passado por parametro na tela...");
        return getElements(locator).size() != 0;
    }

    /**
     * Faz highlight no elemento
     *
     * @param locator localizador do elemento
     */
    protected static void highlightElement(By locator) {
        log.debug("Fazendo highlight no elemento...");
        try {
            js("arguments[0].style.border='3px solid " + Data.get("color") + "'", locator);
        } catch (Exception e) {
            log.debug("Não pode aplicar o highligth", e);
        }
    }

    /**
     * Pega o webElement paratir do localizador
     *
     * @param locator localizador do elemento
     * @return retorna o WebElement refereniado pelo localizador
     */
    private static WebElement getElement(By locator) {
        log.debug("Pega o webElement partir do localizador...");
        highlightElement(locator);
        return webDriverInstance.findElement(locator);
    }

    /**
     * Executa comandos do tipo JavaScript
     *
     * @param script  Comando que deve ser executado
     * @param objects Objeto para complementar o comando
     * @return Se tiver resultado, Retorna um objeto com as informaçoes que o script retorna
     */
    private static Object js(String script, Object... objects) {
        return ((JavascriptExecutor) webDriverInstance).executeScript(script, objects);
    }

    /**
     * Acessa a URL indicada
     *
     * @param url URL para acessar
     */
    public void acessarUrl(String url) {
        log.debug("acessar a url");
        webDriverInstance.get(url);
    }

    /**
     * Clica no botao passado no localizador
     *
     * @param locator Localizador By do elemento.
     */
    public void clicar(By locator) {
        AssertPage assertPage = new AssertPage();
        assertPage.assertPage();

        log.debug("Clicando no botão... " + locator.toString());
        setWait(() -> Wait.elementToBeClickable(locator));

        while (existsElement(By.xpath("//svg"))){
            setWait(1000);
            log.info("Aguardando carregamento...");
        }

        clicar(locator, checkElement(locator));
    }

    /**
     * Custom
     * Realiza a rolagem da tela com Javascript
     *
     */
    public void scrollToElementJS(WebElement element) {
        try{
            int Y = (element.getLocation().getY() - 500);
            js("javascript:window.scrollTo(0," + Y + ");");
        } catch (Exception e ){
            log.info("Elemento não encontrado via Javascript");
        }
    }

    /**
     * Troca Frame ou IFrame por ID.
     */
    public void trocarFrame(String id) {
        log.debug("Realiza a troca de frame...");
        setWait("\n" +
                        "Nao foi possivel achar o elemento 'By.tagName(\"iframe\")'" +
                        "\n Tentando trocar o Frame mesmo assim...",
                () -> frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe#" + id)));
    }

    /**
     * @param locator locator do elemento
     */
    public void selecionarCheckbox(By locator) {
        log.debug("Seleciona X checkbox e filtra por página...");
        setWait(() -> Wait.elementToBeClickable(locator));

        List<WebElement> tableRows = checkElements(locator);

        for (WebElement element : tableRows) {
            if (element.isDisplayed()) {
                if (!element.isSelected())
                    element.click();
            }
        }
    }

    /**
     * Seleciona uma opção do combo apartir de um pedaço do texto
     *
     * @param locator localizador do elemento.
     * @param texto   pedaço do texto para ser usado.
     */
    public void selecionarComboPorTexto(By locator, String texto) {
        log.debug("Selecionando um combo apartir de um pedaço de texto...");

        setWait(() -> visibilityOfElementLocated(locator));

        if (existsElement(locator)) {

            WebElement element = checkElement(locator);

            if (element == null)
                element = getElement(locator);

            Select select = new Select(element);

            for (WebElement webElement : select.getOptions()) {
                if (StringUtils.tudoL(getText(webElement)).contains(StringUtils.tudoL(texto))) {
                    select.selectByVisibleText(webElement.getText());
                    break;
                }
            }
        }
    }

    /**
     * Seleciona o combo passando o nome do combo como ira chamar ele 'por' (ex: id, name...);
     * O tipo de do valor 'tipo' (ex: index, text, value) e o valor a chamar 'texto' ou 'index';
     *
     * @param locator Localizador do elemento
     * @param tipo    'index', 'text', 'value';
     * @param texto   Se tiver, Texto que ira ser selecionado;
     * @param index   Se tiver, Index que ira selecionar;
     */
    public void selecionarCombo(By locator, String tipo, String texto, Integer index) {
        log.debug("selecionando um Combo...");
        AssertPage assertPage = new AssertPage();
        assertPage.assertPage();

        setWait(() -> visibilityOfElementLocated(locator));

        if (existsElement(locator)) {

            WebElement element = checkElement(locator);

            if (element == null)
                element = getElement(locator);

            Select combo = new Select(element);
            Integer tamanho = verificarCombo(locator, element);

            if (tamanho != 0) {
                switch (tipo) {
                    case "index":
                        if (!Objects.equals(tamanho, 1))
                            combo.selectByIndex(index);

                        else combo.selectByIndex(0);
                        break;

                    case "text":
                        combo.selectByVisibleText(texto);
                        break;

                    case "value":
                        combo.selectByValue(texto);
                        break;
                }
            }
        }
        log.debug("Combo selecionado... " + locator);
    }

    public void selecionarComboAleatorio(By locator) {
        AssertPage assertPage = new AssertPage();
        assertPage.assertPage();

        selecionarComboAleatorio(locator, 0);
    }

    /**
     * Seleciona um Combo aleatoriamente;
     *
     * @param locator Localizador do elemento
     */
    private void selecionarComboAleatorio(By locator, Integer interator) {
        log.debug("Seleciona um elemento do combo aleatoriamente...");

        AssertPage assertPage = new AssertPage();
        assertPage.assertPage();

        setWait(() -> visibilityOfElementLocated(locator));

        WebElement element = checkElement(locator);
        if (element == null)
            element = getElement(locator);

        Select combo = new Select(element);
        List<WebElement> listaResultado = combo.getOptions();

        Integer tamanhoLista = listaResultado.size();

        Integer ran = NumberUtils.gerarNumeroAleatorio(2, tamanhoLista);

        selecionarCombo(locator, "index", null, ran - 1);

        String selecionado = getText(retornarElementoSelecionadoCombo(locator));

        selecionado = StringUtils.espaco(selecionado);

        if (selecionado == null || Objects.equals(selecionado, "") && interator != 5)
            selecionarComboAleatorio(locator, ++interator);
    }

    /**
     * Troca para o Alert;
     *
     * @param tipo 'true' para OK, 'false' para Cancelar;
     */
    public void switchToAlert(boolean tipo) {
        log.debug("Troca para o Alert...");

        if (Wait.waitFor(2000, Wait::alertIsPresent)) {

            Alert alerta = webDriverInstance.switchTo().alert();
            log.info("Alert text: " + alerta.getText());
            if (tipo) {
                log.debug("Alert OK");
                try {
                    alerta.accept();
                } catch (Exception e) {
                    log.error(getClass().getSimpleName() + " - " + e);
                }
            } else {
                log.debug("Alert Cancel");
                try {
                    alerta.dismiss();
                } catch (Exception e) {
                    log.error(getClass().getSimpleName() + " - " + e);
                }
            }
        }
    }

    /**
     * Verifica se o elemento esta selecionado
     *
     * @param locator localizador do elemento
     * @return se o elemento estiver selecionado, retorna verdadeiro
     */
    protected Boolean isSelected(By locator) {
        log.debug("Verificando se o elemento esta selecionado");
        WebElement element = checkElement(locator);

        return (element != null) ?
                element.isSelected() : getElement(locator).isSelected();
    }

    /**
     * Pega o texto de um elemento pelo locator dele
     *
     * @param locator locatod do elemento
     * @return retona o texto do elemento
     */
    public String getText(By locator) {
        log.debug("Pega texto de um elemento");

        setWait(() -> visibilityOfElementLocated(locator));

        WebElement element = checkElement(locator);

        return (element != null) ?
                element.getText() : getElement(locator).getText();
    }

    /**
     * Pega o texto de um elemento pelo Element dele
     *
     * @param element Elemento
     * @return retorena o texto do elemento
     */
    protected String getText(WebElement element) {
        log.debug("Pega texto de um elemento");
        setWait(() -> visibilityOf(element));
        return element.getText();
    }

    /**
     * Pega atributo de um elemento
     *
     * @param locator  localizador do elemento
     * @param atribute nome do atributo
     * @return Retona o que tem dentro do atributo
     */
    public String getAttribute(By locator, String atribute) {
        log.debug("Pega texto de um elemento");

        setWait(() -> presenceOfAllElementsLocatedBy(locator));

        WebElement element = getElement(locator);

        return (element != null) ?
                element.getAttribute(atribute) : getElement(locator).getAttribute(atribute);
    }

    /**
     * Preenche um campo
     *
     * @param locator localizador do campo
     * @param texto   Texto que ira preencher o campo
     * @param keyss   Comando para mandar de teclado
     */
    public WebElement preencherCampo(By locator, String texto, Keys... keyss) {
        log.debug("Preenche qualquer campo...");
        AssertPage assertPage = new AssertPage();
        assertPage.assertPage();

        setWait(() -> visibilityOfElementLocated(locator));

        WebElement element = checkElement(locator);

        privatePreencherCampo(locator, texto, element, keyss);

        return element;
    }

    /**
     * Apenas a operação de limpar e preencher
     *
     * @param element elemento
     * @param texto   texto a ser preenchido
     */
    public void preencherCampo(WebElement element, String texto) {
        try {
            element.clear();
            element.sendKeys(texto);
        }catch(InvalidElementStateException e){
            Actions performAct = new Actions(webDriverInstance);
            performAct.sendKeys(element, texto).build().perform();
        }catch(Exception e){
            js("arguments[0].value='"+ texto +"';", element);
        }
    }

    /**
     * Preenche um Campo Auto-Complete
     *
     * @param locator locator do campo
     * @param texto   Texto que ira preencher o campo auto complete
     */
    public void preencherCampoAutoComplete(By locator, String texto) {
        log.debug("Preenche qualquer campo Auto Complete....");
        AssertPage assertPage = new AssertPage();
        assertPage.assertPage();

        clicar(locator);
        preencherCampo(locator, texto, Keys.DOWN, Keys.ENTER);
    }

    /**
     * Preenche um Campo Auto-Complete
     *
     * @param nomeCampo Nome do campo
     * @param count     Quantidade de vezes que será pressionado para baixo
     */
    public void selecionarCampoAutoComplete(String nomeCampo, Integer count) {
        log.debug("preenche campo de auto complete aleatório...");
        selecionarCampoAutoComplete(By.name(nomeCampo), count);
    }

    /**
     * Preenche um Campo Auto-Complete
     *
     * @param locator locator
     * @param count   Quantidade de vezes que será pressionado para baixo
     */
    public void selecionarCampoAutoComplete(By locator, Integer count) {
        log.debug("preenche campo de auto complete aleatório...");
        AssertPage assertPage = new AssertPage();
        assertPage.assertPage();

        clicar(locator);

        for (int i = 0; count > i; i++)
            preencherCampo(locator, null, Keys.DOWN);

        preencherCampo(locator, null, Keys.ENTER);
    }

    /**
     * Veririca se esta apacendo o elemento
     *
     * @param locator localizador do elemento
     * @return se estiver aparecendo o elemento, retorna verdadeiro
     */
    protected Boolean isDisplayed(By locator) {
        log.debug("Verirficando se esta mostrando na tela");
        setWait(locator);
        WebElement element = checkElement(locator);

        return (element != null) ?
                element.isDisplayed() : getElement(locator).isDisplayed();
    }

    /**
     * Veririca se o elemento esta habilitado
     *
     * @param locator localizador do elemento
     * @return se o elemento estiver habilitado, retorna verdadeiro
     */
    protected Boolean isEnabled(By locator) {
        log.debug("Verirficando se esta mostrando na tela");
        setWait(locator);
        WebElement element = checkElement(locator);

        return (element != null) ?
                element.isEnabled() : getElement(locator).isEnabled();
    }

    /**
     * Verifica o valor que está selecionado no combo
     *
     * @param locator Localizador do elemento
     * @return Texto selecioanado no combo
     */
    protected WebElement retornarElementoSelecionadoCombo(By locator) {
        log.debug("Verifica o valor que está selecionado no combo...");
        setWait(locator);

        Select select = new Select(getElement(locator));

        return select.getFirstSelectedOption();
    }

    /**
     * Maximiza a tela
     */
    protected void maximizarTela() {
        log.debug("Maximizando a tela");
        if (!Data.get("browser").contains("headless"))
            webDriverInstance.manage().window().maximize();
    }

    /**
     * Deleta cookies
     */
    public void deletarCookies() {
        log.debug("Deletando cookies");
        if (!Data.get("browser").contains("headless"))
            webDriverInstance.manage().deleteAllCookies();
    }

    /**
     * move o mouse para o locator indicado
     *
     * @param locator localizador do WebElement
     */
    protected void mouseMove(By locator) {
        Actions action = new Actions(webDriverInstance);
        action.moveToElement(getElement(locator)).perform();
    }

    /**
     * Sobrecarga.. quando existe mais de um frame na página, utilizar este método para selecionar algum especifico;
     *
     * @param index Qual o index do Frame que precisa.
     */
    protected void trocarFrame(int index) {
        log.debug("Realiza a troca de frame por nome de Frame...");
        setWait(By.tagName("iframe"));
        List<WebElement> frames = getElements(By.tagName("iframe"));
        webDriverInstance.switchTo().frame(frames.get(index));
    }

    /**
     * verifica se tem alert na tela.
     *
     * @return boolean
     */
    @NotNull
    protected Boolean checkAlert() {
        log.debug("verificando se tem alert na tela...");
        return (alertIsPresent().apply(webDriverInstance) != null);
    }

    /**
     * Verifica se a lista de elementos esta visivel na tela
     *
     * @param locator localizador do tipo By
     * @return Retorna a listasta verificada de elemento aparetir do localizador
     */
    @NotNull
    protected List<WebElement> checkElements(By locator) {
        log.debug("Verificando uma lista de elemento...");

        setWait(() -> presenceOfAllElementsLocatedBy(locator));
        List<WebElement> listaElementos = getElements(locator);

        for (WebElement element : listaElementos) {
            if (visibilityOf(element))
                highlightElement(element);
        }
        return listaElementos;
    }

    /**
     * Localiza e evidencia o elemento
     *
     * @param locator localizador do elemento
     * @return elemento que passar pelas condições
     */
    protected WebElement checkElement(By locator) {
        log.debug("verificando os elementos passador por locator ou por lista de elemento...");

        setWait(() -> presenceOfAllElementsLocatedBy(locator));
        WebElement element = getElement(locator);

        if (visibilityOf(element))
            highlightElement(element);

        return element;
    }

    /**
     * Preenche um campo
     *
     * @param locator locator do elemento
     * @param texto   texto para ser preenchido
     * @param element elemento que deve ser preenchido
     * @param keyss   se precisar fazer um comando, mandar um elemento do tipo KEYS
     */
    private void privatePreencherCampo(By locator, String texto, WebElement element, Keys[] keyss) {
        int cont = 0;
        List<String> stringList = new ArrayList<>();

        if (texto != null) {
            while (cont != texto.length())
                stringList.add(texto.substring(cont, cont++ + 1));
        }

        if (element != null) {
            if (keyss != null) {
                if (texto != null)
                    preencherCampo(element, texto);

                for (Keys keys : keyss) {
                    setWait(500); // sem este Wait 'atropela' o auto-complete
                    try{
                        element.sendKeys(keys);
                    }catch(Exception e){
                        Actions performAct = new Actions(webDriverInstance);
                        performAct.sendKeys(element, keys).build().perform();
                    }
                }

            } else if (texto != null)
                preencherCampo(element, texto);

        } else
            log.error(String.format("Elemento não apresentado na tela... %s", locator));
    }

    /**
     * Clicando no elemento por Driver ou por JS se não der certo por Driver
     *
     * @param locator Locator do elemento
     * @param element Elemento retirado do Locator
     */
    private void clicar(By locator, WebElement element) {
        if (element != null)
            element.click();
        else checkElement(locator).click();
    }

    /**
     * Verifica se o Combo tem todos os parametros de dentro dele ja carregados;
     *
     * @param locator Localizador do elemento
     */
    private Integer verificarCombo(By locator, WebElement element) {
        log.debug("Verificando combo...");
        Integer tamanho = 0;

        setWait(locator);

        Select combo = new Select(element);

        while (!Objects.equals(tamanho, combo.getOptions().size())) {
            setWait();
            tamanho = combo.getOptions().size();
        }
        return tamanho;
    }

    /**
     * Faz highlight no elemento
     *
     * @param element elemento
     */
    private void highlightElement(WebElement element) {
        log.debug("Fazendo highlight no elemento...");
        js("arguments[0].style.border='3px solid " + Data.get("color") + "'", element);
    }
    //--------------------------------- END OF OTHER METHODS ------------------------------------
}