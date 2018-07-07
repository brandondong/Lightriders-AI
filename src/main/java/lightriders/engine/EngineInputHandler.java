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

	public Move action(String type, String time) {
		// TODO consider passing time to factory bot method.
		return bot.bestMove(currentBoard, botPlayer);
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
		}

	}

}
