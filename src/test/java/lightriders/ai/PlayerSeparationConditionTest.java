package lightriders.ai;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lightriders.game.Board;
import lightriders.game.Move;

class PlayerSeparationConditionTest {

	private PlayerSeparationCondition condition;

	@BeforeEach
	void setup() {
		condition = new PlayerSeparationCondition();
	}

	@Test
	void testPlayersBesideEachOther() {
		Board board = Board.start(4, 4, 1, 1, 2, 1);
		assertFalse(condition.checkSeparated(board));
	}

	@Test
	void testPlayersSeparated() {
		Board board = Board.start(6, 1, 1, 0, 2, 0).makeMove(Move.RIGHT, Player.ONE);
		assertTrue(condition.checkSeparated(board));
	}

	@Test
	void testPlayersNeighborsSeparated() {
		Board board = Board.start(4, 2, 1, 0, 2, 0).makeMove(Move.DOWN, Player.ZERO).makeMove(Move.DOWN, Player.ONE);
		assertTrue(condition.checkSeparated(board));
	}

	@Test
	void testMultiplePaths() {
		Board board = Board.start(4, 4, 0, 0, 3, 3);
		assertFalse(condition.checkSeparated(board));
	}

}
