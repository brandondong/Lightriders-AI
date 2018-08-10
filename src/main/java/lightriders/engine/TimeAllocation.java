package lightriders.engine;

class TimeAllocation {

	/**
	 * Calculates the amount of time to use for this round if split evenly and
	 * assuming all rounds played.
	 * 
	 * @param currentRound
	 *            The current round number given by the engine
	 * @param timeToRespond
	 *            Time required to respond by for this round
	 * @param timePerMove
	 *            Time given per move
	 * @param width
	 *            Width of the board
	 * @param height
	 *            Height of the board
	 * @return The amount of time to use
	 */
	public int allocateEvenly(int currentRound, int timeToRespond, int timePerMove, int width, int height) {
		if (timeToRespond < timePerMove) {
			// Use up the full time. The next round will be given the time per move.
			return timeToRespond;
		}
		int maxRounds = (width * height - 2) / 2;
		// Current round is zero indexed.
		int roundsLeft = maxRounds - currentRound;
		// Possible division by zero but the very last round doesn't matter. Guaranteed
		// draw.
		return timePerMove + Math.floorDiv(timeToRespond - timePerMove, roundsLeft);
	}

}
