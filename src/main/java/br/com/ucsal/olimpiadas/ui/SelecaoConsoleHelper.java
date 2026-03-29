package br.com.ucsal.olimpiadas.ui;

import br.com.ucsal.olimpiadas.service.CadastroService;

import java.util.Scanner;

public class SelecaoConsoleHelper {

    private final Scanner input;
    private final CadastroService cadastroService;

    public SelecaoConsoleHelper(Scanner input, CadastroService cadastroService) {
        this.input = input;
        this.cadastroService = cadastroService;
    }

    public Long escolherParticipante() {
        System.out.println("\nParticipantes:");
        for (var p : cadastroService.listarParticipantes()) {
            System.out.printf("  %d) %s%n", p.getId(), p.getNome());
        }
        System.out.print("Escolha o id do participante: ");

        try {
            long id = Long.parseLong(input.nextLine());
            boolean existe = cadastroService.participanteExiste(id);
            if (!existe) {
                System.out.println("id inválido");
                return null;
            }
            return id;
        } catch (Exception e) {
            System.out.println("entrada inválida");
            return null;
        }
    }

    public Long escolherProva() {
        System.out.println("\nProvas:");
        for (var p : cadastroService.listarProvas()) {
            System.out.printf("  %d) %s%n", p.getId(), p.getTitulo());
        }
        System.out.print("Escolha o id da prova: ");

        try {
            long id = Long.parseLong(input.nextLine());
            boolean existe = cadastroService.provaExiste(id);
            if (!existe) {
                System.out.println("id inválido");
                return null;
            }
            return id;
        } catch (Exception e) {
            System.out.println("entrada inválida");
            return null;
        }
    }
}
