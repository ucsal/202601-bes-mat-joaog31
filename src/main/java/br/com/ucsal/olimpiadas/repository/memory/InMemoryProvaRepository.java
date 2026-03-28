package br.com.ucsal.olimpiadas.repository.memory;

import br.com.ucsal.olimpiadas.Prova;
import br.com.ucsal.olimpiadas.repository.ProvaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryProvaRepository implements ProvaRepository {

    private final List<Prova> provas = new ArrayList<>();

    @Override
    public Prova save(Prova prova) {
        provas.add(prova);
        return prova;
    }

    @Override
    public List<Prova> findAll() {
        return List.copyOf(provas);
    }

    @Override
    public Optional<Prova> findById(long id) {
        return provas.stream().filter(prova -> prova.getId() == id).findFirst();
    }

    @Override
    public boolean existsById(long id) {
        return provas.stream().anyMatch(prova -> prova.getId() == id);
    }
}
