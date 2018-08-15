package lightriders.simulation;

import javax.swing.SwingUtilities;

import lightriders.ai.IBot;
import lightriders.ai.Player;
import lightriders.game.Board;
import lightriders.game.Move;

class HumanBotMatch {

	private final Player human;

	private BotWorker worker;

	private final IBot bot;

	private final InteractiveBoard interactiveBoard;

	/**
	 * @param bot
	 *            The bot to play against
	 * @param board
	 *            The board to play
	 * @param human
	 *            The player that the human will control
	 */
	public HumanBotMatch(IBot bot, Board board, Player human) {
		this.bot = bot;
		this.human = human;
		interactiveBoard = new InteractiveBoard(board, this::handleMouseClick);
		startBackgroundBotWorker(board);
	}

	/**
	 * Starts a new instance of the interactive match.
	 */
	public void start() {
		interactiveBoard.create();
	}

	private void handleMouseClick(int x, int y, Board currentBoard) {
		int p0x = currentBoard.getX(human);
		int p0y = currentBoard.getY(human);
		Move m;
		if (x == p0x && y == p0y - 1) {
			m = Move.UP;
		} else if (x == p0x && y == p0y + 1) {
			m = Move.DOWN;
		} else if (x == p0x - 1 && y == p0y) {
			m = Move.LEFT;
		} else if (x == p0x + 1 && y == p0y) {
			m = Move.RIGHT;
		} else {
			return;
		}
		if (!currentBoard.possibleMovesFor(human).contains(m)) {
			return;
		}
		if (worker.isDone()) {
			Move botMove = blockForWorkerMove();
			makeMoves(currentBoard, m, botMove);
		} else {
			interactiveBoard.setProcessingState("Loading Opponent Move...");
			new Thread(() -> {
				Move botMove = blockForWorkerMove();
				SwingUtilities.invokeLater(() -> {
					makeMoves(currentBoard, m, botMove);
				});
			}).start();
		}
	}

	private void makeMoves(Board currentBoard, Move m, Move botMove) {
		Board nextBoard = currentBoard.makeMove(m, human).makeMove(botMove, human.opponent());
		if (human == Player.ZERO) {
			interactiveBoard.makeMoves(m, botMove);
		} else {
			interactiveBoard.makeMoves(botMove, m);
		}
		startBackgroundBotWorker(nextBoard);
	}

	private Move blockForWorkerMove() {
		try {
			return worker.get();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private void startBackgroundBotWorker(Board currentBoard) {
		worker = new BotWorker(bot, currentBoard, human.opponent());
		worker.execute();
	}

}
