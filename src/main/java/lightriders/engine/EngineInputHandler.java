package lightriders.engine;

import lightriders.ai.IBot;
import lightriders.ai.Player;
import lightriders.game.Board;
import lightriders.game.Move;

class EngineInputHandler {

	private final IBot bot;

	private final TimeAllocation allocator = new TimeAllocation();

	private int width;

	private int height;

	private Player botPlayer;

	private Board currentBoard;

	private int timePerMove;

	private int currentRound;

	/**
	 * @param bot
	 *            The bot to decide moves with
	 */
	public EngineInputHandler(IBot bot) {
		this.bot = bot;
	}

	/**
	 * Decides a move to play for the bot player.
	 * 
	 * @param time
	 *            Time the bot has to respond in milliseconds
	 * @return The chosen move
	 */
	public Move action(String time) {
		int timeToRespond = Integer.parseInt(time);
		return bot.bestMove(currentBoard, botPlayer,
				allocator.allocateEvenly(currentRound, timeToRespond, timePerMove, width, height));
	}

	/**
	 * Handles an update of the game state.
	 * 
	 * @param type
	 *            Engine input type
	 * @param value
	 *            Engine input value
	 */
	public void update(String type, String value) {
		switch (type) {
		case "field":
			currentBoard = Board.parseFromEngine(width, height, value);
			break;
		case "round":
			currentRound = Integer.parseInt(value);
			break;
		}

	}

	/**
	 * Handles a game setting.
	 * 
	 * @param type
	 *            Engine input type
	 * @param value
	 *            Engine input value
	 */
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
