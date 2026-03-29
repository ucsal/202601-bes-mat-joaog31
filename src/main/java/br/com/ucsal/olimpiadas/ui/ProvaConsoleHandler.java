package br.com.ucsal.olimpiadas.ui;

import br.com.ucsal.olimpiadas.Prova;
import br.com.ucsal.olimpiadas.service.CadastroService;

import java.util.Scanner;

public class ProvaConsoleHandler {

    private final Scanner input;
    private final CadastroService cadastroService;

    public ProvaConsoleHandler(Scanner input, CadastroService cadastroService) {
        this.input = input;
        this.cadastroService = cadastroService;
    }

    public void cadastrarProva() {
        System.out.print("Título da prova: ");
        var titulo = input.nextLine();

        try {
            Prova prova = cadastroService.cadastrarProva(titulo);
            System.out.println("Prova criada: " + prova.getId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
