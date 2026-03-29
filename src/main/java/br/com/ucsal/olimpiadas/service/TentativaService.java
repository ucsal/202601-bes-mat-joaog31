package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.Questao;
import br.com.ucsal.olimpiadas.Resposta;
import br.com.ucsal.olimpiadas.Tentativa;
import br.com.ucsal.olimpiadas.repository.ParticipanteRepository;
import br.com.ucsal.olimpiadas.repository.ProvaRepository;
import br.com.ucsal.olimpiadas.repository.QuestaoRepository;
import br.com.ucsal.olimpiadas.repository.TentativaRepository;

import java.util.List;

public class TentativaService {

    private final ParticipanteRepository participanteRepository;
    private final ProvaRepository provaRepository;
    private final QuestaoRepository questaoRepository;
    private final TentativaRepository tentativaRepository;
    private final IdGeneratorService idGeneratorService;

    public TentativaService(
            ParticipanteRepository participanteRepository,
            ProvaRepository provaRepository,
            QuestaoRepository questaoRepository,
            TentativaRepository tentativaRepository,
            IdGeneratorService idGeneratorService
    ) {
        this.participanteRepository = participanteRepository;
        this.provaRepository = provaRepository;
        this.questaoRepository = questaoRepository;
        this.tentativaRepository = tentativaRepository;
        this.idGeneratorService = idGeneratorService;
    }

    public List<Questao> buscarQuestoesDaProva(long provaId) {
        if (!provaRepository.existsById(provaId)) {
            throw new IllegalArgumentException("prova inexistente");
        }

        return questaoRepository.findByProvaId(provaId);
    }

    public Tentativa registrarTentativa(long participanteId, long provaId, List<Resposta> respostas) {
        if (!participanteRepository.existsById(participanteId)) {
            throw new IllegalArgumentException("participante inexistente");
        }
        if (!provaRepository.existsById(provaId)) {
            throw new IllegalArgumentException("prova inexistente");
        }

        Tentativa tentativa = new Tentativa();
        tentativa.setId(idGeneratorService.nextTentativaId());
        tentativa.setParticipanteId(participanteId);
        tentativa.setProvaId(provaId);
        tentativa.getRespostas().addAll(respostas);

        return tentativaRepository.save(tentativa);
    }

    public Resposta corrigirResposta(Questao questao, char alternativaMarcada) {
        Resposta resposta = new Resposta();
        resposta.setQuestaoId(questao.getId());
        resposta.setAlternativaMarcada(alternativaMarcada);
        resposta.setCorreta(questao.isRespostaCorreta(alternativaMarcada));
        return resposta;
    }

    public int calcularNota(Tentativa tentativa) {
        int acertos = 0;
        for (Resposta resposta : tentativa.getRespostas()) {
            if (resposta.isCorreta()) {
                acertos++;
            }
        }
        return acertos;
    }

    public List<Tentativa> listarTentativas() {
        return tentativaRepository.findAll();
    }
}
