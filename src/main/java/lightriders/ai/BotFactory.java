package lightriders.ai;

import lightriders.evaluation.ChokePointRoundsEstimator;
import lightriders.random.DeterministicMajorityStrategy;

public class BotFactory {

	/**
	 * Creates a fully configured bot for competition use.
	 * 
	 * @return A new bot
	 */
	public static IBot create() {
		return new SearchBot(6, 12, new DeterministicMajorityStrategy(), null, new ChokePointRoundsEstimator());
	}

}
