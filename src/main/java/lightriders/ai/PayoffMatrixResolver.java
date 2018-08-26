package lightriders.ai;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import lightriders.lp.ZeroSumNashEquilibriumSolver;

class PayoffMatrixResolver<T> {

	private static interface PayoffMatrixReduction<E> {

		E reduce(double[][] payoffMatrix);
	}

	private final ZeroSumNashEquilibriumSolver solver = new ZeroSumNashEquilibriumSolver();

	/**
	 * Calculates a Nash equilibrium strategy in a zero sum payoff matrix resolved
	 * dynamically for pruning.
	 * 
	 * @param positions
	 *            A non-empty matrix of game positions
	 * @param positionEvaluator
	 *            Evaluation function for a single matrix cell
	 * @return An array of probabilities corresponding to each of the row player's
	 *         actions
	 */
	public double[] findNashEquilibriumStrategy(T[][] positions, Function<T, Double> positionEvaluator) {
		return fillPayoffMatrix(positions, positionEvaluator, (value, row) -> {
			double[] probabilities = new double[positions.length];
			probabilities[row] = 1;
			return probabilities;
		}, solver::findNashEquilibriumStrategy);
	}

	/**
	 * Calculates the Nash equilibrium value of a zero sum payoff matrix resolved
	 * dynamically for pruning.
	 * 
	 * @param positions
	 *            A non-empty matrix of game positions
	 * @param positionEvaluator
	 *            Evaluation function for a single matrix cell
	 * @return The Nash equilibrium payoff
	 */
	public double findNashEquilibriumValue(T[][] positions, Function<T, Double> positionEvaluator) {
		return fillPayoffMatrix(positions, positionEvaluator, (value, row) -> {
			return value;
		}, solver::findNashEquilibriumValue);
	}

	private <E> E fillPayoffMatrix(T[][] positions, Function<T, Double> positionEvaluator,
			BiFunction<Double, Integer, E> prunedResult, PayoffMatrixReduction<E> matrixReduction) {
		// Instead of naively filling out the payoff matrix by evaluating all the cells,
		// search in some order to maximize chances of finding early pure equilibria.
		int numRows = positions.length;
		int numColumns = positions[0].length;
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
							return prunedResult.apply(value, row);
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
			payoffMatrix[bestRow][bestColumn] = positionEvaluator.apply(positions[bestRow][bestColumn]);
			hasBeenEvaluated[bestRow][bestColumn] = true;
			numEmptyInRow[bestRow]--;
			numEmptyInColumn[bestColumn]--;
			leastInRow[bestRow] = Math.min(leastInRow[bestRow], payoffMatrix[bestRow][bestColumn]);
			maxInColumn[bestColumn] = Math.max(maxInColumn[bestColumn], payoffMatrix[bestRow][bestColumn]);
		}
		return matrixReduction.reduce(payoffMatrix);
	}

}
