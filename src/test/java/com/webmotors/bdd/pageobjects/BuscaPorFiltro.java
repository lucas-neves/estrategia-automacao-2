package com.webmotors.bdd.pageobjects;

import br.com.estrategiaconcursos.base.BaseForPages;
import br.com.estrategiaconcursos.base.Wait;
import br.com.estrategiaconcursos.util.Data;
import br.com.estrategiaconcursos.util.NumberUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * @author lucasns
 * @since #1.0
 */
@Component
@ConfigurationProperties(prefix = "buscafiltro")
public class BuscaPorFiltro extends BaseForPages {
    //--------------------------------- OTHER METHODS ------------------------------------
    public void validarPagina() {
        log.debug("validarPagina");
        if (Wait.setWait(5000, () -> Wait.urlContains("/cursos/"))) {
            Assert.assertTrue("O título do filtro não está visível", isDisplayed(By.className("page-header-title")));
        }
    }

    public void validarResultados() {
        log.debug("validarResultados");
        List<WebElement> resultadosLista = checkElements(By.cssSelector(".page-result-list section"));
        Assert.assertTrue("A lista de resultados não foi carregada", resultadosLista.size() > 0);
    }

    public void filtrarResultados(String termo) {
        log.debug("filtrarResultados");
        Wait.setWait(5000);
        preencherCampo(By.cssSelector(".search input"), termo, Keys.ENTER);
    }

    public void validarFiltro(String termo) {
        log.debug("validarFiltro");
        List<WebElement> resultados = checkElements(By.cssSelector(".page-result-list section"));
        boolean correto = resultados.stream()
                .allMatch(e -> e.findElement(By.cssSelector(".card-prod-title a")).getText().contains(termo));
        Assert.assertTrue("Os resultados não contém o termo buscado", correto);
    }

    public void clicarPrimeiroItem() {
        log.debug("clicarPrimeiroItem");
        List<WebElement> resultados = checkElements(By.cssSelector(".page-result-list section"));
        resultados.get(0).findElement(By.className("card-prod-details")).click();
    }

    public void selecionarItemAleatorio() {
        log.debug("selecionarItemAleatorio");
        List<WebElement> resultados = checkElements(By.cssSelector(".page-result-list section"));

        WebElement e;
        Random r = new Random();
        double qtdDisponivel;
        do {
            e = resultados.get(r.nextInt(resultados.size()));
            qtdDisponivel = NumberUtils.extrairNumeros(e.findElement(By.className("card-prod-available")).getText()).get(0);

        } while (qtdDisponivel == 0);
        String titulo = e.findElement(By.cssSelector(".card-prod-title a")).getText();
        Data.set("tituloPacote", titulo);
        e.findElement(By.className("card-prod-details")).click();
    }

    //--------------------------------- FILTRO REGIÃO ------------------------------------
    public void selecionarEstadoFiltroRegiao(String estado) {
        log.debug("selecionarEstadoFiltroRegiao");
        List<WebElement> estados = checkElements(By.cssSelector(".regions-list > section li > a"));
        Optional<WebElement> linkEstado = estados.stream().filter(e -> e.getText().contains(estado)).findAny();
        linkEstado.ifPresent(WebElement::click);
    }

    public void validarRegiaoFiltrada(String estado) {
        log.debug("validarRegiaoFiltrada");
        String estadoText = checkElement(By.cssSelector("section[data-url$='?estado=SP'] .section-header-title")).getText();
        Assert.assertTrue("A lista de resultados não foi carregada", estadoText.contains(estado));
    }

    //--------------------------------- END OF OTHER METHODS ------------------------------------
}