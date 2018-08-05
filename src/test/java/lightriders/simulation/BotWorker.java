package lightriders.simulation;

import javax.swing.SwingWorker;

import lightriders.ai.IBot;
import lightriders.ai.Player;
import lightriders.game.Board;
import lightriders.game.Move;

class BotWorker extends SwingWorker<Move, Void> {

	private final IBot bot;

	private final Board board;

	private final Player player;

	public BotWorker(IBot bot, Board board, Player player) {
		this.bot = bot;
		this.board = board;
		this.player = player;
	}

	@Override
	protected Move doInBackground() {
		return bot.bestMove(board, player, Integer.MAX_VALUE);
	}

}
