package lightriders.ai;

import lightriders.evaluation.IEvaluator;
import lightriders.evaluation.IRoundsEstimator;
import lightriders.evaluation.TerminalStateEvaluator;
import lightriders.random.IRandomStrategy;

public class BotTestFactory {

	/**
	 * Creates a new bot with the specified parameters.
	 * 
	 * @param depth
	 *            The depth to search to when not separated or 0 for no limit
	 * @param separatedDepth
	 *            The depth to search to when separated or 0 for no limit
	 * @param randomStrategy
	 *            A strategy for choosing moves based on calculated optimal
	 *            probabilities
	 * @param evaluator
	 *            Board evaluation function
	 * @param estimator
	 *            Separated rounds left estimation function
	 * @return A new bot
	 */
	public static IBot create(int depth, int separatedDepth, IRandomStrategy randomStrategy, IEvaluator evaluator,
			IRoundsEstimator estimator) {
		return new SearchBot(depth, separatedDepth, randomStrategy, evaluator, estimator);
	}

	/**
	 * Creates a bot that uses a purely brute force strategy.
	 * 
	 * @param random
	 *            The random strategy to use
	 * @return A new bot
	 */
	public static IBot bruteForce(IRandomStrategy random) {
		TerminalStateEvaluator terminal = new TerminalStateEvaluator();
		return new SearchBot(0, 0, random, terminal, terminal);
	}

}
