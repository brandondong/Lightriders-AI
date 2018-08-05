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
	 * @param time
	 *            Time to process move in milliseconds
	 * @return The computed best move
	 */
	Move bestMove(Board board, Player player, int time);

	/**
	 * Creates a fully configured bot for competition use.
	 * 
	 * @return A new bot
	 */
	public static IBot newCompetitionBot() {
		return new SearchBot(new DeterministicMajorityStrategy(), null, new ChokePointRoundsEstimator());
	}

}