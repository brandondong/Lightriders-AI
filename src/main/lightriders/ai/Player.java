package lightriders.ai;

public enum Player {
	ZERO, ONE;

	public Player opponent() {
		switch (this) {
		case ZERO:
			return ONE;
		case ONE:
			return ZERO;
		}
		throw new RuntimeException();
	}

	public static Player parseFromEngine(String value) {
		return value.equals("0") ? ZERO : ONE;
	}
}
