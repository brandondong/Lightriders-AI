package lightriders.ai;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lightriders.evaluation.IEvaluator;
import lightriders.evaluation.IRoundsEstimator;
import lightriders.game.Board;
import lightriders.game.Move;
import lightriders.random.IRandomStrategy;

class SearchBot implements IBot {

	private final IRandomStrategy randomStrategy;

	private final IEvaluator evaluator;

	private final IRoundsEstimator estimator;

	private final PlayerSeparationCondition separationCondition = new PlayerSeparationCondition();

	/**
	 * @param randomStrategy
	 *            A strategy for choosing moves based on calculated optimal
	 *            probabilities
	 * @param evaluator
	 *            Board evaluation function
	 * @param estimator
	 *            Separated rounds left estimation function
	 */
	public SearchBot(IRandomStrategy randomStrategy, IEvaluator evaluator, IRoundsEstimator estimator) {
		this.randomStrategy = randomStrategy;
		this.evaluator = evaluator;
		this.estimator = estimator;
	}

	@Override
	public Move bestMove(Board board, Player player, int time) {
		List<Move> moves = board.possibleMovesFor(player);
		boolean separated = separationCondition.checkSeparated(board);
		long nanoStart = System.nanoTime();
		// Use iterative deepening to search to the appropriate depth for the time
		// limit.
		for (int depth = 1;; depth++) {
			SearchBotDepthHelper helper = new SearchBotDepthHelper(depth, randomStrategy, evaluator, estimator);
			Move m = helper.bestMoveForDepth(board, player, separated, moves);
			long nanoCurrent = System.nanoTime();
			long millisElapsed = TimeUnit.NANOSECONDS.toMillis(nanoCurrent - nanoStart);
			if (millisElapsed >= time) {
				return m;
			}
		}
	}

}
