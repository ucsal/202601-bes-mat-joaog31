package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.Prova;

import java.util.List;
import java.util.Optional;

public interface ProvaRepository {
    Prova save(Prova prova);

    List<Prova> findAll();

    Optional<Prova> findById(long id);

    boolean existsById(long id);
}
