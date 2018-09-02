package lightriders.ai;

import lightriders.random.DeterministicMajorityStrategy;

public class BotTestFactory {

	public static final int TESTING_MOVE_TIME = 200;

	public static IBot newBruteForceBot() {
		return new BruteForceBot(new DeterministicMajorityStrategy());
	}

}
