package lightriders.lp;

public class ZeroSumNashEquilibriumSolver {

	private final SimplexSolver simplexSolver = new SimplexSolver();

	/**
	 * Calculates the Nash equilibrium value of a zero sum payoff matrix.
	 * 
	 * @param matrix
	 *            A payoff matrix for a zero sum game
	 * @return The Nash equilibrium payoff
	 */
	public double findNashEquilibriumValue(double[][] matrix) {
		return solveAssociatedLP(matrix).objectiveValue();
	}

	/**
	 * Calculates a Nash equilibrium strategy in a zero sum game.
	 * 
	 * @param matrix
	 *            A row player payoff matrix for a zero sum game indexed in the form
	 *            [row][column]
	 * @return An array of probabilities corresponding to each of the row player's
	 *         actions
	 */
	public double[] findNashEquilibriumStrategy(double[][] matrix) {
		return solveAssociatedLP(matrix).variableValues();
	}

	private LPResult solveAssociatedLP(double[][] matrix) {
		int numRows = matrix.length;
		int numColumns = matrix[0].length;
		int numConstraints = numColumns + 1;
		int numVariables = 2 + numRows + numColumns;

		double[][] a = new double[numConstraints][numVariables];
		double[] b = new double[numConstraints];
		double[] c = new double[numVariables];

		c[0] = 1;
		c[1] = -1;
		b[numConstraints - 1] = 1;
		for (int i = 0; i < numRows; i++) {
			// Probabilities sum to 1.
			a[numConstraints - 1][2 + i] = 1;
		}
		for (int i = 0; i < numConstraints - 1; i++) {
			a[i][0] = 1;
			a[i][1] = -1;
			// Slack variables.
			a[i][2 + numRows + i] = 1;
		}
		for (int row = 0; row < numRows; row++) {
			for (int column = 0; column < numColumns; column++) {
				a[column][2 + row] = matrix[row][column];
			}
		}
		return simplexSolver.findSolution(c, a, b);
	}

}
