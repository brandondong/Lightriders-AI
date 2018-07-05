package lightriders.ai;

import lightriders.evaluation.IEvaluator;
import lightriders.evaluation.IRoundsEstimator;
import lightriders.random.IRandomStrategy;

public class BotTestFactory {

	public static IBot create(int depth, int separatedDepth, IRandomStrategy randomStrategy, IEvaluator evaluator,
			IRoundsEstimator estimator) {
		return new SearchBot(depth, separatedDepth, randomStrategy, evaluator, estimator);
	}

}
