package lightriders.engine;

import lightriders.ai.IBot;
import lightriders.ai.Player;
import lightriders.game.Board;
import lightriders.game.Move;

class EngineInputHandler {

	private int width;

	private int height;

	private Player botPlayer;

	private Board currentBoard;

	private final IBot bot = IBot.newCompetitionBot();

	private int timePerMove;

	public Move action(String type, String time) {
		// TODO consider smarter time allocation calculations.
		return bot.bestMove(currentBoard, botPlayer, timePerMove);
	}

	public void update(String player, String type, String value) {
		switch (type) {
		case "field":
			currentBoard = Board.parseFromEngine(width, height, value);
			break;
		}

	}

	public void settings(String type, String value) {
		switch (type) {
		case "field_width":
			width = Integer.parseInt(value);
			break;
		case "field_height":
			height = Integer.parseInt(value);
			break;
		case "your_botid":
			botPlayer = Player.parseFromEngine(value);
			break;
		case "time_per_move":
			timePerMove = Integer.parseInt(value);
			break;
		}

	}

}
