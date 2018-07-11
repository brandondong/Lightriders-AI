package lightriders.simulation;

import lightriders.game.Board;

interface IBoardMouseListener {

	/**
	 * Handles a mouse click event on a game board.
	 * 
	 * @param x
	 *            The selected board cell's x position
	 * @param y
	 *            The selected board cell's y position
	 * @param currentBoard
	 *            The current game board
	 */
	void boardClick(int x, int y, Board currentBoard);

}
