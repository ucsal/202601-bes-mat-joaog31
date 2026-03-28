package br.com.ucsal.olimpiadas.service;

public class SeedService {

    private final CadastroService cadastroService;

    public SeedService(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    public void carregarDadosIniciais() {
        var prova = cadastroService.cadastrarProva("Olimpíada 2026 • Nível 1 • Prova A");

        cadastroService.cadastrarQuestao(
                prova.getId(),
                """
                        Questão 1 — Mate em 1.
                        É a vez das brancas.
                        Encontre o lance que dá mate imediatamente.
                        """,
                new String[]{"A) Qh7#", "B) Qf5#", "C) Qc8#", "D) Qh8#", "E) Qe6#"},
                'C',
                "6k1/5ppp/8/8/8/7Q/6PP/6K1 w - - 0 1"
        );
    }
}
