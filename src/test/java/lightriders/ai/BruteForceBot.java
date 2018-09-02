package lightriders.ai;

import lightriders.evaluation.TerminalStateEvaluator;
import lightriders.game.Board;
import lightriders.game.Move;
import lightriders.random.IRandomStrategy;

class BruteForceBot implements IBot {

	private final SearchBotDepthHelper helper;

	private final PlayerSeparationCondition separationCondition = new PlayerSeparationCondition();

	public BruteForceBot(IRandomStrategy randomStrategy) {
		TerminalStateEvaluator terminal = new TerminalStateEvaluator();
		helper = new SearchBotDepthHelper(0, randomStrategy, terminal, terminal);
	}

	@Override
	public Move bestMove(Board board, Player player, int time) {
		return helper.bestMoveForDepth(board, player, separationCondition.checkSeparated(board),
				board.possibleMovesFor(player));
	}

}
