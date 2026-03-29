package br.com.ucsal.olimpiadas.ui;

import br.com.ucsal.olimpiadas.Questao;
import br.com.ucsal.olimpiadas.Resposta;
import br.com.ucsal.olimpiadas.Tentativa;
import br.com.ucsal.olimpiadas.service.CadastroService;
import br.com.ucsal.olimpiadas.service.TentativaService;

import java.util.List;
import java.util.Scanner;

public class TentativaConsoleHandler {

    private final Scanner input;
    private final CadastroService cadastroService;
    private final TentativaService tentativaService;
    private final SelecaoConsoleHelper selecaoConsoleHelper;
    private final FenBoardPrinter fenBoardPrinter;

    public TentativaConsoleHandler(
            Scanner input,
            CadastroService cadastroService,
            TentativaService tentativaService,
            SelecaoConsoleHelper selecaoConsoleHelper,
            FenBoardPrinter fenBoardPrinter
    ) {
        this.input = input;
        this.cadastroService = cadastroService;
        this.tentativaService = tentativaService;
        this.selecaoConsoleHelper = selecaoConsoleHelper;
        this.fenBoardPrinter = fenBoardPrinter;
    }

    public void aplicarProva() {
        if (cadastroService.listarParticipantes().isEmpty()) {
            System.out.println("cadastre participantes primeiro");
            return;
        }
        if (cadastroService.listarProvas().isEmpty()) {
            System.out.println("cadastre provas primeiro");
            return;
        }

        var participanteId = selecaoConsoleHelper.escolherParticipante();
        if (participanteId == null) {
            return;
        }

        var provaId = selecaoConsoleHelper.escolherProva();
        if (provaId == null) {
            return;
        }

        List<Questao> questoesDaProva;
        try {
            questoesDaProva = tentativaService.buscarQuestoesDaProva(provaId);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (questoesDaProva.isEmpty()) {
            System.out.println("esta prova não possui questões cadastradas");
            return;
        }

        Tentativa tentativa = new Tentativa();
        tentativa.setParticipanteId(participanteId);
        tentativa.setProvaId(provaId);

        System.out.println("\n--- Início da Prova ---");

        for (var q : questoesDaProva) {
            System.out.println("\nQuestão #" + q.getId());
            System.out.println(q.getEnunciado());

            if (q.getFenInicial() != null && !q.getFenInicial().isBlank()) {
                System.out.println("Posição inicial:");
                fenBoardPrinter.imprimir(q.getFenInicial());
            }

            for (var alt : q.getAlternativas()) {
                System.out.println(alt);
            }

            System.out.print("Sua resposta (A–E): ");
            char marcada;
            try {
                marcada = Questao.normalizar(input.nextLine().trim().charAt(0));
            } catch (Exception e) {
                System.out.println("resposta inválida (marcando como errada)");
                marcada = 'X';
            }

            Resposta resposta = tentativaService.corrigirResposta(q, marcada);
            tentativa.getRespostas().add(resposta);
        }

        tentativa = tentativaService.registrarTentativa(participanteId, provaId, tentativa.getRespostas());

        int nota = tentativaService.calcularNota(tentativa);
        System.out.println("\n--- Fim da Prova ---");
        System.out.println("Nota (acertos): " + nota + " / " + tentativa.getRespostas().size());
    }

    public void listarTentativas() {
        System.out.println("\n--- Tentativas ---");
        for (var t : tentativaService.listarTentativas()) {
            System.out.printf("#%d | participante=%d | prova=%d | nota=%d/%d%n", t.getId(), t.getParticipanteId(),
                    t.getProvaId(), tentativaService.calcularNota(t), t.getRespostas().size());
        }
    }
}
