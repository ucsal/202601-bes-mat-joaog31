package br.com.ucsal.olimpiadas.service;

public class IdGeneratorService {

    private long proximoParticipanteId = 1;
    private long proximaProvaId = 1;
    private long proximaQuestaoId = 1;
    private long proximaTentativaId = 1;

    public long nextParticipanteId() {
        return proximoParticipanteId++;
    }

    public long nextProvaId() {
        return proximaProvaId++;
    }

    public long nextQuestaoId() {
        return proximaQuestaoId++;
    }

    public long nextTentativaId() {
        return proximaTentativaId++;
    }
}
