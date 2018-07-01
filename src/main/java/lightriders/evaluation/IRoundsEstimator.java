package lightriders.evaluation;

import lightriders.ai.Player;
import lightriders.game.Board;

public interface IRoundsEstimator {

	/**
	 * Estimates the rounds left on a board for a specified player.
	 * 
	 * @param board
	 *            The board to estimate for
	 * @param player
	 *            The player to estimate for
	 * @return The number of estimated rounds left
	 */
	int roundsLeft(Board board, Player player);

}
