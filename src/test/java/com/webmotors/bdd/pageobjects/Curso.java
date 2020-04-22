package com.webmotors.bdd.pageobjects;

import br.com.estrategiaconcursos.base.BaseForPages;
import br.com.estrategiaconcursos.base.Wait;
import br.com.estrategiaconcursos.util.Data;
import br.com.estrategiaconcursos.util.NumberUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lucasns
 * @since #1.0
 */
@Component
@ConfigurationProperties(prefix = "curso")
public class Curso extends BaseForPages {
    //--------------------------------- OTHER METHODS ------------------------------------
    public void validarPagina() {
        log.debug("validarPagina");
        if (Wait.setWait(() -> Wait.visibilityOfElementLocated(By.className("page-header-title")))) {
            Assert.assertEquals("O campo de busca não está aparecendo", "Cursos por concurso", getText(By.className("page-header-title")));
        }
    }
    public void validarTituloCurso() {
        log.debug("validarTituloCurso");
        String titulo = checkElement(By.className("cur-details-info-title")).getText();
        Assert.assertTrue("O título do curso está divergente", titulo.contains(Data.get("tituloCurso")));
    }

    public void validarValorCurso() {
        log.debug("validarValorCurso");
        String valor = checkElement(By.className("value")).getText();
        valor = NumberUtils.extrairNumeros(valor).get(0).toString();
        Assert.assertEquals("O valor do curso está divergente", Data.get("valorTotalCurso"), valor);
    }
    //--------------------------------- END OF OTHER METHODS ------------------------------------
}