package br.com.estrategiaconcursos.base;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static br.com.estrategiaconcursos.base.Wait.checkURL;

/**
 * @author lucasns
 * @since #1.0
 */
public class AssertPage {
    // -------------------------- FILDS --------------------------
    private static final Logger log = LoggerFactory.getLogger(AssertPage.class.getSimpleName());

    // -------------------------- OTHER METHODS --------------------------

    /**
     * Fazendo as verificações de página
     * Verificando se a pagina esta carregada
     * Verificando se a url ainda é a mesma
     */
    public void assertPage() {

        log.debug("Verificando se a url ainda é a mesma");
        checkURL();
    }
}