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
import br.com.ucsal.olimpiadas.ui.ParticipanteConsoleHandler;
import br.com.ucsal.olimpiadas.ui.ProvaConsoleHandler;
import br.com.ucsal.olimpiadas.ui.QuestaoConsoleHandler;
import br.com.ucsal.olimpiadas.ui.SelecaoConsoleHelper;
import br.com.ucsal.olimpiadas.ui.TentativaConsoleHandler;

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
	private static final SelecaoConsoleHelper selecaoConsoleHelper = new SelecaoConsoleHelper(in, cadastroService);
	private static final ParticipanteConsoleHandler participanteConsoleHandler = new ParticipanteConsoleHandler(in,
			cadastroService);
	private static final ProvaConsoleHandler provaConsoleHandler = new ProvaConsoleHandler(in, cadastroService);
	private static final QuestaoConsoleHandler questaoConsoleHandler = new QuestaoConsoleHandler(in, cadastroService,
			selecaoConsoleHelper);
	private static final TentativaConsoleHandler tentativaConsoleHandler = new TentativaConsoleHandler(in,
			cadastroService, tentativaService, selecaoConsoleHelper, fenBoardPrinter);

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
			case "1" -> participanteConsoleHandler.cadastrarParticipante();
			case "2" -> provaConsoleHandler.cadastrarProva();
			case "3" -> questaoConsoleHandler.cadastrarQuestao();
			case "4" -> tentativaConsoleHandler.aplicarProva();
			case "5" -> tentativaConsoleHandler.listarTentativas();
			case "0" -> {
				System.out.println("tchau");
				return;
			}
			default -> System.out.println("opção inválida");
			}
		}
	}

}