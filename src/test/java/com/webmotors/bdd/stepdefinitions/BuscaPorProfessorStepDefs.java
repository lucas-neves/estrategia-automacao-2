package com.webmotors.bdd.stepdefinitions;

import br.com.estrategiaconcursos.driver.WebDriverRecicle;
import com.webmotors.bdd.TestApplication;
import com.webmotors.bdd.pageobjects.*;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lucasns
 * @since #1.0
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class)
@SpringBootTest
public class BuscaPorProfessorStepDefs {

    @Autowired
    private PaginaInicial paginaInicial;
    @Autowired
    private BuscaPorFiltro buscaPorFiltro;
    @Autowired
    private PacoteDeCursos pacoteDeCursos;
    @Autowired
    private Curso curso;

    @Given("^Eu estou na página inicial do site$")
    public void acessarPaginaInicial() throws Throwable {
        paginaInicial.iniciar();
    }

    @When("^clico no link do filtro \"([^\"]*)\"$")
    public void clicarLinkFiltro(final String filtro) throws Throwable {
        paginaInicial.clicarLinkFiltro(filtro);
    }

    @And("^sistema direciona para página de resultados filtrados$")
    public void validarPaginaResultados() throws Throwable {
        buscaPorFiltro.validarPagina();
        buscaPorFiltro.validarResultados();
    }

    @And("^pesquiso pelo\\(a\\) professor\\(a\\) \"([^\"]*)\"$")
    public void pesquisarProfessor(final String professor) throws Throwable {
        buscaPorFiltro.filtrarResultados(professor);
    }

    @And("^valido o resultado filtrado: \"([^\"]*)\"$")
    public void validarFiltro(final String termo) throws Throwable {
        buscaPorFiltro.validarFiltro(termo);
    }

    @And("^acesso a página do professor$")
    public void acessarCursosDoProfessor() throws Throwable {
        buscaPorFiltro.clicarPrimeiroItem();
    }

    @And("^valido a página do professor \"([^\"]*)\"$")
    public void validarPaginaProfessor(final String professor) throws Throwable {
        pacoteDeCursos.validarPagina(professor);
    }

    @When("^seleciono um curso para ser detalhado$")
    public void selecionarUmCurso() throws Throwable {
        pacoteDeCursos.selecionarItemAleatorio();
    }

    @Then("^sistema direciona para página de detalhamento$")
    public void validarPaginaCurso() throws Throwable {
        curso.validarPagina();
        curso.validarTituloCurso();
        curso.validarValorCurso();
    }

    @After("@test3")
    public void killBrowser(Scenario scenario){
        WebDriverRecicle.recicleWebDriver(scenario);
    }
}
