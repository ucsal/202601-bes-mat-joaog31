package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.Participante;

import java.util.List;
import java.util.Optional;

public interface ParticipanteRepository {
    Participante save(Participante participante);

    List<Participante> findAll();

    Optional<Participante> findById(long id);

    boolean existsById(long id);
}
