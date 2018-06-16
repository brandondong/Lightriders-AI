package lightriders.lp;

import java.util.Arrays;

import lightriders.equilibrium.INashEquilibriumSolver;

public class ZeroSumNashEquilibriumSolver implements INashEquilibriumSolver {

	private final SimplexSolver simplexSolver = new SimplexSolver();

	/* (non-Javadoc)
	 * @see lightriders.lp.INashEquilibriumSolver#findNashEquilibriumValue(double[][])
	 */
	@Override
	public double findNashEquilibriumValue(double[][] matrix) {
		double[] variableValues = solveAssociatedLP(matrix);
		return variableValues[0] - variableValues[1];
	}

	/* (non-Javadoc)
	 * @see lightriders.lp.INashEquilibriumSolver#findNashEquilibriumStrategy(double[][])
	 */
	@Override
	public double[] findNashEquilibriumStrategy(double[][] matrix) {
		double[] variableValues = solveAssociatedLP(matrix);
		int numRows = matrix.length;
		return Arrays.copyOfRange(variableValues, 2, 2 + numRows);
	}

	private double[] solveAssociatedLP(double[][] matrix) {
		// LP explanation here: http://www.fuhuthu.com/CPSC420S2018/lp.pdf.
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
				a[column][2 + row] = -matrix[row][column];
			}
		}
		return simplexSolver.findSolution(c, a, b);
	}

}
