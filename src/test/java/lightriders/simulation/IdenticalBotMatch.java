package lightriders.simulation;

import lightriders.ai.BotTestFactory;
import lightriders.ai.IBot;
import lightriders.ai.Player;
import lightriders.game.Board;
import lightriders.game.Move;

class IdenticalBotMatch {

	private final IBot bot;

	private Board currentBoard;

	private IdenticalBotMatch(IBot bot, Board currentBoard) {
		this.bot = bot;
		this.currentBoard = currentBoard;
	}

	/**
	 * Starts a new instance of the bot match.
	 * 
	 * @param bot
	 *            The bot to play against itself
	 * @param board
	 *            A valid starting board to play, assumed that the bots start on
	 *            symmetrical left and right sides of the board
	 */
	public static void start(IBot bot, Board currentBoard) {
		IdenticalBotMatch match = new IdenticalBotMatch(bot, currentBoard);
		match.start();
	}

	private void start() {
		InteractiveBoard board = InteractiveBoard.start(currentBoard);
		while (!currentBoard.possibleMovesFor(Player.ZERO).isEmpty()) {
			Move m0 = bot.bestMove(currentBoard, Player.ZERO, BotTestFactory.TESTING_MOVE_TIME);
			Move m1 = mirrorMove(m0);
			board.makeMoves(m0, m1);
			currentBoard = currentBoard.makeMove(m0, Player.ZERO).makeMove(m1, Player.ONE);
		}
	}

	private Move mirrorMove(Move m) {
		if (m == Move.LEFT) {
			return Move.RIGHT;
		} else if (m == Move.RIGHT) {
			return Move.LEFT;
		}
		return m;
	}
}
