# Automação Estratégia!
Testando a qualidade das features **busca**, **listagem** e **detalhamento** de cursos catalogados.

## BDD
Automação baseada em BDD usando:
- Intellij Community (free)
- Java 8 (requisito mínimo para execução)
- Spring Boot 1.5.1
- Selenium Server 3.0.1
- Cucumber 1.2.5
- Hamcrest 1.3

### User Story
**Funcionalidade:** Busca
Na tela inicial do site da Estratégia
Devo escolher um mecanismo de busca (texto ou filtro)
e ser guiado para página de resultados

**Funcionalidade:** Listagem
Na tela de resultados de pesquisa por cursos
Devo verificar que o número de resultados é diferente de zero
e que os resultados contém a(s) palavra(s)-chave buscadas

**Funcionalidade:** Detalhamento
Na tela de detalhes do curso escolhido
Devo verificar se o valor apresentado do curso é igual ao valor obtido na tela de filtragem
e devo verificar se o valor parcelado do curso confere com o valor total do curso

- Executar a classe abaixo 
```
src/test/java/com/webmotors/bdd/CucumberTest.java
```

- Visualizar o resultado em: 
```
target/selenium-reports/index.html
```

