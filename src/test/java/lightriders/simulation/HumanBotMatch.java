package lightriders.simulation;

import java.awt.Graphics;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import lightriders.game.Board;

public class HumanBotMatch {

	private static final String BACKGROUND_IMG_PATH = "src/test/resources/background.png";

	private static final int CELL_SIZE = 33;

	private final Board currentBoard;

	public HumanBotMatch(Board board) {
		currentBoard = board;
	}

	public void start() {
		JFrame frame = new JFrame("Light Riders");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		ImageIcon icon = new ImageIcon(BACKGROUND_IMG_PATH);
		JLabel background = new JLabel(icon) {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int centerX = getWidth() / 2;
				int centerY = getHeight() / 2;
				int totalWidth = currentBoard.width() * CELL_SIZE;
				int totalHeight = currentBoard.height() * CELL_SIZE;
				int leftX = centerX - totalWidth / 2;
				int upY = centerY - totalHeight / 2;
				for (int x = 0; x < currentBoard.width(); x++) {
					for (int y = 0; y < currentBoard.height(); y++) {
						g.drawRect(leftX + x * CELL_SIZE, upY + y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
					}
				}
			}
		};
		frame.add(background);
		// Size the frame and show it.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try (Scanner s = new Scanner(System.in)) {
			System.out.print("New game/new custom game/load existing game? (1/2/3): ");
			int answer = s.nextInt();
			Board board;
			if (answer == 1) {
				int width = 16;
				int height = 16;
				System.out.print("Enter player 0 starting x: ");
				int p0x = s.nextInt();
				System.out.print("Enter player 0 starting y: ");
				int p0y = s.nextInt();
				int p1x = width - 1 - p0x;
				int p1y = height - 1 - p0y;
				board = Board.start(width, height, p0x, p0y, p1x, p1y);
			} else if (answer == 2) {
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
				board = Board.start(width, height, p0x, p0y, p1x, p1y);
			} else {
				System.out.print("Enter width: ");
				int width = s.nextInt();
				System.out.print("Enter height: ");
				int height = s.nextInt();
				System.out.print("Enter board input: ");
				String value = s.nextLine();
				board = Board.parseFromEngine(width, height, value);
			}

			new HumanBotMatch(board).start();
		}
	}

}
