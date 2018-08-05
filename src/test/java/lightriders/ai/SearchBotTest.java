package lightriders.ai;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import lightriders.evaluation.TerminalStateEvaluator;
import lightriders.game.Board;
import lightriders.game.Move;
import lightriders.random.DeterministicMajorityStrategy;

class SearchBotTest {

	private static final Board FOUR_BY_FOUR_START = Board.start(4, 4, 1, 1, 2, 1);

	@Test
	void testFirstMove4by4() {
		TerminalStateEvaluator terminal = new TerminalStateEvaluator();
		SearchBot bot = new SearchBot(0, 0, new DeterministicMajorityStrategy(), terminal, terminal);
		assertEquals(Move.DOWN, bot.bestMove(FOUR_BY_FOUR_START, Player.ZERO, Integer.MAX_VALUE));
		assertEquals(Move.DOWN, bot.bestMove(FOUR_BY_FOUR_START, Player.ONE, Integer.MAX_VALUE));
	}

	@Test
	void testResponseToBadMove4by4() {
		TerminalStateEvaluator terminal = new TerminalStateEvaluator();
		SearchBot bot = new SearchBot(0, 0, new DeterministicMajorityStrategy(), terminal, terminal);
		Board board = FOUR_BY_FOUR_START.makeMove(Move.DOWN, Player.ZERO).makeMove(Move.RIGHT, Player.ONE);
		assertEquals(Move.LEFT, bot.bestMove(board, Player.ZERO, Integer.MAX_VALUE));
	}

	@Test
	void testResponseToOtherBadMove4by4() {
		TerminalStateEvaluator terminal = new TerminalStateEvaluator();
		SearchBot bot = new SearchBot(0, 0, new DeterministicMajorityStrategy(), terminal, terminal);
		Board board = FOUR_BY_FOUR_START.makeMove(Move.DOWN, Player.ZERO).makeMove(Move.UP, Player.ONE);
		assertEquals(Move.LEFT, bot.bestMove(board, Player.ZERO, Integer.MAX_VALUE));
	}

	@Test
	void testUseSeparatedStrategy() {
		Board board = Board.start(6, 2, 2, 0, 3, 0).makeMove(Move.DOWN, Player.ZERO).makeMove(Move.LEFT, Player.ZERO);
		SearchBot bot = new SearchBot(0, 0, new DeterministicMajorityStrategy(), (b, p) -> {
			throw new RuntimeException();
		}, new TerminalStateEvaluator());
		Move m = bot.bestMove(board, Player.ZERO, Integer.MAX_VALUE);
		assertTrue(m == Move.LEFT || m == Move.UP);
	}

}
