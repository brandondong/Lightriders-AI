package lightriders.simulation;

import java.util.Scanner;

import javax.swing.JFrame;

import lightriders.game.Board;

public class HumanBotMatch {

	private final Board currentBoard;

	public HumanBotMatch(Board board) {
		currentBoard = board;
	}

	public void start() {
		JFrame frame = new JFrame("FrameDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Size the frame and show it.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try (Scanner s = new Scanner(System.in)) {
			System.out.print("Enter width: ");
			int width = s.nextInt();
			System.out.print("Enter height: ");
			int height = s.nextInt();
			System.out.print("Enter player 0 starting x: ");
			int p0x = s.nextInt();
			System.out.print("Enter player 0 starting y: ");
			int p0y = s.nextInt();
			System.out.print("Enter player 1 starting x: ");
			int p1x = s.nextInt();
			System.out.print("Enter player 1 starting y: ");
			int p1y = s.nextInt();
			Board board = Board.start(width, height, p0x, p0y, p1x, p1y);
			new HumanBotMatch(board).start();
		}
	}

}
