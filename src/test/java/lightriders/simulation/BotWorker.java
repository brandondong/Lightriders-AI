package lightriders.simulation;

import javax.swing.SwingWorker;

import lightriders.ai.Player;
import lightriders.ai.SearchBot;
import lightriders.evaluation.TerminalStateEvaluator;
import lightriders.game.Board;
import lightriders.game.Move;
import lightriders.random.DeterministicMajorityStrategy;

class BotWorker extends SwingWorker<Move, Void> {

	private final SearchBot bot = new SearchBot(0, 0, new DeterministicMajorityStrategy(), new TerminalStateEvaluator(),
			new TerminalStateEvaluator());

	private final Board board;

	private final Player player;

	public BotWorker(Board board, Player player) {
		this.board = board;
		this.player = player;
	}

	@Override
	protected Move doInBackground() {
		return bot.bestMove(board, player);
	}

}
