package lightriders.simulation;

import java.util.Scanner;

import lightriders.ai.BotTestFactory;
import lightriders.ai.IBot;
import lightriders.ai.Player;
import lightriders.game.Board;

class Main {

	public static void main(String[] args) {
		try (Scanner s = new Scanner(System.in)) {
			IBot bot = constructBotFromInput(s);
			System.out.print("New game/load existing game? (1/2): ");
			boolean newGame = s.nextInt() == 1;
			if (newGame) {
				newGame(bot, s);
			} else {
				existingGame(bot, s);
			}
		}
	}

	private static IBot constructBotFromInput(Scanner s) {
		System.out.print("Competition/brute force bot? (1/2): ");
		if (s.nextInt() == 1) {
			return IBot.newCompetitionBot();
		}
		return BotTestFactory.newBruteForceBot();
	}

	private static void newGame(IBot bot, Scanner s) {
		System.out.print("Human-bot/identical bot match? (1/2): ");
		boolean humanBotMatch = s.nextInt() == 1;
		System.out.print("Enter width: ");
		int width = s.nextInt();
		System.out.print("Enter height: ");
		int height = s.nextInt();
		if (humanBotMatch) {
			System.out.print("Enter human starting x: ");
			int p0x = s.nextInt();
			System.out.print("Enter human starting y: ");
			int p0y = s.nextInt();
			System.out.print("Enter bot starting x: ");
			int p1x = s.nextInt();
			System.out.print("Enter bot starting y: ");
			int p1y = s.nextInt();
			Board board = Board.start(width, height, p0x, p0y, p1x, p1y);
			HumanBotMatch.start(bot, board, Player.ZERO);
		} else {
			System.out.print("Enter bot starting x: ");
			int p0x = s.nextInt();
			System.out.print("Enter bot starting y: ");
			int p0y = s.nextInt();
			int p1x = width - 1 - p0x;
			int p1y = p0y;
			Board board = Board.start(width, height, p0x, p0y, p1x, p1y);
			IdenticalBotMatch.start(bot, board);
		}
	}

	private static void existingGame(IBot bot, Scanner s) {
		System.out.print("Enter width: ");
		int width = s.nextInt();
		System.out.print("Enter height: ");
		int height = s.nextInt();
		System.out.print("Enter board input: ");
		String value = s.nextLine();
		Board board = Board.parseFromEngine(width, height, value);
		System.out.print("Enter human player id (0/1)");
		Player human = Player.parseFromEngine(s.nextLine());
		HumanBotMatch.start(bot, board, human);
	}

}
