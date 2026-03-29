package br.com.ucsal.olimpiadas.ui;

import br.com.ucsal.olimpiadas.Questao;
import br.com.ucsal.olimpiadas.service.CadastroService;

import java.util.Scanner;

public class QuestaoConsoleHandler {

    private final Scanner input;
    private final CadastroService cadastroService;
    private final SelecaoConsoleHelper selecaoConsoleHelper;

    public QuestaoConsoleHandler(
            Scanner input,
            CadastroService cadastroService,
            SelecaoConsoleHelper selecaoConsoleHelper
    ) {
        this.input = input;
        this.cadastroService = cadastroService;
        this.selecaoConsoleHelper = selecaoConsoleHelper;
    }

    public void cadastrarQuestao() {
        if (cadastroService.listarProvas().isEmpty()) {
            System.out.println("não há provas cadastradas");
            return;
        }

        var provaId = selecaoConsoleHelper.escolherProva();
        if (provaId == null) {
            return;
        }

        System.out.println("Enunciado:");
        var enunciado = input.nextLine();

        var alternativas = new String[5];
        for (int i = 0; i < 5; i++) {
            char letra = (char) ('A' + i);
            System.out.print("Alternativa " + letra + ": ");
            alternativas[i] = letra + ") " + input.nextLine();
        }

        System.out.print("Alternativa correta (A–E): ");
        char correta;
        try {
            correta = Questao.normalizar(input.nextLine().trim().charAt(0));
        } catch (Exception e) {
            System.out.println("alternativa inválida");
            return;
        }

        try {
            Questao questao = cadastroService.cadastrarQuestao(provaId, enunciado, alternativas, correta, null);
            System.out.println("Questão cadastrada: " + questao.getId() + " (na prova " + provaId + ")");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
