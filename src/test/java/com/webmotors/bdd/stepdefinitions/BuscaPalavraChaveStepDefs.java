package com.webmotors.bdd.stepdefinitions;

import br.com.estrategiaconcursos.driver.WebDriverRecicle;
import com.webmotors.bdd.TestApplication;
import com.webmotors.bdd.pageobjects.BuscaPorTexto;
import com.webmotors.bdd.pageobjects.Curso;
import com.webmotors.bdd.pageobjects.PaginaInicial;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.When;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

/**
 * @author lucasns
 * @since #1.0
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class)
@SpringBootTest
public class BuscaPalavraChaveStepDefs {

    @Autowired
    private PaginaInicial paginaInicial;
    @Autowired
    private BuscaPorTexto buscaPorTexto;
    @Autowired
    private Curso curso;

    @Given("^Eu estou na página inicial$")
    public void acessarPaginaInicial() throws Throwable {
        paginaInicial.iniciar();
    }

    @When("^preencho o campo de pesquisa de cursos com \"([^\"]*)\" e pesquiso$")
    public void buscarPalavraChave(final String palavraChave) throws Throwable {
        paginaInicial.preencherCampoBusca(palavraChave);
        paginaInicial.clicarBotaoPesquisar();
    }

    @And("^sistema direciona para página de pesquisa do termo \"([^\"]*)\"$")
    public void validarPaginaResultados(final String palavraChave) throws Throwable {
        buscaPorTexto.validarPagina(palavraChave);
    }

    @And("^valido o conteúdo dos resultados$")
    public void validarResultados() throws Throwable {
        buscaPorTexto.validarResultados();
    }

    @When("^seleciono um item para ser detalhado$")
    public void selecionarDetalharItem() throws Throwable {
        buscaPorTexto.selecionarItemAleatorio();
    }

    @Then("^sistema direciona para página de detalhamento do curso$")
    public void validarPaginaCurso() throws Throwable {
        curso.validarPagina();
        curso.validarTituloCurso();
        curso.validarValorCurso();
    }

    @After("@test1")
    public void killBrowser(Scenario scenario){
        WebDriverRecicle.recicleWebDriver(scenario);
    }
}
