package lightriders.lp;

import java.util.Arrays;

import lightriders.equilibrium.INashEquilibriumSolver;

public class ZeroSumNashEquilibriumSolver implements INashEquilibriumSolver {

	private final SimplexSolver simplexSolver = new SimplexSolver();

	@Override
	public double findNashEquilibriumValue(double[][] matrix) {
		double[] variableValues = solveAssociatedLP(matrix);
		return variableValues[0] - variableValues[1];
	}

	@Override
	public double[] findNashEquilibriumStrategy(double[][] matrix) {
		double[] variableValues = solveAssociatedLP(matrix);
		int numRows = matrix.length;
		return Arrays.copyOfRange(variableValues, 2, 2 + numRows);
	}

	private double[] solveAssociatedLP(double[][] matrix) {
		// LP explanation here: http://www.fuhuthu.com/CPSC420S2018/lp.pdf.
		// Not in canonical tableau form. Phase 1.
		int numRows = matrix.length;
		int numColumns = matrix[0].length;
		int numConstraints = numColumns + 2;
		// Variables are ordered in the following way: 1 for the original objective
		// value, 2 for the unbounded equilibrium value, the probabilities for each
		// action, slack variables, and 1 for the artificial variable.
		int numVariables = 4 + numRows + numColumns;

		double[][] a = new double[numConstraints][numVariables];
		double[] b = new double[numConstraints];
		double[] c = new double[numVariables];
		int[] basicVariablesIndex = new int[numConstraints];

		// Minimizing single artificial variable is equivalent to maximizing the
		// summation of probabilities.
		Arrays.fill(c, 3, 3 + numRows, 1);

		a[0][0] = 1;
		a[0][1] = -1;
		a[0][2] = 1;

		// Probabilities sum to 1.
		Arrays.fill(a[numConstraints - 1], 3, 3 + numRows, 1);
		// Artificial variable.
		a[numConstraints - 1][numVariables - 1] = 1;
		b[numConstraints - 1] = 1;

		// Populate the minimization constraints.
		for (int i = 0; i < numColumns; i++) {
			a[i + 1][1] = 1;
			a[i + 1][2] = -1;
			// Slack variables.
			a[i + 1][3 + numRows + i] = 1;
		}
		for (int row = 0; row < numRows; row++) {
			for (int column = 0; column < numColumns; column++) {
				a[column + 1][3 + row] = -matrix[row][column];
			}
		}
		basicVariablesIndex[0] = 0;
		for (int i = 1; i < numConstraints; i++) {
			basicVariablesIndex[i] = 2 + numRows + i;
		}
		simplexSolver.performSimplexPhase1(c, a, b, basicVariablesIndex);

		// Phase 2.
		numConstraints -= 1;
		numVariables -= 2;
		double[][] a2 = new double[numConstraints][numVariables];
		double[] b2 = new double[numConstraints];
		double[] c2 = new double[numVariables];
		int[] basicVariablesIndex2 = new int[numConstraints];

		System.arraycopy(b, 1, b2, 0, b2.length);
		for (int i = 0; i < numVariables; i++) {
			c2[i] = -a[0][i + 1];
		}
		for (int row = 0; row < numConstraints; row++) {
			System.arraycopy(a[row + 1], 1, a2[row], 0, numVariables);
		}
		for (int i = 0; i < numConstraints; i++) {
			basicVariablesIndex2[i] = basicVariablesIndex[i + 1] - 1;
		}
		return simplexSolver.findSolution(c2, a2, b2, basicVariablesIndex2);
	}

}
