package lightriders.ai;

import java.util.List;
import java.util.stream.Collectors;

import lightriders.evaluation.IEvaluator;
import lightriders.evaluation.IRoundsEstimator;
import lightriders.game.Board;
import lightriders.game.Move;
import lightriders.random.IRandomStrategy;

class SearchBot implements IBot {

	private final int separatedDepth;

	private final SimultaneousMinmax<Board> minmax;

	private final IRandomStrategy randomStrategy;

	private final IRoundsEstimator estimator;

	private final PlayerSeparationCondition separationCondition = new PlayerSeparationCondition();

	/**
	 * @param depth
	 *            The depth to search to when not separated or 0 for no limit
	 * @param separatedDepth
	 *            The depth to search to when separated or 0 for no limit
	 * @param randomStrategy
	 *            A strategy for choosing moves based on calculated optimal
	 *            probabilities
	 * @param evaluator
	 *            Board evaluation function
	 * @param estimator
	 *            Separated rounds left estimation function
	 */
	public SearchBot(int depth, int separatedDepth, IRandomStrategy randomStrategy, IEvaluator evaluator,
			IRoundsEstimator estimator) {
		this.separatedDepth = separatedDepth;
		this.randomStrategy = randomStrategy;
		this.estimator = estimator;
		minmax = new SimultaneousMinmax<Board>(depth, this::nextBoardsMatrix, evaluator::evaluateBoard);
	}

	@Override
	public Move bestMove(Board board, Player player) {
		List<Move> moves = board.possibleMovesFor(player);
		if (separationCondition.checkSeparated(board)) {
			TreeHeightSearch<Board> maxRounds = new TreeHeightSearch<>(separatedDepth, b -> nextBoardArray(b, player),
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
