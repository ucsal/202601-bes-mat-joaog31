package br.com.ucsal.olimpiadas.repository.memory;

import br.com.ucsal.olimpiadas.Questao;
import br.com.ucsal.olimpiadas.repository.QuestaoRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryQuestaoRepository implements QuestaoRepository {

    private final List<Questao> questoes = new ArrayList<>();

    @Override
    public Questao save(Questao questao) {
        questoes.add(questao);
        return questao;
    }

    @Override
    public List<Questao> findAll() {
        return List.copyOf(questoes);
    }

    @Override
    public List<Questao> findByProvaId(long provaId) {
        return questoes.stream().filter(questao -> questao.getProvaId() == provaId).toList();
    }
}
