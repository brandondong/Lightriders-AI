package lightriders.evaluation;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lightriders.ai.Player;
import lightriders.game.Board;

class ChokePointRoundsEstimatorTest {

	private ChokePointRoundsEstimator estimator;

	@BeforeEach
	void setup() {
		estimator = new ChokePointRoundsEstimator();
	}

	@Test
	void testEstimateNoChokePoints() {
		Board board = Board.start(4, 4, 0, 0, 3, 0);
		assertEquals(14, estimator.roundsLeft(board, Player.ZERO));
		assertEquals(14, estimator.roundsLeft(board, Player.ONE));
	}

}
