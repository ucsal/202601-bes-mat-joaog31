package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.Participante;
import br.com.ucsal.olimpiadas.Prova;
import br.com.ucsal.olimpiadas.Questao;
import br.com.ucsal.olimpiadas.repository.ParticipanteRepository;
import br.com.ucsal.olimpiadas.repository.ProvaRepository;
import br.com.ucsal.olimpiadas.repository.QuestaoRepository;

import java.util.List;

public class CadastroService {

    private final ParticipanteRepository participanteRepository;
    private final ProvaRepository provaRepository;
    private final QuestaoRepository questaoRepository;
    private final IdGeneratorService idGeneratorService;

    public CadastroService(
            ParticipanteRepository participanteRepository,
            ProvaRepository provaRepository,
            QuestaoRepository questaoRepository,
            IdGeneratorService idGeneratorService
    ) {
        this.participanteRepository = participanteRepository;
        this.provaRepository = provaRepository;
        this.questaoRepository = questaoRepository;
        this.idGeneratorService = idGeneratorService;
    }

    public Participante cadastrarParticipante(String nome, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("nome inválido");
        }

        Participante participante = new Participante();
        participante.setId(idGeneratorService.nextParticipanteId());
        participante.setNome(nome);
        participante.setEmail(email);

        return participanteRepository.save(participante);
    }

    public Prova cadastrarProva(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("título inválido");
        }

        Prova prova = new Prova();
        prova.setId(idGeneratorService.nextProvaId());
        prova.setTitulo(titulo);

        return provaRepository.save(prova);
    }

    public Questao cadastrarQuestao(
            long provaId,
            String enunciado,
            String[] alternativas,
            char alternativaCorreta,
            String fenInicial
    ) {
        if (!provaRepository.existsById(provaId)) {
            throw new IllegalArgumentException("prova inexistente");
        }

        Questao questao = new Questao();
        questao.setId(idGeneratorService.nextQuestaoId());
        questao.setProvaId(provaId);
        questao.setEnunciado(enunciado);
        questao.setAlternativas(alternativas);
        questao.setAlternativaCorreta(alternativaCorreta);
        questao.setFenInicial(fenInicial);

        return questaoRepository.save(questao);
    }

    public List<Participante> listarParticipantes() {
        return participanteRepository.findAll();
    }

    public List<Prova> listarProvas() {
        return provaRepository.findAll();
    }

    public boolean participanteExiste(long participanteId) {
        return participanteRepository.existsById(participanteId);
    }

    public boolean provaExiste(long provaId) {
        return provaRepository.existsById(provaId);
    }
}
