package lightriders.ai;

import java.util.Arrays;
import java.util.function.BiFunction;

import lightriders.lp.ZeroSumNashEquilibriumSolver;

class SimultaneousMinmax<T> {

	private static class PossiblePruneResult {

		private double[] probabilities;

		private double value;

		private double[][] payoffMatrix;

		public PossiblePruneResult(double[] probabilities, double value) {
			this.probabilities = probabilities;
			this.value = value;
		}

		public PossiblePruneResult(double[][] payoffMatrix) {
			this.payoffMatrix = payoffMatrix;
		}

		public boolean wasPureEquilibriumFound() {
			return probabilities != null;
		}

		public double[] getProbabilities() {
			return probabilities;
		}

		public double getValue() {
			return value;
		}

		public double[][] getPayoffMatrix() {
			return payoffMatrix;
		}
	}

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
		PossiblePruneResult result = fillPayoffMatrix(depth, player, nextPositions, numRows, numColumns);
		if (result.wasPureEquilibriumFound()) {
			return result.getProbabilities();
		}
		return solver.findNashEquilibriumStrategy(result.getPayoffMatrix());
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
		PossiblePruneResult result = fillPayoffMatrix(depth, player, nextPositions, numRows, numColumns);
		if (result.wasPureEquilibriumFound()) {
			return result.getValue();
		}
		return solver.findNashEquilibriumValue(result.getPayoffMatrix());
	}

	private PossiblePruneResult fillPayoffMatrix(int depth, Player player, T[][] nextPositions, int numRows,
			int numColumns) {
		// Instead of naively filling out the payoff matrix by evaluating all the
		// children recursively, search in some order to maximize chances of finding
		// early pure equilibria.
		double[] leastInRow = new double[numRows];
		Arrays.fill(leastInRow, Double.MAX_VALUE);
		double[] maxInColumn = new double[numColumns];
		Arrays.fill(maxInColumn, -Double.MAX_VALUE);
		int[] numEmptyInRow = new int[numRows];
		Arrays.fill(numEmptyInRow, numColumns);
		int[] numEmptyInColumn = new int[numColumns];
		Arrays.fill(numEmptyInColumn, numRows);
		boolean[][] hasBeenEvaluated = new boolean[numRows][numColumns];

		double[][] payoffMatrix = new double[numRows][numColumns];
		for (int i = 0; i < numRows * numColumns; i++) {
			// Search for a good candidate to evaluate.
			int bestRow = -1;
			int bestColumn = -1;
			int lowestEmpty = Integer.MAX_VALUE;
			for (int row = 0; row < numRows; row++) {
				for (int column = 0; column < numColumns; column++) {
					double value = payoffMatrix[row][column];
					if (hasBeenEvaluated[row][column] && value == maxInColumn[column] && value == leastInRow[row]) {
						// Potential pure Nash equilibrium.
						int numEmpty = numEmptyInRow[row] + numEmptyInColumn[column];
						if (numEmpty == 0) {
							double[] probabilities = new double[numRows];
							probabilities[row] = 1;
							return new PossiblePruneResult(probabilities, value);
						} else if (numEmpty < lowestEmpty) {
							lowestEmpty = numEmpty;
							if (numEmptyInRow[row] > 0) {
								// Search row for candidate to evaluate.
								bestRow = row;
								for (int emptyC = 0; emptyC < numColumns; emptyC++) {
									if (!hasBeenEvaluated[row][emptyC]) {
										bestColumn = emptyC;
										break;
									}
								}

							} else {
								// Otherwise column.
								bestColumn = column;
								for (int emptyR = 0; emptyR < numRows; emptyR++) {
									if (!hasBeenEvaluated[emptyR][column]) {
										bestRow = emptyR;
										break;
									}
								}
							}
						}
					}
				}
			}
			if (bestRow == -1) {
				// Didn't find any potential pure equilibrium to confirm. Fill a blank entry.
				for (int row = 0; row < numRows; row++) {
					for (int column = 0; column < numColumns; column++) {
						if (!hasBeenEvaluated[row][column]) {
							int numEmpty = numEmptyInRow[row] + numEmptyInColumn[column];
							if (numEmpty < lowestEmpty) {
								lowestEmpty = numEmpty;
								bestRow = row;
								bestColumn = column;
							}
						}
					}
				}
			}
			payoffMatrix[bestRow][bestColumn] = minmax(depth - 1, nextPositions[bestRow][bestColumn], player);
			hasBeenEvaluated[bestRow][bestColumn] = true;
			numEmptyInRow[bestRow]--;
			numEmptyInColumn[bestColumn]--;
			leastInRow[bestRow] = Math.min(leastInRow[bestRow], payoffMatrix[bestRow][bestColumn]);
			maxInColumn[bestColumn] = Math.max(maxInColumn[bestColumn], payoffMatrix[bestRow][bestColumn]);
		}
		return new PossiblePruneResult(payoffMatrix);
	}

}
