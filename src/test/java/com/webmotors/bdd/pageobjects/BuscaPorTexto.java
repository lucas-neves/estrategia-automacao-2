package com.webmotors.bdd.pageobjects;

import br.com.estrategiaconcursos.base.BaseForPages;
import br.com.estrategiaconcursos.base.Wait;
import br.com.estrategiaconcursos.util.Data;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author lucasns
 * @since #1.0
 */
@Component
@ConfigurationProperties(prefix = "buscatexto")
public class BuscaPorTexto extends BaseForPages {
    //--------------------------------- FIELDS ------------------------------------
    private String palavraChave;

    //--------------------------------- OTHER METHODS ------------------------------------
    public void validarPagina(String palavraChave) {
        log.debug("validarPagina");
        this.palavraChave = palavraChave;
        if (Wait.setWait(5000, () -> Wait.urlContains("/pesquisa/"))) {
            Assert.assertTrue("O resultado da busca não está visível", isDisplayed(By.cssSelector("h1.title")));
        }
    }

    public void validarResultados() {
        log.debug("validarResultados");
        List<WebElement> resultados = checkElements(By.cssSelector(".found strong"));
        String quantidade = resultados.get(0).getText();
        String termo = resultados.get(1).getText();

        List<WebElement> resultadosLista = checkElements(By.cssSelector(".page-result-list section"));
        Assert.assertEquals("O termo buscado não está correto", palavraChave, termo.replaceAll("\"", ""));
        Assert.assertEquals("A quantidade de resultados está divergente",
                Integer.parseInt(quantidade), resultadosLista.size());
    }

    public void selecionarItemAleatorio() {
        log.debug("selecionarItemAleatorio");
        List<WebElement> resultados = checkElements(By.cssSelector(".page-result-list section"));
        Random r = new Random();
        WebElement e = resultados.get(r.nextInt(resultados.size()));

        String titulo = e.findElement(By.cssSelector(".card-prod-title a")).getText();
        String preco = e.findElement(By.className("card-prod-price")).getText();
        salvarTituloItem(titulo);
        salvarPrecoItem(preco);

        e.findElement(By.className("card-prod-details")).click();
    }

    private void salvarTituloItem(String titulo) {
        log.debug("salvarTituloItem");
        Data.set("tituloCurso", titulo);
    }

    private void salvarPrecoItem(String preco) {
        log.debug("salvarPrecoItem");
        String[] array = preco.replace(",", ".")
                .replaceAll("[|R$]", "").split("x");
        Float[] valores = Arrays.stream(array).map(Float::valueOf).toArray(Float[]::new);
        float qtdParcelas = valores[0];
        float parcela = valores[1];
        float total = qtdParcelas * parcela;
        Data.set("valorTotalCurso", Float.toString(total));
    }

    //--------------------------------- END OF OTHER METHODS ------------------------------------
}