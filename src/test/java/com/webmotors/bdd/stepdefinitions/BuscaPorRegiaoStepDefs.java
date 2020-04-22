package com.webmotors.bdd.stepdefinitions;

import br.com.estrategiaconcursos.driver.WebDriverRecicle;
import br.com.estrategiaconcursos.util.Data;
import com.webmotors.bdd.TestApplication;
import com.webmotors.bdd.pageobjects.BuscaPorFiltro;
import com.webmotors.bdd.pageobjects.Curso;
import com.webmotors.bdd.pageobjects.PacoteDeCursos;
import com.webmotors.bdd.pageobjects.PaginaInicial;
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
public class BuscaPorRegiaoStepDefs {

    @Autowired
    private PaginaInicial paginaInicial;
    @Autowired
    private BuscaPorFiltro buscaPorFiltro;
    @Autowired
    private PacoteDeCursos pacoteDeCursos;
    @Autowired
    private Curso curso;

    @Given("^Eu estou na página inicial da Estratégia$")
    public void acessarPaginaInicial() throws Throwable {
        paginaInicial.iniciar();
    }

    @When("^clico no link \"([^\"]*)\"$")
    public void clicarLinkFiltro(final String filtro) throws Throwable {
        paginaInicial.clicarLinkFiltro(filtro);
    }

    @And("^sistema direciona para página de regiões$")
    public void validarPaginaRegioes() throws Throwable {
        buscaPorFiltro.validarPagina();
    }

    @And("^seleciono o estado \"([^\"]*)\"$")
    public void selecionarEstado(final String estado) throws Throwable {
        buscaPorFiltro.selecionarEstadoFiltroRegiao(estado);
    }

    @And("^valido o resultado filtrado pelo estado: \"([^\"]*)\"$")
    public void validarResultados(final String estado) throws Throwable {
        buscaPorFiltro.validarRegiaoFiltrada(estado);
        buscaPorFiltro.validarResultados();
    }

    @And("^seleciono um concurso$")
    public void selecionarConcurso() throws Throwable {
        buscaPorFiltro.selecionarItemAleatorio();
    }

    @And("^sistema direciona para página de cursos por concurso$")
    public void validarPaginaCursos() throws Throwable {
        pacoteDeCursos.validarPagina(Data.get("tituloPacote"));
    }

    @When("^seleciono um curso para ser exibido$")
    public void selecionarUmCurso() throws Throwable {
        pacoteDeCursos.selecionarItemAleatorio();
    }

    @Then("^sistema direciona para página de detalhes do curso$")
    public void validarPaginaCurso() throws Throwable {
        curso.validarPagina();
        curso.validarTituloCurso();
        curso.validarValorCurso();
    }

    @After("@test2")
    public void killBrowser(Scenario scenario){
        WebDriverRecicle.recicleWebDriver(scenario);
    }
}
