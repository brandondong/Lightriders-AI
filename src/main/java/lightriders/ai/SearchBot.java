package lightriders.ai;

import java.util.List;

import lightriders.evaluation.IEvaluator;
import lightriders.evaluation.IRoundsEstimator;
import lightriders.game.Board;
import lightriders.game.Move;
import lightriders.random.IRandomStrategy;

public class SearchBot {

	private final int separatedDepth;

	private final SimultaneousMinmax<Board> minmax;

	private final IRandomStrategy randomStrategy;

	private final IRoundsEstimator estimator;

	/**
	 * @param depth
	 *            The depth to search to or 0 for no limit
	 * @param randomStrategy
	 *            A strategy for choosing moves based on calculated optimal
	 *            probabilities
	 * @param evaluator
	 *            Board evaluation function
	 */
	public SearchBot(int depth, int separatedDepth, IRandomStrategy randomStrategy, IEvaluator evaluator,
			IRoundsEstimator estimator) {
		this.separatedDepth = separatedDepth;
		this.randomStrategy = randomStrategy;
		this.estimator = estimator;
		minmax = new SimultaneousMinmax<Board>(depth, this::nextBoardsMatrix, evaluator::evaluateBoard);
	}

	/**
	 * Computes the best move for the given board and player.
	 * 
	 * @param board
	 *            The current board where the game is not over
	 * @param player
	 *            The player to choose for
	 * @return The computed best move
	 */
	public Move bestMove(Board board, Player player) {
		List<Move> moves = board.possibleMovesFor(player);
		if (playersSeparated(board)) {
			TreeHeightSearch<Board> maxRounds = new TreeHeightSearch<>(separatedDepth, b -> nextBoardArray(b, player),
					b -> estimator.roundsLeft(b, player));
			int bestIndex = maxRounds.highestSubtree(board);
			return moves.get(bestIndex);
		}
		double[] probabilities = minmax.optimalDecisionProbabilities(board, player);
		return randomStrategy.chooseMove(moves, probabilities);
	}

	private boolean playersSeparated(Board board) {
		boolean[][] visited = new boolean[board.width()][board.height()];
		return playersSeparated(board, board.getX(Player.ZERO), board.getY(Player.ZERO), board.getX(Player.ONE),
				board.getY(Player.ONE), visited);
	}

	private boolean playersSeparated(Board board, int x, int y, int targetX, int targetY, boolean[][] visited) {
		if (visited[x][y]) {
			return false;
		}
		visited[x][y] = true;
		if (x == targetX && y == targetY) {
			return true;
		}
		if (board.isFilled(x, y) || !board.inBounds(x, y)) {
			return false;
		}
		return playersSeparated(board, x + 1, y, targetX, targetY, visited)
				|| playersSeparated(board, x - 1, y, targetX, targetY, visited)
				|| playersSeparated(board, x, y + 1, targetX, targetY, visited)
				|| playersSeparated(board, x, y - 1, targetX, targetY, visited);
	}

	private Board[] nextBoardArray(Board board, Player player) {
		return null;
	}

	private Board[][] nextBoardsMatrix(Board board, Player player) {
		List<Move> moves = board.possibleMovesFor(player);
		if (moves.isEmpty()) {
			return new Board[][] {};
		}
		Player opponent = player.opponent();
		List<Move> opponentMoves = board.possibleMovesFor(opponent);
		if (opponentMoves.isEmpty()) {
			return new Board[][] {};
		}
		Board[][] nextBoards = new Board[moves.size()][opponentMoves.size()];
		int row = 0;
		for (Move move : moves) {
			Board next = board.makeMove(move, player);
			int column = 0;
			for (Move opponentMove : opponentMoves) {
				nextBoards[row][column] = next.makeMove(opponentMove, opponent);
				column++;
			}
			row++;
		}
		return nextBoards;
	}

}
