package com.webmotors.bdd.pageobjects;

import br.com.estrategiaconcursos.base.BaseForPages;
import br.com.estrategiaconcursos.base.Wait;
import br.com.estrategiaconcursos.driver.WebDriverFactory;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lucasns
 * @since #1.0
 */
@Component
@ConfigurationProperties(prefix = "paginainicial")
public class PaginaInicial extends BaseForPages {
    //--------------------------------- FIELDS ------------------------------------
    @Value("${app.baseurl}")
    private String baseurl;

    //--------------------------------- OTHER METHODS ------------------------------------
    public void iniciar() {
        WebDriverFactory.criarNovoWebDriver();
        acessarUrl(baseurl);

        if (Wait.setWait(() -> Wait.visibilityOfElementLocated(By.className("introducao")))) {
            Assert.assertTrue("O campo de busca não está aparecendo", isDisplayed(By.cssSelector("form > div.search-input > input")));
        }
    }

    public void clicarLinkFiltro(String filtro) {
        log.debug("clicarLinkFiltro");
        clicar(By.xpath(String.format("//a[text()='%s']", filtro)));
    }

    public void preencherCampoBusca(String palavraCHave) {
        log.debug("preencherCampoBusca");
        preencherCampo(By.cssSelector("form > div.search-input > input"), palavraCHave);
    }

    public void clicarBotaoPesquisar() {
        log.debug("clicarBotaoPesquisar");
        clicar(By.cssSelector("button.search-input-icon"));
    }
    //--------------------------------- END OF OTHER METHODS ------------------------------------
}