package br.com.ucsal.olimpiadas.repository.memory;

import br.com.ucsal.olimpiadas.Participante;
import br.com.ucsal.olimpiadas.repository.ParticipanteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryParticipanteRepository implements ParticipanteRepository {

    private final List<Participante> participantes = new ArrayList<>();

    @Override
    public Participante save(Participante participante) {
        participantes.add(participante);
        return participante;
    }

    @Override
    public List<Participante> findAll() {
        return List.copyOf(participantes);
    }

    @Override
    public Optional<Participante> findById(long id) {
        return participantes.stream().filter(participante -> participante.getId() == id).findFirst();
    }

    @Override
    public boolean existsById(long id) {
        return participantes.stream().anyMatch(participante -> participante.getId() == id);
    }
}
