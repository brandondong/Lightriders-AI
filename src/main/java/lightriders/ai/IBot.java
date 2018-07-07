package lightriders.ai;

import lightriders.evaluation.ChokePointRoundsEstimator;
import lightriders.game.Board;
import lightriders.game.Move;
import lightriders.random.DeterministicMajorityStrategy;

public interface IBot {

	/**
	 * Computes the best move for the given board and player.
	 * 
	 * @param board
	 *            The current board where the game is not over
	 * @param player
	 *            The player to choose for
	 * @return The computed best move
	 */
	Move bestMove(Board board, Player player);

	/**
	 * Creates a fully configured bot for competition use.
	 * 
	 * @return A new bot
	 */
	public static IBot newCompetitionBot() {
		return new SearchBot(6, 12, new DeterministicMajorityStrategy(), null, new ChokePointRoundsEstimator());
	}

}