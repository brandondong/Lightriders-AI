package lightriders.ai;

public enum Player {
	ONE, TWO;

	public Player opponent() {
		switch (this) {
		case ONE:
			return TWO;
		case TWO:
			return ONE;
		}
		throw new RuntimeException();
	}
}
