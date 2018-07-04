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

		public final int entryX;

		public final int entryY;

		public ChokePoint(int x, int y, int entryX, int entryY) {
			this.x = x;
			this.y = y;
			this.entryX = entryX;
			this.entryY = entryY;
		}

	}

	@Override
	public int roundsLeft(Board board, Player player) {
		int x = board.getX(player);
		int y = board.getY(player);
		boolean[][] visited = new boolean[board.width()][board.height()];
		return totalArea(x, y, visited, board, x, y) - 1;
	}

	private int totalArea(int x, int y, boolean[][] visited, Board board, int ignoreFilledX, int ignoredFilledY) {
		List<ChokePoint> chokePoints = new ArrayList<>();
		int area = area(x, y, visited, board, chokePoints, ignoreFilledX, ignoredFilledY);
		int maxChokePoint = chokePoints.stream().map(c -> {
			boolean[][] chokeVisited = new boolean[board.width()][board.height()];
			chokeVisited[c.entryX][c.entryY] = true;
			return totalArea(c.x, c.y, chokeVisited, board, ignoreFilledX, ignoredFilledY);
		}).max(Comparator.naturalOrder()).orElse(0);
		return area + maxChokePoint;
	}

	private int area(int x, int y, boolean[][] visited, Board board, List<ChokePoint> chokePoints, int ignoreFilledX,
			int ignoredFilledY) {
		if (!board.inBounds(x, y) || visited[x][y]) {
			return 0;
		}
		visited[x][y] = true;
		if (board.isFilled(x, y) && !(x == ignoreFilledX && y == ignoredFilledY)) {
			return 0;
		}
		int upArea = 0;
		if (inBoundsAndFilled(x - 1, y - 1, board) && inBoundsAndFilled(x + 1, y - 1, board)
				&& !board.isFilled(x, y - 1)) {
			chokePoints.add(new ChokePoint(x, y - 1, x, y));
		} else {
			upArea = area(x, y - 1, visited, board, chokePoints, ignoreFilledX, ignoredFilledY);
		}
		int downArea = 0;
		if (inBoundsAndFilled(x - 1, y + 1, board) && inBoundsAndFilled(x + 1, y + 1, board)
				&& !board.isFilled(x, y + 1)) {
			chokePoints.add(new ChokePoint(x, y + 1, x, y));
		} else {
			downArea = area(x, y + 1, visited, board, chokePoints, ignoreFilledX, ignoredFilledY);
		}
		int leftArea = 0;
		if (inBoundsAndFilled(x - 1, y - 1, board) && inBoundsAndFilled(x - 1, y + 1, board)
				&& !board.isFilled(x - 1, y)) {
			chokePoints.add(new ChokePoint(x - 1, y, x, y));
		} else {
			leftArea = area(x - 1, y, visited, board, chokePoints, ignoreFilledX, ignoredFilledY);
		}
		int rightArea = 0;
		if (inBoundsAndFilled(x + 1, y - 1, board) && inBoundsAndFilled(x + 1, y + 1, board)
				&& !board.isFilled(x + 1, y)) {
			chokePoints.add(new ChokePoint(x + 1, y, x, y));
		} else {
			rightArea = area(x + 1, y, visited, board, chokePoints, ignoreFilledX, ignoredFilledY);
		}
		return 1 + upArea + downArea + leftArea + rightArea;
	}

	private boolean inBoundsAndFilled(int x, int y, Board board) {
		return board.inBounds(x, y) && board.isFilled(x, y);
	}

}
