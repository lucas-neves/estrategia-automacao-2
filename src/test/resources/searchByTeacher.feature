@test3
Feature: Busca por professor, listagem e detalhamento de cursos
  Automação das principais features do site da Estratégia
  realizando pesquisa de cursos pelo filtro 'Por professor'

  Background:
    Given Eu estou na página inicial do site

  Scenario: Pesquisar cursos pelo filtro 'Por professor'
    When clico no link do filtro "Por professor"
    And sistema direciona para página de resultados filtrados
    And pesquiso pelo(a) professor(a) "Ena Loiola"
    And valido o resultado filtrado: "Ena Loiola"
    And acesso a página do professor
    And valido a página do professor "Ena Loiola"
    When seleciono um curso para ser detalhado
    Then sistema direciona para página de detalhamento