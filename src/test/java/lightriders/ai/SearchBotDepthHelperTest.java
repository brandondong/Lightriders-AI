package lightriders.ai;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import lightriders.evaluation.TerminalStateEvaluator;
import lightriders.game.Board;
import lightriders.game.Move;
import lightriders.random.DeterministicMajorityStrategy;

class SearchBotDepthHelperTest {

	private static final Board FOUR_BY_FOUR_START = Board.start(4, 4, 1, 1, 2, 1);

	@Test
	void testFirstMove4by4() {
		TerminalStateEvaluator terminal = new TerminalStateEvaluator();
		SearchBotDepthHelper bot = new SearchBotDepthHelper(0, new DeterministicMajorityStrategy(), terminal, terminal);
		assertEquals(Move.DOWN, bot.bestMoveForDepth(FOUR_BY_FOUR_START, Player.ZERO, false,
				FOUR_BY_FOUR_START.possibleMovesFor(Player.ZERO)));
		assertEquals(Move.DOWN, bot.bestMoveForDepth(FOUR_BY_FOUR_START, Player.ONE, false,
				FOUR_BY_FOUR_START.possibleMovesFor(Player.ZERO)));
	}

	@Test
	void testResponseToBadMove4by4() {
		TerminalStateEvaluator terminal = new TerminalStateEvaluator();
		SearchBotDepthHelper bot = new SearchBotDepthHelper(0, new DeterministicMajorityStrategy(), terminal, terminal);
		Board board = FOUR_BY_FOUR_START.makeMove(Move.DOWN, Player.ZERO).makeMove(Move.RIGHT, Player.ONE);
		assertEquals(Move.LEFT, bot.bestMoveForDepth(board, Player.ZERO, false, board.possibleMovesFor(Player.ZERO)));
	}

	@Test
	void testResponseToOtherBadMove4by4() {
		TerminalStateEvaluator terminal = new TerminalStateEvaluator();
		SearchBotDepthHelper bot = new SearchBotDepthHelper(0, new DeterministicMajorityStrategy(), terminal, terminal);
		Board board = FOUR_BY_FOUR_START.makeMove(Move.DOWN, Player.ZERO).makeMove(Move.UP, Player.ONE);
		assertEquals(Move.LEFT, bot.bestMoveForDepth(board, Player.ZERO, false, board.possibleMovesFor(Player.ZERO)));
	}

	@Test
	void testUseSeparatedStrategy() {
		Board board = Board.start(6, 2, 2, 0, 3, 0).makeMove(Move.DOWN, Player.ZERO).makeMove(Move.LEFT, Player.ZERO);
		SearchBotDepthHelper bot = new SearchBotDepthHelper(0, new DeterministicMajorityStrategy(), (b, p) -> {
			throw new RuntimeException();
		}, new TerminalStateEvaluator());
		Move m = bot.bestMoveForDepth(board, Player.ZERO, true, board.possibleMovesFor(Player.ZERO));
		assertTrue(m == Move.LEFT || m == Move.UP);
	}

}
