package lightriders.game;

import java.util.ArrayList;
import java.util.List;

import lightriders.ai.Player;

public class Board {

	private final boolean[][] isFilledGrid;

	private final int p0x;

	private final int p0y;

	private final int p1x;

	private final int p1y;

	private Board(boolean[][] isFilledGrid, int p0x, int p0y, int p1x, int p1y) {
		this.p0x = p0x;
		this.p0y = p0y;
		this.p1x = p1x;
		this.p1y = p1y;
		this.isFilledGrid = isFilledGrid;
	}

	/**
	 * Determines the possible moves available for a specified player
	 * 
	 * @param player
	 *            The player to calculate for
	 * @return The possible moves for the given player
	 */
	public List<Move> possibleMovesFor(Player player) {
		int x;
		int y;
		if (player == Player.ZERO) {
			x = p0x;
			y = p0y;
		} else {
			x = p1x;
			y = p1y;
		}
		List<Move> moves = new ArrayList<>(3);
		if (y >= 1 && !isFilledGrid[x][y - 1]) {
			moves.add(Move.UP);
		}
		if (y <= isFilledGrid[0].length - 2 && !isFilledGrid[x][y + 1]) {
			moves.add(Move.DOWN);
		}
		if (x >= 1 && !isFilledGrid[x - 1][y]) {
			moves.add(Move.LEFT);
		}
		if (x <= isFilledGrid.length - 2 && !isFilledGrid[x + 1][y]) {
			moves.add(Move.RIGHT);
		}
		return moves;
	}

	/**
	 * Produces the new board after making the specified move for the given player.
	 * <p>
	 * The move passed in must be valid for the player
	 * 
	 * @param move
	 *            The move to make
	 * @param player
	 *            The player to move for
	 * @return The new board after making the move
	 */
	public Board makeMove(Move move, Player player) {
		int width = isFilledGrid.length;
		int height = isFilledGrid[0].length;
		boolean[][] isFilledCopy = new boolean[width][height];
		for (int i = 0; i < width; i++) {
			System.arraycopy(isFilledGrid[i], 0, isFilledCopy[i], 0, height);
		}
		int dx;
		int dy;
		if (move == Move.UP) {
			dx = 0;
			dy = -1;
		} else if (move == Move.DOWN) {
			dx = 0;
			dy = 1;
		} else if (move == Move.LEFT) {
			dx = -1;
			dy = 0;
		} else {
			dx = 1;
			dy = 0;
		}
		if (player == Player.ZERO) {
			int newp1x = p0x + dx;
			int newp1y = p0y + dy;
			isFilledCopy[newp1x][newp1y] = true;
			return new Board(isFilledCopy, newp1x, newp1y, p1x, p1y);
		} else {
			int newp2x = p1x + dx;
			int newp2y = p1y + dy;
			isFilledCopy[newp2x][newp2y] = true;
			return new Board(isFilledCopy, p0x, p0y, newp2x, newp2y);
		}
	}

	/**
	 * Formatted in the engine board format.
	 */
	@Override
	public String toString() {
		return null;
	}

	/**
	 * Constructs a new board with the two players at the given starting positions.
	 * 
	 * @param width
	 *            The board width
	 * @param height
	 *            The board height
	 * @param p0x
	 *            Player 0's starting x position
	 * @param p0y
	 *            Player 0's staring y position
	 * @param p1x
	 *            Player 1's starting x position
	 * @param p1y
	 *            Player 1's starting y position
	 * @return The new starting board
	 */
	public static Board start(int width, int height, int p0x, int p0y, int p1x, int p1y) {
		boolean[][] isFilledGrid = new boolean[width][height];
		isFilledGrid[p0x][p0y] = true;
		isFilledGrid[p1x][p1y] = true;
		return new Board(isFilledGrid, p0x, p0y, p1x, p1y);
	}

	/**
	 * Constructs a new board from the engine input.
	 * 
	 * @param width
	 *            The board width
	 * @param height
	 *            The board height
	 * @param value
	 *            Engine input
	 * @return The corresponding board
	 */
	public static Board parseFromEngine(int width, int height, String value) {
		boolean[][] isFilledGrid = new boolean[width][height];
		String[] cells = value.split(",");
		int p0x = -1;
		int p0y = -1;
		int p1x = -1;
		int p1y = -1;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int index = y * width + x;
				String cell = cells[index];
				if (cell.equals("0")) {
					p0x = x;
					p0y = y;
				} else if (cell.equals("1")) {
					p1x = x;
					p1y = y;
				}
				if (!cell.equals(".")) {
					isFilledGrid[x][y] = true;
				}
			}
		}
		return new Board(isFilledGrid, p0x, p0y, p1x, p1y);
	}

}
