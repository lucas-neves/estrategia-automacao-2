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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * @author lucasns
 * @since #1.0
 */
@Component
@ConfigurationProperties(prefix = "pacotecursos")
public class PacoteDeCursos extends BaseForPages {
    //--------------------------------- OTHER METHODS ------------------------------------
    public void validarPagina(String termo) {
        log.debug("validarPagina");
        if (Wait.setWait(5000, () -> Wait.urlContains("/cursosPor"))) {
            Assert.assertEquals("O título da página não está em conformidade",
                    termo, checkElement(By.className("section-header-title")).getText());
        }
    }

    public void selecionarItemAleatorio() {
        log.debug("selecionarItemAleatorio");
        String categoria = buscarCategoriaAtiva();
        String cssSelector = String.format("div[data-type='%s'] .js-card-prod-container section", categoria);
        List<WebElement> resultados = checkElements(By.cssSelector(cssSelector));
        Random r = new Random();
        WebElement e = resultados.get(r.nextInt(resultados.size()));

        String titulo = e.findElement(By.cssSelector(".card-prod-title a")).getText();
        String preco = e.findElement(By.className("card-prod-price")).getText();
        salvarTituloItem(titulo);
        salvarPrecoItem(preco);

        e.findElement(By.className("card-prod-details")).click();
    }

    private String buscarCategoriaAtiva() {
        log.debug("buscarCategoriaAtiva");
        List<WebElement> categorias = checkElements(By.cssSelector(".cur-categories > button"));
        Optional<WebElement> ativo = categorias.stream().filter(e -> e.getAttribute("class").contains("is-active")).findFirst();
        return ativo.map(element -> element.getAttribute("data-type")).orElse(null);
    }

    private void salvarTituloItem(String titulo) {
        log.debug("salvarTituloItem");
        Data.set("tituloCurso", titulo);
    }

    private void salvarPrecoItem(String preco) {
        log.debug("salvarPrecoItem");
        String[] array = preco
                .replace(",", ".")
                .replaceAll("[cursos em até|de|R$]", "").split("x");
        Float[] valores = Arrays.stream(array).map(Float::valueOf).toArray(Float[]::new);
        float qtdParcelas = valores[0];
        float parcela = valores[1];
        float total = qtdParcelas * parcela;
        Data.set("valorTotalCurso", Float.toString(total));
    }

    //--------------------------------- END OF OTHER METHODS ------------------------------------
}