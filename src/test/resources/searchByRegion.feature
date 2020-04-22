@test2
Feature: Busca por região, listagem e detalhamento de cursos
  Automação das principais features do site da Estratégia
  realizando pesquisa de cursos pelo filtro 'Por região'

  Background:
    Given Eu estou na página inicial da Estratégia

  Scenario: Pesquisar cursos pelo filtro 'Por região'
    When clico no link "Por região"
    And sistema direciona para página de regiões
    And seleciono o estado "São Paulo"
    And valido o resultado filtrado pelo estado: "São Paulo"
    And seleciono um concurso
    And sistema direciona para página de cursos por concurso
    When seleciono um curso para ser exibido
    Then sistema direciona para página de detalhes do curso