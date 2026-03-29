package br.com.ucsal.olimpiadas.repository.memory;

import br.com.ucsal.olimpiadas.Tentativa;
import br.com.ucsal.olimpiadas.repository.TentativaRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTentativaRepository implements TentativaRepository {

    private final List<Tentativa> tentativas = new ArrayList<>();

    @Override
    public Tentativa save(Tentativa tentativa) {
        tentativas.add(tentativa);
        return tentativa;
    }

    @Override
    public List<Tentativa> findAll() {
        return List.copyOf(tentativas);
    }
}
