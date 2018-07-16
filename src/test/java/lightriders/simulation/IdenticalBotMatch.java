package lightriders.simulation;

import lightriders.ai.IBot;
import lightriders.ai.Player;
import lightriders.game.Board;
import lightriders.game.Move;

class IdenticalBotMatch {

	private final InteractiveBoard board;

	private final IBot bot;

	private Board currentBoard;

	/**
	 * @param bot
	 *            The bot to play against itself
	 * @param board
	 *            A valid starting board to play
	 */
	public IdenticalBotMatch(IBot bot, Board currentBoard) {
		this.bot = bot;
		this.currentBoard = currentBoard;
		board = new InteractiveBoard(currentBoard);
	}

	/**
	 * Starts a new instance of the bot match.
	 */
	public void start() {
		board.create();
		while (!currentBoard.possibleMovesFor(Player.ZERO).isEmpty()) {
			Move m0 = bot.bestMove(currentBoard, Player.ZERO);
			Move m1;
			if (m0 == Move.LEFT) {
				m1 = Move.RIGHT;
			} else if (m0 == Move.RIGHT) {
				m1 = Move.LEFT;
			} else {
				m1 = m0;
			}
			board.makeMoves(m0, m1);
			currentBoard = currentBoard.makeMove(m0, Player.ZERO).makeMove(m1, Player.ONE);
		}
	}
}
