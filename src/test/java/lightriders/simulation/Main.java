package lightriders.simulation;

import java.util.Scanner;

import lightriders.ai.BotTestFactory;
import lightriders.ai.Player;
import lightriders.game.Board;
import lightriders.random.DeterministicMajorityStrategy;

class Main {

	public static void main(String[] args) {
		try (Scanner s = new Scanner(System.in)) {
			System.out.print("New game/new custom game/load existing game? (1/2/3): ");
			int answer = s.nextInt();
			Board board;
			Player human;
			if (answer == 1) {
				int width = 16;
				int height = 16;
				System.out.print("Enter human player starting x: ");
				int p0x = s.nextInt();
				System.out.print("Enter human player starting y: ");
				int p0y = s.nextInt();
				int p1x = width - 1 - p0x;
				int p1y = p0y;
				board = Board.start(width, height, p0x, p0y, p1x, p1y);
				human = Player.ZERO;
			} else if (answer == 2) {
				System.out.print("Enter width: ");
				int width = s.nextInt();
				System.out.print("Enter height: ");
				int height = s.nextInt();
				System.out.print("Enter human starting x: ");
				int p0x = s.nextInt();
				System.out.print("Enter human starting y: ");
				int p0y = s.nextInt();
				System.out.print("Enter bot starting x: ");
				int p1x = s.nextInt();
				System.out.print("Enter bot starting y: ");
				int p1y = s.nextInt();
				board = Board.start(width, height, p0x, p0y, p1x, p1y);
				human = Player.ZERO;
			} else {
				System.out.print("Enter width: ");
				int width = s.nextInt();
				System.out.print("Enter height: ");
				int height = s.nextInt();
				System.out.print("Enter board input: ");
				String value = s.nextLine();
				board = Board.parseFromEngine(width, height, value);
				System.out.print("Enter human player id (0/1)");
				human = Player.parseFromEngine(s.nextLine());
			}

			new HumanBotMatch(BotTestFactory.bruteForce(new DeterministicMajorityStrategy()), board, human).start();
		}
	}

}
