package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.Tentativa;

import java.util.List;

public interface TentativaRepository {
    Tentativa save(Tentativa tentativa);

    List<Tentativa> findAll();
}
