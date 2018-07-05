package lightriders.ai;

import lightriders.game.Board;
import lightriders.game.Move;

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

}