package lightriders.ai;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lightriders.game.Board;
import lightriders.game.Move;

class ShortestPathEvaluatorTest {

	private static final double TOL = 0.0001;

	private ShortestPathEvaluator evaluator;

	@BeforeEach
	void setup() {
		evaluator = new ShortestPathEvaluator();
	}

	@Test
	void test3By3() {
		Board board = Board.start(3, 3, 0, 1, 1, 1);
		assertEquals(-3, evaluator.evaluateBoard(board, Player.ZERO), TOL);
	}

	@Test
	void test4By4Even() {
		Board board = Board.start(4, 4, 1, 1, 2, 1);
		assertEquals(0, evaluator.evaluateBoard(board, Player.ZERO), TOL);
	}

	@Test
	void test4By4Uneven() {
		Board board = Board.start(4, 4, 0, 0, 3, 2);
		assertEquals(2, evaluator.evaluateBoard(board, Player.ONE), TOL);
	}

	@Test
	void test4By4AfterMove() {
		Board board = Board.start(4, 4, 1, 1, 2, 1);
		board = board.makeMove(Move.UP, Player.ZERO).makeMove(Move.DOWN, Player.ONE);
		assertEquals(-4, evaluator.evaluateBoard(board, Player.ZERO), TOL);
	}

}
