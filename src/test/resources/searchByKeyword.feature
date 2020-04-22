@test1
Feature: Busca por palavra-chave, listagem e detalhamento de cursos
  Automação das principais features do site da Estratégia
  realizando pesquisa de cursos por palavra-chave

  Background:
    Given Eu estou na página inicial

  Scenario: Pesquisar cursos por palavra-chave
    When preencho o campo de pesquisa de cursos com "pedagogia" e pesquiso
    And sistema direciona para página de pesquisa do termo "pedagogia"
    And valido o conteúdo dos resultados
    When seleciono um item para ser detalhado
    Then sistema direciona para página de detalhamento do curso