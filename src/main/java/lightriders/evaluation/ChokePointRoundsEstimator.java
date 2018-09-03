package lightriders.evaluation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lightriders.ai.Player;
import lightriders.game.Board;

public class ChokePointRoundsEstimator implements IRoundsEstimator {

	private static class ChokePoint {

		public final int x;

		public final int y;

		public ChokePoint(int x, int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * For debugging purposes.
		 */
		@Override
		public String toString() {
			return String.format("[%d, %d]", x, y);
		}
	}

	@Override
	public int roundsLeft(Board board, Player player) {
		int x = board.getX(player);
		int y = board.getY(player);
		boolean[][] visited = new boolean[board.width()][board.height()];
		return totalArea(x, y, visited, board) - 1;
	}

	private int totalArea(int x, int y, boolean[][] visited, Board board) {
		// Estimate total area by combining the area accessible before choke points and
		// then adding the maximum area choke point found recursively.
		List<ChokePoint> chokePoints = new ArrayList<>();
		int areaWithoutChokePoints = areaWithoutChokePoints(x, y, visited, board, chokePoints);
		// Guaranteed termination because of reuse of the visited check array.
		int maxChokePoint = chokePoints.stream().map(c -> {
			if (visited[c.x][c.y]) {
				return 0;
			}
			return totalArea(c.x, c.y, visited, board);
		}).max(Comparator.naturalOrder()).orElse(0);
		return areaWithoutChokePoints + maxChokePoint;
	}

	private int areaWithoutChokePoints(int x, int y, boolean[][] visited, Board board, List<ChokePoint> chokePoints) {
		visited[x][y] = true;
		int upArea = 0;
		if (wallOrFilled(x - 1, y - 1, board) && wallOrFilled(x + 1, y - 1, board)
				&& emptyUnvisited(x, y - 1, visited, board)) {
			chokePoints.add(new ChokePoint(x, y - 1));
		} else if (emptyUnvisited(x, y - 1, visited, board)) {
			upArea = areaWithoutChokePoints(x, y - 1, visited, board, chokePoints);
		}
		int downArea = 0;
		if (wallOrFilled(x - 1, y + 1, board) && wallOrFilled(x + 1, y + 1, board)
				&& emptyUnvisited(x, y + 1, visited, board)) {
			chokePoints.add(new ChokePoint(x, y + 1));
		} else if (emptyUnvisited(x, y + 1, visited, board)) {
			downArea = areaWithoutChokePoints(x, y + 1, visited, board, chokePoints);
		}
		int leftArea = 0;
		if (wallOrFilled(x - 1, y - 1, board) && wallOrFilled(x - 1, y + 1, board)
				&& emptyUnvisited(x - 1, y, visited, board)) {
			chokePoints.add(new ChokePoint(x - 1, y));
		} else if (emptyUnvisited(x - 1, y, visited, board)) {
			leftArea = areaWithoutChokePoints(x - 1, y, visited, board, chokePoints);
		}
		int rightArea = 0;
		if (wallOrFilled(x + 1, y - 1, board) && wallOrFilled(x + 1, y + 1, board)
				&& emptyUnvisited(x + 1, y, visited, board)) {
			chokePoints.add(new ChokePoint(x + 1, y));
		} else if (emptyUnvisited(x + 1, y, visited, board)) {
			rightArea = areaWithoutChokePoints(x + 1, y, visited, board, chokePoints);
		}
		return 1 + upArea + downArea + leftArea + rightArea;
	}

	private boolean emptyUnvisited(int x, int y, boolean[][] visited, Board board) {
		return board.inBounds(x, y) && !visited[x][y] && !board.isFilled(x, y);
	}

	private boolean wallOrFilled(int x, int y, Board board) {
		return !board.inBounds(x, y) || board.isFilled(x, y);
	}

}
