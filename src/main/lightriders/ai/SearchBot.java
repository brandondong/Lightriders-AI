package lightriders.ai;

import java.util.List;

import lightriders.game.Board;
import lightriders.game.Move;
import lightriders.random.IRandomStrategy;

public class SearchBot {

	private final SimultaneousMinmax<Board> minmax;

	private final IRandomStrategy randomStrategy;

	/**
	 * @param depth
	 *            The depth to search to or 0 for no limit
	 * @param randomStrategy
	 *            A strategy for choosing moves based on calculated optimal
	 *            probabilities
	 */
	public SearchBot(int depth, IRandomStrategy randomStrategy) {
		this.randomStrategy = randomStrategy;
		minmax = new SimultaneousMinmax<Board>(depth, this::nextBoards, null);
	}

	/**
	 * Computes the best move for the given board and player.
	 * 
	 * @param board
	 *            The current board
	 * @param player
	 *            The player to choose for
	 * @return The computed best move
	 */
	public Move bestMove(Board board, Player player) {
		double[] probabilities = minmax.optimalDecisionProbabilities(board, player);
		List<Move> moves = board.possibleMovesFor(player);
		return randomStrategy.chooseMove(moves, probabilities);
	}

	private Board[][] nextBoards(Board board, Player player) {
		List<Move> moves = board.possibleMovesFor(player);
		if (moves.isEmpty()) {
			return new Board[][] {};
		}
		List<Move> opponentMoves = board.possibleMovesFor(player.opponent());
		if (opponentMoves.isEmpty()) {
			return new Board[][] {};
		}
		Board[][] nextBoards = new Board[moves.size()][opponentMoves.size()];
		// TODO
		return nextBoards;
	}

}
