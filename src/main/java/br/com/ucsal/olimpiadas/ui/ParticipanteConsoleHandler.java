package br.com.ucsal.olimpiadas.ui;

import br.com.ucsal.olimpiadas.Participante;
import br.com.ucsal.olimpiadas.service.CadastroService;

import java.util.Scanner;

public class ParticipanteConsoleHandler {

    private final Scanner input;
    private final CadastroService cadastroService;

    public ParticipanteConsoleHandler(Scanner input, CadastroService cadastroService) {
        this.input = input;
        this.cadastroService = cadastroService;
    }

    public void cadastrarParticipante() {
        System.out.print("Nome: ");
        var nome = input.nextLine();

        System.out.print("Email (opcional): ");
        var email = input.nextLine();

        try {
            Participante participante = cadastroService.cadastrarParticipante(nome, email);
            System.out.println("Participante cadastrado: " + participante.getId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
