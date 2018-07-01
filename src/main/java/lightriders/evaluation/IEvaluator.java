package lightriders.evaluation;

import lightriders.ai.Player;
import lightriders.game.Board;

public interface IEvaluator {

	/**
	 * Gives a zero sum score for a given board and given player.
	 * 
	 * @param board
	 *            The board to evaluate
	 * @param player
	 *            The player's perspective to evaluate from
	 * @return A zero sum score
	 */
	double evaluateBoard(Board board, Player player);

}
