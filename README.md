# Refatoracao SOLID - Olimpiadas

## 1. Resumo rapido 
Este projeto era centralizado em uma unica classe (`App`) e foi dividido em camadas para aplicar SOLID.

Hoje:
- `App` cuida so do bootstrap (montagem das dependencias) e da delegacao do menu;
- `service` cuida das regras de negocio;
- `repository` cuida do acesso a dados;
- `ui` cuida da interacao de console e exibicao.

Com a ultima refatoracao, as responsabilidades de console que estavam no `App` foram separadas em handlers:
- `src/main/java/br/com/ucsal/olimpiadas/ui/ParticipanteConsoleHandler.java`
- `src/main/java/br/com/ucsal/olimpiadas/ui/ProvaConsoleHandler.java`
- `src/main/java/br/com/ucsal/olimpiadas/ui/QuestaoConsoleHandler.java`
- `src/main/java/br/com/ucsal/olimpiadas/ui/TentativaConsoleHandler.java`
- `src/main/java/br/com/ucsal/olimpiadas/ui/SelecaoConsoleHelper.java`
- `src/main/java/br/com/ucsal/olimpiadas/ui/FenBoardPrinter.java`

## 2. Antes e depois

### Antes
- Uma classe fazia tudo.
- Alto acoplamento.
- Dificil de testar.

### Depois
- Responsabilidades separadas.
- Regras de negocio isoladas.
- Mais facil de manter e evoluir.
- `App` ficou menor e com menos acoplamento com detalhes de entrada/saida.

## 3. SOLID aplicado 

### S - Single Responsibility Principle
Ideia: cada classe deve ter uma responsabilidade.

Arquivos onde foi aplicado:
- `src/main/java/br/com/ucsal/olimpiadas/App.java` -> composicao das dependencias e delegacao do menu.
- `src/main/java/br/com/ucsal/olimpiadas/service/CadastroService.java` -> cadastro e validacoes.
- `src/main/java/br/com/ucsal/olimpiadas/service/TentativaService.java` -> aplicar prova, corrigir e calcular nota.
- `src/main/java/br/com/ucsal/olimpiadas/service/SeedService.java` -> dados iniciais.
- `src/main/java/br/com/ucsal/olimpiadas/service/IdGeneratorService.java` -> IDs.
- `src/main/java/br/com/ucsal/olimpiadas/ui/ParticipanteConsoleHandler.java` -> fluxo de cadastro de participante no console.
- `src/main/java/br/com/ucsal/olimpiadas/ui/ProvaConsoleHandler.java` -> fluxo de cadastro de prova no console.
- `src/main/java/br/com/ucsal/olimpiadas/ui/QuestaoConsoleHandler.java` -> fluxo de cadastro de questao no console.
- `src/main/java/br/com/ucsal/olimpiadas/ui/TentativaConsoleHandler.java` -> fluxo de aplicacao e listagem de tentativas.
- `src/main/java/br/com/ucsal/olimpiadas/ui/SelecaoConsoleHelper.java` -> selecao de participante/prova no console.
- `src/main/java/br/com/ucsal/olimpiadas/ui/FenBoardPrinter.java` -> impressao de FEN.


### O - Open/Closed Principle
Ideia: aberto para extensao, fechado para modificacao.

Arquivos onde foi aplicado:
- `src/main/java/br/com/ucsal/olimpiadas/repository/ParticipanteRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/ProvaRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/QuestaoRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/TentativaRepository.java`

Implementacao atual:
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryParticipanteRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryProvaRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryQuestaoRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryTentativaRepository.java`



### L - Liskov Substitution Principle
Ideia: implementacoes devem poder substituir suas interfaces sem quebrar o sistema.

Arquivos onde foi aplicado:
- Interfaces em `repository/*`.
- Implementacoes em `repository/memory/*`.
- Consumo nas regras:
  - `src/main/java/br/com/ucsal/olimpiadas/service/CadastroService.java`
  - `src/main/java/br/com/ucsal/olimpiadas/service/TentativaService.java`


### I - Interface Segregation Principle
Ideia: interfaces pequenas e especificas.

Arquivos onde foi aplicado:
- `src/main/java/br/com/ucsal/olimpiadas/repository/ParticipanteRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/ProvaRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/QuestaoRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/TentativaRepository.java`



### D - Dependency Inversion Principle
Ideia: regras de negocio dependem de abstracoes, nao de implementacoes.

Arquivos onde foi aplicado:
- Regras (alto nivel):
  - `src/main/java/br/com/ucsal/olimpiadas/service/CadastroService.java`
  - `src/main/java/br/com/ucsal/olimpiadas/service/TentativaService.java`
- Abstracoes:
  - `src/main/java/br/com/ucsal/olimpiadas/repository/*`
- Implementacoes concretas:
  - `src/main/java/br/com/ucsal/olimpiadas/repository/memory/*`
- Composicao/injecao:
  - `src/main/java/br/com/ucsal/olimpiadas/App.java`

Observacao: no `App` ainda existe composicao manual com `new` (composition root), o que e aceitavel para esse porte de projeto.


Testes adicionados:
- `src/test/java/br/com/ucsal/olimpiadas/CadastroServiceTest.java`
- `src/test/java/br/com/ucsal/olimpiadas/TentativaServiceTest.java`

Status apos refatoracao do App:
- Testes executados com sucesso: 8 passando, 0 falhas.


## 5. Nesse projeto foi usado o GithubCopilot
