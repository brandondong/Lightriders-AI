package lightriders.ai;

import java.util.LinkedList;
import java.util.Queue;

import lightriders.evaluation.IEvaluator;
import lightriders.game.Board;

class ShortestPathEvaluator implements IEvaluator {

	private static class Coord {

		public final int x;

		public final int y;

		public final boolean isCurrentPlayer;

		public Coord(int x, int y, boolean isCurrentPlayer) {
			this.x = x;
			this.y = y;
			this.isCurrentPlayer = isCurrentPlayer;
		}

		/**
		 * For debugging purposes.
		 */
		@Override
		public String toString() {
			return String.format("[%d, %d] current player=%s", x, y, isCurrentPlayer);
		}
	}

	@Override
	public double evaluateBoard(Board board, Player player) {
		Player opponent = player.opponent();
		int px = board.getX(player);
		int py = board.getY(player);
		int ox = board.getX(opponent);
		int oy = board.getY(opponent);

		int countP = 0;
		int countO = 0;
		int width = board.width();
		int height = board.height();
		boolean[][] visited = new boolean[width][height];
		Queue<Coord> queue = new LinkedList<>();
		queue.add(new Coord(px, py, true));
		queue.add(new Coord(ox, oy, false));
		visited[px][py] = true;
		visited[ox][oy] = true;
		while (!queue.isEmpty()) {
			Coord current = queue.remove();
			int currentX = current.x;
			int currentY = current.y;
			// No worries about order of evaluation because of even spacing between players.
			if (current.isCurrentPlayer) {
				countP++;
			} else {
				countO++;
			}
			if (isEmptySpace(currentX - 1, currentY, board, visited, width, height)) {
				addToQueue(visited, queue, current, currentX - 1, currentY);
			}
			if (isEmptySpace(currentX + 1, currentY, board, visited, width, height)) {
				addToQueue(visited, queue, current, currentX + 1, currentY);
			}
			if (isEmptySpace(currentX, currentY - 1, board, visited, width, height)) {
				addToQueue(visited, queue, current, currentX, currentY - 1);
			}
			if (isEmptySpace(currentX, currentY + 1, board, visited, width, height)) {
				addToQueue(visited, queue, current, currentX, currentY + 1);
			}
		}

		return countP - countO;
	}

	private void addToQueue(boolean[][] visited, Queue<Coord> queue, Coord current, int x, int y) {
		queue.add(new Coord(x, y, current.isCurrentPlayer));
		visited[x][y] = true;
	}

	private boolean isEmptySpace(int x, int y, Board board, boolean[][] visited, int width, int height) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return false;
		}
		return !visited[x][y] && !board.isFilled(x, y);
	}

}
