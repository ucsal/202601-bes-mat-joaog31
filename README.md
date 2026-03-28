# Refatoracao SOLID - Olimpiadas

## Objetivo
Documentar de forma detalhada quais principios SOLID foram aplicados na refatoracao e em quais arquivos cada principio aparece.

## Contexto da refatoracao
Na versao original, a classe `App` concentrava quase todas as responsabilidades:
- interface de console;
- validacoes de entrada;
- regras de negocio;
- persistencia em listas;
- geracao de IDs;
- carga inicial (seed);
- impressao do tabuleiro FEN.

Esse desenho gerava alto acoplamento e dificultava manutencao e testes.

## Mapa de principios SOLID por arquivo

### S - Single Responsibility Principle (SRP)
Cada classe passou a ter uma unica responsabilidade principal.

Arquivos e aplicacao:

1. `src/main/java/br/com/ucsal/olimpiadas/App.java`
- Responsabilidade: apenas fluxo de menu e interacao com usuario.
- O que foi removido da App: regras de negocio, seed, geracao de ID e detalhes de persistencia.

2. `src/main/java/br/com/ucsal/olimpiadas/service/CadastroService.java`
- Responsabilidade: casos de uso de cadastro (participante, prova, questao) e validacoes relacionadas.

3. `src/main/java/br/com/ucsal/olimpiadas/service/TentativaService.java`
- Responsabilidade: casos de uso de aplicacao da prova (buscar questoes, corrigir resposta, registrar tentativa, calcular nota).

4. `src/main/java/br/com/ucsal/olimpiadas/service/SeedService.java`
- Responsabilidade: carga de dados iniciais.

5. `src/main/java/br/com/ucsal/olimpiadas/service/IdGeneratorService.java`
- Responsabilidade: gerar IDs sequenciais para entidades.

6. `src/main/java/br/com/ucsal/olimpiadas/ui/FenBoardPrinter.java`
- Responsabilidade: transformar FEN em representacao textual do tabuleiro no console.

7. Repositorios em `src/main/java/br/com/ucsal/olimpiadas/repository/*`
- Responsabilidade: contrato de acesso a dados, sem regra de negocio.

8. Implementacoes em memoria em `src/main/java/br/com/ucsal/olimpiadas/repository/memory/*`
- Responsabilidade: estrategia de armazenamento em memoria.

Resultado SRP:
- classes menores, mais coesas, com motivo de mudanca mais claro.

### O - Open/Closed Principle (OCP)
O sistema foi aberto para extensao e fechado para modificacao no nucleo de regras.

Arquivos e aplicacao:

1. `src/main/java/br/com/ucsal/olimpiadas/repository/ParticipanteRepository.java`
2. `src/main/java/br/com/ucsal/olimpiadas/repository/ProvaRepository.java`
3. `src/main/java/br/com/ucsal/olimpiadas/repository/QuestaoRepository.java`
4. `src/main/java/br/com/ucsal/olimpiadas/repository/TentativaRepository.java`

Como OCP foi aplicado:
- os servicos usam interfaces de repositorio;
- novas formas de persistencia (ex.: banco relacional, arquivo) podem ser adicionadas criando novas implementacoes sem alterar `CadastroService` e `TentativaService`.

Implementacao atual usada como extensao:
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryParticipanteRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryProvaRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryQuestaoRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryTentativaRepository.java`

### L - Liskov Substitution Principle (LSP)
As implementacoes concretas podem substituir suas abstrações sem alterar comportamento esperado dos servicos.

Arquivos e aplicacao:

1. Contratos:
- `src/main/java/br/com/ucsal/olimpiadas/repository/ParticipanteRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/ProvaRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/QuestaoRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/TentativaRepository.java`

2. Substitutos concretos:
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryParticipanteRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryProvaRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryQuestaoRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryTentativaRepository.java`

3. Classes consumidoras:
- `src/main/java/br/com/ucsal/olimpiadas/service/CadastroService.java`
- `src/main/java/br/com/ucsal/olimpiadas/service/TentativaService.java`

Como LSP foi aplicado:
- `CadastroService` e `TentativaService` nao dependem de detalhes internos das implementacoes;
- qualquer classe que respeite a interface pode substituir a implementacao atual.

### I - Interface Segregation Principle (ISP)
Foram criadas interfaces pequenas e especificas ao inves de uma interface unica grande.

Arquivos e aplicacao:

1. `src/main/java/br/com/ucsal/olimpiadas/repository/ParticipanteRepository.java`
- Metodos apenas para operacoes de participante.

2. `src/main/java/br/com/ucsal/olimpiadas/repository/ProvaRepository.java`
- Metodos apenas para operacoes de prova.

3. `src/main/java/br/com/ucsal/olimpiadas/repository/QuestaoRepository.java`
- Metodos apenas para operacoes de questao (incluindo busca por prova).

4. `src/main/java/br/com/ucsal/olimpiadas/repository/TentativaRepository.java`
- Metodos apenas para operacoes de tentativa.

Como ISP foi aplicado:
- consumidores dependem so do que usam;
- evita obrigar implementacoes a suportar metodos que nao fazem sentido para aquele agregado.

### D - Dependency Inversion Principle (DIP)
As regras de alto nivel dependem de abstrações, nao de detalhes concretos.

Arquivos e aplicacao:

1. Alto nivel (regras):
- `src/main/java/br/com/ucsal/olimpiadas/service/CadastroService.java`
- `src/main/java/br/com/ucsal/olimpiadas/service/TentativaService.java`

2. Abstracoes:
- interfaces em `src/main/java/br/com/ucsal/olimpiadas/repository/*`

3. Detalhes concretos:
- implementacoes em `src/main/java/br/com/ucsal/olimpiadas/repository/memory/*`

4. Composicao das dependencias:
- `src/main/java/br/com/ucsal/olimpiadas/App.java`

Como DIP foi aplicado:
- `App` instancia implementacoes concretas e injeta nos servicos;
- servicos seguem independentes da tecnologia de persistencia.

## Evidencias de melhoria apos SOLID

1. Menor acoplamento
- Regras de negocio nao ficam mais presas ao console nem a listas estaticas.

2. Maior testabilidade
- Testes focam casos de uso sem depender da interface de entrada:
  - `src/test/java/br/com/ucsal/olimpiadas/CadastroServiceTest.java`
  - `src/test/java/br/com/ucsal/olimpiadas/TentativaServiceTest.java`

3. Maior extensibilidade
- Troca de persistencia pode ser feita por nova implementacao de repositorio.

## Lista consolidada de arquivos SOLID

Camada de interface:
- `src/main/java/br/com/ucsal/olimpiadas/App.java`

Camada de servicos:
- `src/main/java/br/com/ucsal/olimpiadas/service/CadastroService.java`
- `src/main/java/br/com/ucsal/olimpiadas/service/TentativaService.java`
- `src/main/java/br/com/ucsal/olimpiadas/service/SeedService.java`
- `src/main/java/br/com/ucsal/olimpiadas/service/IdGeneratorService.java`

Camada de repositorios (abstracoes):
- `src/main/java/br/com/ucsal/olimpiadas/repository/ParticipanteRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/ProvaRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/QuestaoRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/TentativaRepository.java`

Camada de repositorios (detalhes in-memory):
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryParticipanteRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryProvaRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryQuestaoRepository.java`
- `src/main/java/br/com/ucsal/olimpiadas/repository/memory/InMemoryTentativaRepository.java`

Utilitario de UI:
- `src/main/java/br/com/ucsal/olimpiadas/ui/FenBoardPrinter.java`

Testes de regra de negocio:
- `src/test/java/br/com/ucsal/olimpiadas/CadastroServiceTest.java`
- `src/test/java/br/com/ucsal/olimpiadas/TentativaServiceTest.java`
