package lightriders.simulation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import lightriders.ai.Player;
import lightriders.game.Board;
import lightriders.game.Move;

class InteractiveBoard {

	private static final String BACKGROUND_IMG_PATH = "src/test/resources/background.png";

	private static final Color GREY = new Color(51, 51, 51);

	private static final Color BLUE = new Color(106, 160, 252);

	private static final Color PINK = new Color(228, 25, 249);

	private static final int STROKE_WIDTH = 4;

	private static final int CELL_SIZE = 33;

	private static final int[] UP_X_POINTS = new int[] { CELL_SIZE / 2, CELL_SIZE / 2 + 6, CELL_SIZE / 2,
			CELL_SIZE / 2 - 6, CELL_SIZE / 2 };

	private static final int[] UP_Y_POINTS = new int[] { CELL_SIZE / 2 - 6, CELL_SIZE / 2 + 6, CELL_SIZE / 2 + 2,
			CELL_SIZE / 2 + 6, CELL_SIZE / 2 - 6 };

	private static final int[] DOWN_X_POINTS = Arrays.stream(UP_X_POINTS).map(i -> CELL_SIZE - 1 - i).toArray();

	private static final int[] DOWN_Y_POINTS = Arrays.stream(UP_Y_POINTS).map(i -> CELL_SIZE - 1 - i).toArray();

	private Board currentBoard;

	private final int start0x;

	private final int start0y;

	private final int start1x;

	private final int start1y;

	private final boolean[][] filledCells;

	private final List<Move> p0Moves = new ArrayList<>();

	private final List<Move> p1Moves = new ArrayList<>();

	private final IBoardMouseListener mouseClickedCallback;

	private JLabel background;

	/**
	 * @param currentBoard
	 *            The starting game board
	 * @param mouseClickedCallback
	 *            A handler for mouse click events on the board
	 */
	public InteractiveBoard(Board currentBoard, IBoardMouseListener mouseClickedCallback) {
		this.currentBoard = currentBoard;
		this.mouseClickedCallback = mouseClickedCallback;
		start0x = currentBoard.getX(Player.ZERO);
		start0y = currentBoard.getY(Player.ZERO);
		start1x = currentBoard.getX(Player.ONE);
		start1y = currentBoard.getY(Player.ONE);
		filledCells = new boolean[currentBoard.width()][currentBoard.height()];
		for (int x = 0; x < currentBoard.width(); x++) {
			for (int y = 0; y < currentBoard.height(); y++) {
				filledCells[x][y] = currentBoard.isFilled(x, y) && !(x == start0x && y == start0y)
						&& !(x == start1x && y == start1y);
			}
		}
	}

	/**
	 * Starts a new window with an interactive board.
	 */
	public void create() {
		JFrame frame = new JFrame("Light Riders");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		ImageIcon icon = new ImageIcon(BACKGROUND_IMG_PATH);
		background = new JLabel(icon) {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int upY = getGridTopY(this);
				int leftX = getGridLeftX(this);
				g.setColor(GREY);
				// Draw the grid and any filled cells.
				for (int x = 0; x < currentBoard.width(); x++) {
					for (int y = 0; y < currentBoard.height(); y++) {
						g.drawRect(leftX + x * CELL_SIZE, upY + y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
						if (filledCells[x][y]) {
							g.fillRect(leftX + x * CELL_SIZE, upY + y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
						}
					}
				}
				// Draw the player paths.
				int p0x = currentBoard.getX(Player.ZERO);
				int p0y = currentBoard.getY(Player.ZERO);
				int p1x = currentBoard.getX(Player.ONE);
				int p1y = currentBoard.getY(Player.ONE);
				drawPlayerPath(g, upY, leftX, p0x, p0y, start0x, start0y, p0Moves, BLUE);
				drawPlayerPath(g, upY, leftX, p1x, p1y, start1x, start1y, p1Moves, PINK);
			}

			private void drawPlayerPath(Graphics g, int upY, int leftX, int pX, int pY, int startX, int startY,
					List<Move> pMoves, Color c) {
				g.setColor(c);
				((Graphics2D) g).setStroke(new BasicStroke(STROKE_WIDTH));
				int currentX = startX;
				int currentY = startY;
				int halfSize = CELL_SIZE / 2;
				for (Move m : pMoves) {
					int nextX = currentX;
					int nextY = currentY;
					if (m == Move.UP) {
						nextY--;
					} else if (m == Move.DOWN) {
						nextY++;
					} else if (m == Move.LEFT) {
						nextX--;
					} else {
						nextX++;
					}
					g.drawLine(leftX + currentX * CELL_SIZE + halfSize, upY + currentY * CELL_SIZE + halfSize,
							leftX + nextX * CELL_SIZE + halfSize, upY + nextY * CELL_SIZE + halfSize);
					currentX = nextX;
					currentY = nextY;
				}
				int[] xPoints;
				int[] yPoints;
				Move lastMove;
				if (!pMoves.isEmpty()) {
					lastMove = pMoves.get(pMoves.size() - 1);
				} else if (pX < currentBoard.width() / 2) {
					lastMove = Move.RIGHT;
				} else {
					lastMove = Move.LEFT;
				}
				if (lastMove == Move.UP) {
					xPoints = UP_X_POINTS;
					yPoints = UP_Y_POINTS;
				} else if (lastMove == Move.DOWN) {
					xPoints = DOWN_X_POINTS;
					yPoints = DOWN_Y_POINTS;
				} else if (lastMove == Move.LEFT) {
					xPoints = UP_Y_POINTS;
					yPoints = UP_X_POINTS;
				} else {
					xPoints = DOWN_Y_POINTS;
					yPoints = DOWN_X_POINTS;
				}
				g.drawPolygon(Arrays.stream(xPoints).map(i -> i + pX * CELL_SIZE + leftX).toArray(),
						Arrays.stream(yPoints).map(i -> i + pY * CELL_SIZE + upY).toArray(), xPoints.length);
			}
		};
		background.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int clickX = e.getX();
				int clickY = e.getY();
				int upY = getGridTopY(background);
				int leftX = getGridLeftX(background);
				if (clickX < leftX || clickY < upY) {
					return;
				}
				int x = (clickX - leftX) / CELL_SIZE;
				int y = (clickY - upY) / CELL_SIZE;
				if (x >= currentBoard.width() || y >= currentBoard.height()) {
					return;
				}
				mouseClickedCallback.boardClick(x, y, currentBoard);
			}
		});
		frame.add(background);
		// Size the frame and show it.
		frame.pack();
		frame.setVisible(true);

	}

	/**
	 * Makes the specified moves for each player, re-rendering the board and
	 * discarding any processing images.
	 * <p>
	 * The moves must be valid for each player
	 * 
	 * @param p0Move
	 *            Player 0's move
	 * @param p1Move
	 *            Player 1's move
	 */
	public void makeMoves(Move p0Move, Move p1Move) {
		currentBoard = currentBoard.makeMove(p0Move, Player.ZERO).makeMove(p1Move, Player.ONE);
		p0Moves.add(p0Move);
		p1Moves.add(p1Move);
		if (background != null) {
			background.repaint();
			// TODO hide processing image.
		}
	}

	/**
	 * Displays a processing image overlaying the board.
	 */
	public void showProcessingImage() {
		// TODO Auto-generated method stub

	}

	private int getGridTopY(JComponent component) {
		int centerY = component.getHeight() / 2;
		int totalHeight = currentBoard.height() * CELL_SIZE;
		int upY = centerY - totalHeight / 2;
		return upY;
	}

	private int getGridLeftX(JComponent component) {
		int centerX = component.getWidth() / 2;
		int totalWidth = currentBoard.width() * CELL_SIZE;
		int leftX = centerX - totalWidth / 2;
		return leftX;
	}

}
