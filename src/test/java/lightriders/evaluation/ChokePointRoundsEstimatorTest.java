package lightriders.evaluation;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lightriders.ai.Player;
import lightriders.game.Board;
import lightriders.game.Move;

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

	@Test
	void testInfiniteLoop() {
		// Infinite loop detected during play testing.
		Board board = Board.start(4, 4, 1, 0, 3, 0).makeMove(Move.DOWN, Player.ZERO).makeMove(Move.LEFT, Player.ZERO)
				.makeMove(Move.UP, Player.ZERO).makeMove(Move.DOWN, Player.ONE);
		assertEquals(10, estimator.roundsLeft(board, Player.ONE));
	}

	@Test
	void testChooseMaxChokePoint() {
		Board board = Board.start(4, 4, 0, 3, 3, 1).makeMove(Move.LEFT, Player.ONE).makeMove(Move.LEFT, Player.ONE)
				.makeMove(Move.DOWN, Player.ONE).makeMove(Move.RIGHT, Player.ONE);
		assertEquals(6, estimator.roundsLeft(board, Player.ZERO));
	}

}
