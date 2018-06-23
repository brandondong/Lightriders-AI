package lightriders.ai;

import java.util.function.BiFunction;

import lightriders.lp.ZeroSumNashEquilibriumSolver;

class SimultaneousMinmax<T> {

	private final int depth;

	private final ZeroSumNashEquilibriumSolver solver = new ZeroSumNashEquilibriumSolver();

	private final BiFunction<T, Player, T[][]> possibleMoves;

	private final BiFunction<T, Player, Double> evaluation;

	/**
	 * @param depth
	 *            The depth to search to or 0 for no limit
	 * @param possibleMoves
	 *            Generates a matrix of possible next positions with the specified
	 *            player as the row actions
	 * @param evaluation
	 *            Assigns a score to a given position from the specified player's
	 *            perspective
	 */
	public SimultaneousMinmax(int depth, BiFunction<T, Player, T[][]> possibleMoves,
			BiFunction<T, Player, Double> evaluation) {
		this.depth = depth;
		this.possibleMoves = possibleMoves;
		this.evaluation = evaluation;
	}

	/**
	 * Finds the optimal decision probabilities for the specified player and
	 * position.
	 * 
	 * @param position
	 *            The specified position where the game is not over
	 * @param player
	 *            The player to optimize for
	 * @return An array of probabilities corresponding to each of the player's
	 *         possible decisions
	 */
	public double[] optimalDecisionProbabilities(T position, Player player) {
		T[][] nextPositions = possibleMoves.apply(position, player);
		int numRows = nextPositions.length;
		int numColumns = nextPositions[0].length;
		double[][] payoffMatrix = new double[numRows][numColumns];
		for (int row = 0; row < numRows; row++) {
			for (int column = 0; column < numColumns; column++) {
				payoffMatrix[row][column] = minmax(depth - 1, nextPositions[row][column], player);
			}
		}
		return solver.findNashEquilibriumStrategy(payoffMatrix);
	}

	private double minmax(int depth, T position, Player player) {
		if (depth == 0) {
			return evaluation.apply(position, player);
		}
		T[][] nextPositions = possibleMoves.apply(position, player);
		int numRows = nextPositions.length;
		// Check if the game is over.
		if (numRows == 0) {
			return evaluation.apply(position, player);
		}
		// If there are any rows, there must be columns in the matrix.
		int numColumns = nextPositions[0].length;
		double[][] payoffMatrix = new double[numRows][numColumns];
		for (int row = 0; row < numRows; row++) {
			for (int column = 0; column < numColumns; column++) {
				payoffMatrix[row][column] = minmax(depth - 1, nextPositions[row][column], player);
			}
		}
		return solver.findNashEquilibriumValue(payoffMatrix);
	}

}
