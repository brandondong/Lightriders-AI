package lightriders.engine;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import lightriders.ai.Player;
import lightriders.game.Board;
import lightriders.game.Move;

class EngineInputHandlerTest {

	@Test
	void testGameStart() {
		String field = ".,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,0,.,.,.,.,.,.,.,.,1,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.";
		Board expected = Board.parseFromEngine(16, 16, field);
		EngineInputHandler handler = new EngineInputHandler((b, p, t) -> {
			assertEquals(expected, b);
			assertEquals(Player.ZERO, p);
			return Move.RIGHT;
		});

		handler.settings("timebank", "10000");
		handler.settings("time_per_move", "200");
		handler.settings("player_names", "player0,player1");
		handler.settings("your_bot", "player0");
		handler.settings("your_botid", "0");
		handler.settings("field_width", "16");
		handler.settings("field_height", "16");

		handler.update("round", "0");
		handler.update("field", field);
		assertEquals(Move.RIGHT, handler.action("10000"));
	}

}
