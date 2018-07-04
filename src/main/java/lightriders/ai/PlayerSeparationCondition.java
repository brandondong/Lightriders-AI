package lightriders.ai;

import lightriders.game.Board;

class PlayerSeparationCondition {

	/**
	 * Checks if the two players have no way of reaching other.
	 * 
	 * @param board
	 *            The current board
	 * @return <code>true</code> if the players are separated, <code>false</code>
	 *         otherwise
	 */
	public boolean checkSeparated(Board board) {
		boolean[][] visited = new boolean[board.width()][board.height()];
		return !checkConnected(board, board.getX(Player.ZERO), board.getY(Player.ZERO), board.getX(Player.ONE),
				board.getY(Player.ONE), visited, true);
	}

	private boolean checkConnected(Board board, int x, int y, int targetX, int targetY, boolean[][] visited,
			boolean ignoreFilled) {
		if (!board.inBounds(x, y) || visited[x][y]) {
			return false;
		}
		visited[x][y] = true;
		if (x == targetX && y == targetY) {
			return true;
		}
		if (!ignoreFilled && board.isFilled(x, y)) {
			return false;
		}
		return checkConnected(board, x + 1, y, targetX, targetY, visited, false)
				|| checkConnected(board, x - 1, y, targetX, targetY, visited, false)
				|| checkConnected(board, x, y + 1, targetX, targetY, visited, false)
				|| checkConnected(board, x, y - 1, targetX, targetY, visited, false);
	}

}
