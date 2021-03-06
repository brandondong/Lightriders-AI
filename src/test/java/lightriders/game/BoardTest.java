package lightriders.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import lightriders.ai.Player;

class BoardTest {

	@Test
	void testNoMovesAvailable() {
		Board board = Board.start(2, 1, 0, 0, 1, 0);
		assertTrue(board.possibleMovesFor(Player.ZERO).isEmpty());
		assertTrue(board.possibleMovesFor(Player.ONE).isEmpty());
	}

	@Test
	void testOnlyMovesAvailableForP1() {
		Board board = Board.start(3, 1, 1, 0, 2, 0);
		assertEquals(Collections.singletonList(Move.LEFT), board.possibleMovesFor(Player.ZERO));
		assertTrue(board.possibleMovesFor(Player.ONE).isEmpty());
	}

	@Test
	void testNoMovesAvailableAfterMoving() {
		Board board = Board.start(3, 1, 1, 0, 2, 0).makeMove(Move.LEFT, Player.ZERO);
		assertTrue(board.possibleMovesFor(Player.ZERO).isEmpty());
		assertTrue(board.possibleMovesFor(Player.ONE).isEmpty());
	}

	@Test
	void testParseStartingBoard() {
		Board board = Board.parseFromEngine(16, 16,
				".,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,0,.,.,.,.,.,.,.,.,1,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.");
		assertEquals(3, board.getX(Player.ZERO));
		assertEquals(12, board.getX(Player.ONE));
		assertEquals(7, board.getY(Player.ZERO));
		assertEquals(7, board.getY(Player.ONE));
		assertTrue(board.isFilled(3, 7));
		assertTrue(board.isFilled(12, 7));
		assertFalse(board.isFilled(12, 8));
	}

}
