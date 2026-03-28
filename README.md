# Refatoracao SOLID - Olimpiadas

## 1. Objetivo do trabalho
Este projeto foi refatorado para aplicar os principios SOLID no que era possivel dentro do escopo de uma aplicacao console Java com persistencia em memoria.

O comportamento funcional principal foi preservado:
- cadastrar participante;
- cadastrar prova;
- cadastrar questao;
- aplicar prova;
- listar tentativas.

## 2. Problema da versao inicial
Antes da refatoracao, a classe `App` concentrava responsabilidades de:
- entrada e saida de console;
- regras de negocio;
- armazenamento em memoria;
- geracao de IDs;
- seed de dados iniciais;
- formatacao do tabuleiro FEN.

Isso gerava alto acoplamento, baixa coesao e dificultava testes automatizados.

## 3. Arquitetura apos refatoracao
A estrutura foi separada em camadas:

- `App`:
  - camada de interface (menu e interacao com o usuario).
- `service`:
  - `CadastroService`: regras de cadastro e validacoes;
  - `TentativaService`: regras de aplicacao/correcao e nota;
  - `SeedService`: carga inicial de dados;
  - `IdGeneratorService`: geracao de IDs.
- `repository`:
  - interfaces (`ParticipanteRepository`, `ProvaRepository`, `QuestaoRepository`, `TentativaRepository`);
  - implementacoes em memoria no pacote `repository.memory`.
- `ui`:
  - `FenBoardPrinter`: impressao do tabuleiro a partir de FEN.
- `model` (classes ja existentes no pacote raiz):
  - `Participante`, `Prova`, `Questao`, `Resposta`, `Tentativa`.

## 4. Aplicacao dos principios SOLID

### S - Single Responsibility Principle (SRP)
Cada classe passou a ter um motivo claro para mudar:
- `App` apenas coordena I/O de console;
- servicos concentram regras de negocio;
- repositorios concentram acesso a dados;
- `FenBoardPrinter` concentra formatacao de FEN;
- `SeedService` concentra dados iniciais.

### O - Open/Closed Principle (OCP)
Com interfaces de repositorio, o sistema fica aberto para extensao sem alterar regras centrais:
- e possivel criar `Jdbc...Repository` ou `Jpa...Repository` no futuro;
- `CadastroService` e `TentativaService` continuam os mesmos.

### L - Liskov Substitution Principle (LSP)
As implementacoes em memoria podem substituir os contratos de repositorio sem quebrar os servicos, respeitando a mesma semantica esperada.

### I - Interface Segregation Principle (ISP)
Foram criadas interfaces pequenas e especificas por agregado:
- cada repositorio expoe somente operacoes necessarias ao seu dominio.

### D - Dependency Inversion Principle (DIP)
Servicos dependem de abstracoes (interfaces de repositorio), nao de implementacoes concretas.
As implementacoes concretas sao injetadas na composicao da aplicacao (`App`).

## 5. Melhorias de testabilidade
Foram adicionados testes de unidade focados nos servicos:
- `CadastroServiceTest`:
  - valida ID sequencial de participante;
  - valida regra de nome obrigatorio;
  - valida cadastro de questao vinculada a prova existente.
- `TentativaServiceTest`:
  - valida correcao de respostas e calculo da nota.

Com isso, regras de negocio podem ser testadas sem depender da interface de console.

## 6. Arquivos criados e alterados

### Arquivos alterados
- `src/main/java/br/com/ucsal/olimpiadas/App.java`

### Arquivos criados
- `src/main/java/br/com/ucsal/olimpiadas/repository/ParticipanteRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/ProvaRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/QuestaoRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/TentativaRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryParticipanteRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryProvaRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryQuestaoRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryTentativaRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/service/IdGeneratorService.java`
- `src/main/java/br/com/ucsal/olimpiadas/service/CadastroService.java`
- `src/main/java/br/com/ucsal/olimpiadas/service/TentativaService.java`
- `src/main/java/br/com/ucsal/olimpiadas/service/SeedService.java`
- `src/main/java/br/com/ucsal/olimpiadas/ui/FenBoardPrinter.java`
- `src/test/java/br/com/ucsal/olimpiadas/CadastroServiceTest.java`
- `src/test/java/br/com/ucsal/olimpiadas/TentativaServiceTest.java`
- `README.md`

## 7. Historico detalhado de commits
Abaixo esta o agrupamento de commits utilizado nesta refatoracao.

### Commit 1 - feat(repository): extrai contratos e repositorios em memoria
Inclui interfaces de repositorio e implementacoes in-memory para desacoplar regras de negocio da forma de armazenamento.

### Commit 2 - feat(service): cria camada de servicos e extrai regras da App
Inclui `CadastroService`, `TentativaService`, `SeedService` e `IdGeneratorService`.

### Commit 3 - refactor(app): App vira camada de interface de console
Remove estado global e delega casos de uso para os servicos.

### Commit 4 - test(service): adiciona testes unitarios para regras de negocio
Inclui testes de cadastro, validacoes e calculo de nota.

### Commit 5 - docs(readme): documenta arquitetura e aplicacao SOLID
Inclui explicacao completa para apresentacao academica.

## 8. Como executar
### Compilar e testar
```bash
mvn test
```

### Executar aplicacao
```bash
mvn exec:java -Dexec.mainClass="br.com.ucsal.olimpiadas.App"
```

## 9. Observacoes sobre ambiente
Durante esta sessao, a execucao do Maven falhou por falta de JDK (ambiente com JRE), portanto a validacao local completa depende de configurar JDK compativel com o `pom.xml`.
