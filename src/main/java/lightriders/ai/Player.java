package lightriders.ai;

public enum Player {
	ZERO, ONE;

	/**
	 * Parses the specified player from the engine input.
	 * 
	 * @param value
	 *            The engine input
	 * @return The matching player
	 */
	public static Player parseFromEngine(String value) {
		return value.equals("0") ? ZERO : ONE;
	}

	/**
	 * @return The opponent player.
	 */
	public Player opponent() {
		switch (this) {
		case ZERO:
			return ONE;
		case ONE:
			return ZERO;
		}
		throw new RuntimeException();
	}
}
