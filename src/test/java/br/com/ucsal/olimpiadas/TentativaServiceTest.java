package br.com.ucsal.olimpiadas;

import br.com.ucsal.olimpiadas.repository.memory.InMemoryParticipanteRepository;
import br.com.ucsal.olimpiadas.repository.memory.InMemoryProvaRepository;
import br.com.ucsal.olimpiadas.repository.memory.InMemoryQuestaoRepository;
import br.com.ucsal.olimpiadas.repository.memory.InMemoryTentativaRepository;
import br.com.ucsal.olimpiadas.service.CadastroService;
import br.com.ucsal.olimpiadas.service.IdGeneratorService;
import br.com.ucsal.olimpiadas.service.TentativaService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TentativaServiceTest {

    @Test
    void deveCalcularNotaComBaseNasRespostasCorretas() {
        InMemoryParticipanteRepository participanteRepository = new InMemoryParticipanteRepository();
        InMemoryProvaRepository provaRepository = new InMemoryProvaRepository();
        InMemoryQuestaoRepository questaoRepository = new InMemoryQuestaoRepository();
        InMemoryTentativaRepository tentativaRepository = new InMemoryTentativaRepository();
        IdGeneratorService idGeneratorService = new IdGeneratorService();

        CadastroService cadastroService = new CadastroService(
                participanteRepository,
                provaRepository,
                questaoRepository,
                idGeneratorService
        );

        TentativaService tentativaService = new TentativaService(
                participanteRepository,
                provaRepository,
                questaoRepository,
                tentativaRepository,
                idGeneratorService
        );

        Participante participante = cadastroService.cadastrarParticipante("Ana", "ana@email.com");
        Prova prova = cadastroService.cadastrarProva("Prova A");

        Questao q1 = cadastroService.cadastrarQuestao(
                prova.getId(),
                "Q1",
                new String[]{"A) 1", "B) 2", "C) 3", "D) 4", "E) 5"},
                'A',
                null
        );

        Questao q2 = cadastroService.cadastrarQuestao(
                prova.getId(),
                "Q2",
                new String[]{"A) 1", "B) 2", "C) 3", "D) 4", "E) 5"},
                'B',
                null
        );

        Resposta resposta1 = tentativaService.corrigirResposta(q1, 'A');
        Resposta resposta2 = tentativaService.corrigirResposta(q2, 'C');

        Tentativa tentativa = tentativaService.registrarTentativa(
                participante.getId(),
                prova.getId(),
                List.of(resposta1, resposta2)
        );

        int nota = tentativaService.calcularNota(tentativa);

        assertEquals(1, nota);
        assertEquals(2, tentativa.getRespostas().size());
    }
}
