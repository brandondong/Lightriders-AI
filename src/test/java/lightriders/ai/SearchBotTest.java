package lightriders.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lightriders.evaluation.ChokePointRoundsEstimator;
import lightriders.game.Board;
import lightriders.game.Move;
import lightriders.random.DeterministicMajorityStrategy;

class SearchBotTest {

	private SearchBot bot;

	@BeforeEach
	void setup() {
		bot = new SearchBot(new DeterministicMajorityStrategy(), new ShortestPathEvaluator(),
				new ChokePointRoundsEstimator());
	}

	@Test
	void testSingleChoiceReturnsInstantly() {
		Board board = Board.start(10, 1, 0, 0, 5, 0);
		assertEquals(Move.RIGHT, bot.bestMove(board, Player.ZERO, Integer.MAX_VALUE));
	}

}
