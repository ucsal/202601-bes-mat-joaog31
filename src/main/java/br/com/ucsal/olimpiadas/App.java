package br.com.ucsal.olimpiadas;

import br.com.ucsal.olimpiadas.repository.ParticipanteRepository;
import br.com.ucsal.olimpiadas.repository.ProvaRepository;
import br.com.ucsal.olimpiadas.repository.QuestaoRepository;
import br.com.ucsal.olimpiadas.repository.TentativaRepository;
import br.com.ucsal.olimpiadas.repository.memory.InMemoryParticipanteRepository;
import br.com.ucsal.olimpiadas.repository.memory.InMemoryProvaRepository;
import br.com.ucsal.olimpiadas.repository.memory.InMemoryQuestaoRepository;
import br.com.ucsal.olimpiadas.repository.memory.InMemoryTentativaRepository;
import br.com.ucsal.olimpiadas.service.CadastroService;
import br.com.ucsal.olimpiadas.service.IdGeneratorService;
import br.com.ucsal.olimpiadas.service.SeedService;
import br.com.ucsal.olimpiadas.service.TentativaService;
import br.com.ucsal.olimpiadas.ui.FenBoardPrinter;

import java.util.List;
import java.util.Scanner;

public class App {

	private static final Scanner in = new Scanner(System.in);
	private static final FenBoardPrinter fenBoardPrinter = new FenBoardPrinter();

	private static final ParticipanteRepository participanteRepository = new InMemoryParticipanteRepository();
	private static final ProvaRepository provaRepository = new InMemoryProvaRepository();
	private static final QuestaoRepository questaoRepository = new InMemoryQuestaoRepository();
	private static final TentativaRepository tentativaRepository = new InMemoryTentativaRepository();

	private static final IdGeneratorService idGeneratorService = new IdGeneratorService();
	private static final CadastroService cadastroService = new CadastroService(
			participanteRepository,
			provaRepository,
			questaoRepository,
			idGeneratorService
	);
	private static final TentativaService tentativaService = new TentativaService(
			participanteRepository,
			provaRepository,
			questaoRepository,
			tentativaRepository,
			idGeneratorService
	);
	private static final SeedService seedService = new SeedService(cadastroService);

	public static void main(String[] args) {
		seedService.carregarDadosIniciais();

		while (true) {
			System.out.println("\n=== OLIMPÍADA DE QUESTÕES (V1) ===");
			System.out.println("1) Cadastrar participante");
			System.out.println("2) Cadastrar prova");
			System.out.println("3) Cadastrar questão (A–E) em uma prova");
			System.out.println("4) Aplicar prova (selecionar participante + prova)");
			System.out.println("5) Listar tentativas (resumo)");
			System.out.println("0) Sair");
			System.out.print("> ");

			switch (in.nextLine()) {
			case "1" -> cadastrarParticipante();
			case "2" -> cadastrarProva();
			case "3" -> cadastrarQuestao();
			case "4" -> aplicarProva();
			case "5" -> listarTentativas();
			case "0" -> {
				System.out.println("tchau");
				return;
			}
			default -> System.out.println("opção inválida");
			}
		}
	}

	static void cadastrarParticipante() {
		System.out.print("Nome: ");
		var nome = in.nextLine();

		System.out.print("Email (opcional): ");
		var email = in.nextLine();

		try {
			Participante participante = cadastroService.cadastrarParticipante(nome, email);
			System.out.println("Participante cadastrado: " + participante.getId());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	static void cadastrarProva() {
		System.out.print("Título da prova: ");
		var titulo = in.nextLine();

		try {
			Prova prova = cadastroService.cadastrarProva(titulo);
			System.out.println("Prova criada: " + prova.getId());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	static void cadastrarQuestao() {
		if (cadastroService.listarProvas().isEmpty()) {
			System.out.println("não há provas cadastradas");
			return;
		}

		var provaId = escolherProva();
		if (provaId == null)
			return;

		System.out.println("Enunciado:");
		var enunciado = in.nextLine();

		var alternativas = new String[5];
		for (int i = 0; i < 5; i++) {
			char letra = (char) ('A' + i);
			System.out.print("Alternativa " + letra + ": ");
			alternativas[i] = letra + ") " + in.nextLine();
		}

		System.out.print("Alternativa correta (A–E): ");
		char correta;
		try {
			correta = Questao.normalizar(in.nextLine().trim().charAt(0));
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


	static void aplicarProva() {
		if (cadastroService.listarParticipantes().isEmpty()) {
			System.out.println("cadastre participantes primeiro");
			return;
		}
		if (cadastroService.listarProvas().isEmpty()) {
			System.out.println("cadastre provas primeiro");
			return;
		}

		var participanteId = escolherParticipante();
		if (participanteId == null)
			return;

		var provaId = escolherProva();
		if (provaId == null)
			return;

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
				marcada = Questao.normalizar(in.nextLine().trim().charAt(0));
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

	public static int calcularNota(Tentativa tentativa) {
		return tentativaService.calcularNota(tentativa);
	}

	static void listarTentativas() {
		System.out.println("\n--- Tentativas ---");
		for (var t : tentativaService.listarTentativas()) {
			System.out.printf("#%d | participante=%d | prova=%d | nota=%d/%d%n", t.getId(), t.getParticipanteId(),
					t.getProvaId(), tentativaService.calcularNota(t), t.getRespostas().size());
		}
	}


	static Long escolherParticipante() {
		System.out.println("\nParticipantes:");
		for (var p : cadastroService.listarParticipantes()) {
			System.out.printf("  %d) %s%n", p.getId(), p.getNome());
		}
		System.out.print("Escolha o id do participante: ");

		try {
			long id = Long.parseLong(in.nextLine());
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

	static Long escolherProva() {
		System.out.println("\nProvas:");
		for (var p : cadastroService.listarProvas()) {
			System.out.printf("  %d) %s%n", p.getId(), p.getTitulo());
		}
		System.out.print("Escolha o id da prova: ");

		try {
			long id = Long.parseLong(in.nextLine());
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