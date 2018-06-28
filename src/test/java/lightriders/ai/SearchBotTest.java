package lightriders.ai;

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
		SearchBot bot = new SearchBot(0, new DeterministicMajorityStrategy(), new TerminalStateEvaluator());
		assertEquals(Move.DOWN, bot.bestMove(FOUR_BY_FOUR_START, Player.ZERO));
		assertEquals(Move.DOWN, bot.bestMove(FOUR_BY_FOUR_START, Player.ONE));
	}

	@Test
	void testResponseToBadMove4by4() {
		SearchBot bot = new SearchBot(0, new DeterministicMajorityStrategy(), new TerminalStateEvaluator());
		Board board = FOUR_BY_FOUR_START.makeMove(Move.DOWN, Player.ZERO).makeMove(Move.RIGHT, Player.ONE);
		assertEquals(Move.LEFT, bot.bestMove(board, Player.ZERO));
	}

	@Test
	void testResponseToOtherBadMove4by4() {
		SearchBot bot = new SearchBot(0, new DeterministicMajorityStrategy(), new TerminalStateEvaluator());
		Board board = FOUR_BY_FOUR_START.makeMove(Move.DOWN, Player.ZERO).makeMove(Move.UP, Player.ONE);
		assertEquals(Move.LEFT, bot.bestMove(board, Player.ZERO));
	}

}
