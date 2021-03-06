package lightriders.simulation;

import java.util.concurrent.CompletableFuture;

import javax.swing.SwingUtilities;

import lightriders.ai.IBot;
import lightriders.ai.Player;
import lightriders.game.Board;
import lightriders.game.Move;

class HumanBotMatch {

	private final Player human;

	private BotWorker worker;

	private final IBot bot;

	private InteractiveBoard interactiveBoard;

	private final Board startingBoard;

	private HumanBotMatch(IBot bot, Board board, Player human) {
		this.bot = bot;
		this.human = human;
		startingBoard = board;
		startBackgroundBotWorker(board);
	}

	/**
	 * Starts a new instance of the interactive match.
	 * 
	 * @param bot
	 *            The bot to play against
	 * @param board
	 *            The board to play
	 * @param human
	 *            The player that the human will control
	 */
	public static void start(IBot bot, Board board, Player human) {
		HumanBotMatch match = new HumanBotMatch(bot, board, human);
		match.start();
	}

	private void start() {
		interactiveBoard = InteractiveBoard.start(startingBoard, this::handleMouseClick);
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
			CompletableFuture.runAsync(() -> {
				Move botMove = blockForWorkerMove();
				SwingUtilities.invokeLater(() -> {
					makeMoves(currentBoard, m, botMove);
				});
			});
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
