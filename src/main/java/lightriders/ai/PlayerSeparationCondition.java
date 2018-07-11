package lightriders.ai;

import lightriders.game.Board;

class PlayerSeparationCondition {

	/**
	 * Checks if the two players have no way of affecting the other's movements.
	 * 
	 * @param board
	 *            The current board
	 * @return <code>true</code> if the players are separated, <code>false</code>
	 *         otherwise
	 */
	public boolean checkSeparated(Board board) {
		boolean[][] visited = new boolean[board.width()][board.height()];
		return !checkConnected(board, board.getX(Player.ZERO), board.getY(Player.ZERO), board.getX(Player.ONE),
				board.getY(Player.ONE), visited, true, true);
	}

	private boolean checkConnected(Board board, int x, int y, int targetX, int targetY, boolean[][] visited,
			boolean ignoreFilled, boolean ignoreTarget) {
		if (!board.inBounds(x, y) || visited[x][y]) {
			return false;
		}
		if (x == targetX && y == targetY) {
			return !ignoreTarget;
		}
		visited[x][y] = true;
		if (!ignoreFilled && board.isFilled(x, y)) {
			return false;
		}
		// Don't consider any connections through immediate neighbors.
		return checkConnected(board, x + 1, y, targetX, targetY, visited, false, ignoreFilled)
				|| checkConnected(board, x - 1, y, targetX, targetY, visited, false, ignoreFilled)
				|| checkConnected(board, x, y + 1, targetX, targetY, visited, false, ignoreFilled)
				|| checkConnected(board, x, y - 1, targetX, targetY, visited, false, ignoreFilled);
	}

}
