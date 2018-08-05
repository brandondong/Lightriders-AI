package lightriders.ai;

import java.util.List;
import java.util.stream.Collectors;

import lightriders.evaluation.IEvaluator;
import lightriders.evaluation.IRoundsEstimator;
import lightriders.game.Board;
import lightriders.game.Move;
import lightriders.random.IRandomStrategy;

class SearchBotDepthHelper {

	private final int searchDepth;

	private final IRandomStrategy randomStrategy;

	private final IRoundsEstimator estimator;

	private final SimultaneousMinmax<Board> minmax;

	/**
	 * @param depth
	 *            The depth to search to or 0 for no limit
	 * @param randomStrategy
	 *            A strategy for choosing moves based on calculated optimal
	 *            probabilities
	 * @param evaluator
	 *            Board evaluation function
	 * @param estimator
	 *            Separated rounds left estimation function
	 */
	public SearchBotDepthHelper(int searchDepth, IRandomStrategy randomStrategy, IEvaluator evaluator,
			IRoundsEstimator estimator) {
		this.searchDepth = searchDepth;
		this.randomStrategy = randomStrategy;
		this.estimator = estimator;
		minmax = new SimultaneousMinmax<Board>(searchDepth, this::nextBoardsMatrix, evaluator::evaluateBoard);
	}

	/**
	 * Computes the best move for the given board and player up to the specified
	 * search depth.
	 * 
	 * @param board
	 *            The current board where the game is not over
	 * @param player
	 *            The player to choose for
	 * @param separated
	 *            True if the two players have no way of affecting the other's
	 *            movements
	 * @param moves
	 *            The possible moves for the given player on this board
	 * @return The computed best move
	 */
	public Move bestMoveForDepth(Board board, Player player, boolean separated, List<Move> moves) {
		if (separated) {
			TreeHeightSearch<Board> maxRounds = new TreeHeightSearch<>(searchDepth, b -> nextBoardArray(b, player),
					b -> estimator.roundsLeft(b, player));
			int bestIndex = maxRounds.highestSubtree(board);
			return moves.get(bestIndex);
		}
		double[] probabilities = minmax.optimalDecisionProbabilities(board, player);
		return randomStrategy.chooseMove(moves, probabilities);
	}

	private List<Board> nextBoardArray(Board board, Player player) {
		return board.possibleMovesFor(player).stream().map(m -> board.makeMove(m, player)).collect(Collectors.toList());
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
