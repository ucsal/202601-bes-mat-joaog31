package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.Questao;

import java.util.List;

public interface QuestaoRepository {
    Questao save(Questao questao);

    List<Questao> findAll();

    List<Questao> findByProvaId(long provaId);
}
