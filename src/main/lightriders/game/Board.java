package lightriders.game;

import java.util.ArrayList;
import java.util.List;

import lightriders.ai.Player;

public class Board {

	private final boolean[][] isFilledGrid;

	private final int p1x;

	private final int p1y;

	private final int p2x;

	private final int p2y;

	private Board(int width, int height, int p1x, int p1y, int p2x, int p2y) {
		this.p1x = p1x;
		this.p1y = p1y;
		this.p2x = p2x;
		this.p2y = p2y;
		isFilledGrid = new boolean[width][height];
		isFilledGrid[p1x][p1y] = true;
		isFilledGrid[p2x][p2y] = true;
	}

	private Board(boolean[][] isFilledGrid, int p1x, int p1y, int p2x, int p2y) {
		this.p1x = p1x;
		this.p1y = p1y;
		this.p2x = p2x;
		this.p2y = p2y;
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
		if (player == Player.ONE) {
			x = p1x;
			y = p1y;
		} else {
			x = p2x;
			y = p2y;
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
		if (player == Player.ONE) {
			int newp1x = p1x + dx;
			int newp1y = p1y + dy;
			isFilledCopy[newp1x][newp1y] = true;
			return new Board(isFilledCopy, newp1x, newp1y, p2x, p2y);
		} else {
			int newp2x = p2x + dx;
			int newp2y = p2y + dy;
			isFilledCopy[newp2x][newp2y] = true;
			return new Board(isFilledCopy, p1x, p1y, newp2x, newp2y);
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
	 * @param p1x
	 *            Player 1's starting x position
	 * @param p1y
	 *            Player 1's staring y position
	 * @param p2x
	 *            Player 2's starting x position
	 * @param p2y
	 *            Player 2's starting y position
	 * @return The new starting board
	 */
	public static Board start(int width, int height, int p1x, int p1y, int p2x, int p2y) {
		return new Board(width, height, p1x, p1y, p2x, p2y);
	}

}
