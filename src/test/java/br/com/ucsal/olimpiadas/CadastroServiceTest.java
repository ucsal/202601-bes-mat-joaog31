package br.com.ucsal.olimpiadas;

import br.com.ucsal.olimpiadas.repository.memory.InMemoryParticipanteRepository;
import br.com.ucsal.olimpiadas.repository.memory.InMemoryProvaRepository;
import br.com.ucsal.olimpiadas.repository.memory.InMemoryQuestaoRepository;
import br.com.ucsal.olimpiadas.service.CadastroService;
import br.com.ucsal.olimpiadas.service.IdGeneratorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CadastroServiceTest {

    @Test
    void deveCadastrarParticipanteComIdSequencial() {
        CadastroService cadastroService = new CadastroService(
                new InMemoryParticipanteRepository(),
                new InMemoryProvaRepository(),
                new InMemoryQuestaoRepository(),
                new IdGeneratorService()
        );

        Participante primeiro = cadastroService.cadastrarParticipante("Ana", "ana@email.com");
        Participante segundo = cadastroService.cadastrarParticipante("Bruno", "bruno@email.com");

        assertEquals(1L, primeiro.getId());
        assertEquals(2L, segundo.getId());
    }

    @Test
    void deveLancarExcecaoAoCadastrarParticipanteSemNome() {
        CadastroService cadastroService = new CadastroService(
                new InMemoryParticipanteRepository(),
                new InMemoryProvaRepository(),
                new InMemoryQuestaoRepository(),
                new IdGeneratorService()
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cadastroService.cadastrarParticipante("   ", "email@email.com")
        );

        assertEquals("nome inválido", exception.getMessage());
    }

    @Test
    void deveCadastrarQuestaoParaProvaExistente() {
        CadastroService cadastroService = new CadastroService(
                new InMemoryParticipanteRepository(),
                new InMemoryProvaRepository(),
                new InMemoryQuestaoRepository(),
                new IdGeneratorService()
        );

        Prova prova = cadastroService.cadastrarProva("Prova de Xadrez");

        Questao questao = cadastroService.cadastrarQuestao(
                prova.getId(),
                "Qual lance dá mate?",
                new String[]{"A) Qh7#", "B) Qf5#", "C) Qc8#", "D) Qh8#", "E) Qe6#"},
                'C',
                "6k1/5ppp/8/8/8/7Q/6PP/6K1 w - - 0 1"
        );

        assertEquals(prova.getId(), questao.getProvaId());
        assertEquals('C', questao.getAlternativaCorreta());
    }
}
