package lightriders.lp;

class SimplexSolver {

	/**
	 * Finds the optimal solution to a linear programming problem.
	 * <p>
	 * Note that the input arrays may be mutated.
	 * 
	 * @param c
	 *            Constants of the objective function in standard form
	 * @param a
	 *            Constants of constraint matrix in standard form indexed by
	 *            [constraint][variable]
	 * @param b
	 *            Constants of the RHS of the constraints in standard form
	 * @return The optimal solution
	 */
	public double[] findSolution(double[] c, double[][] a, double[] b) {
		// Algorithm detailed here: https://www.youtube.com/watch?v=yL7JByLlfrw.
		int numVariables = c.length;
		int numConstraints = b.length;
		int[] leftIndex = new int[numConstraints];
		int startingSlackIndex = numVariables - numConstraints;
		for (int i = 0; i < numConstraints; i++) {
			leftIndex[i] = startingSlackIndex + i;
		}
		for (int i = 0; i < numVariables; i++) {
			c[i] *= -1;
		}
		while (true) {
			int pivotColumn = minIndex(c);
			double minValue = c[pivotColumn];
			if (minValue >= 0) {
				// Simplex algorithm terminating condition.
				return getVariableValues(leftIndex, a, b, numVariables);
			}
			int pivotRow = findPivotRow(pivotColumn, a, b);
			double value = a[pivotRow][pivotColumn];
			leftIndex[pivotRow] = pivotColumn;
			for (int row = 0; row < numConstraints; row++) {
				// Zero out the other rows.
				double rowValue = a[row][pivotColumn];
				if (row != pivotRow && rowValue != 0) {
					double scaling = -rowValue / value;
					for (int column = 0; column < numVariables; column++) {
						a[row][column] += scaling * a[pivotRow][column];
					}
					b[row] += scaling * b[pivotRow];
				}
			}
			// Including the objective row.
			double scaling = -minValue / value;
			for (int column = 0; column < numVariables; column++) {
				c[column] += scaling * a[pivotRow][column];
			}
		}
	}

	private double[] getVariableValues(int[] leftIndex, double[][] a, double[] b, int numVariables) {
		double[] variableValues = new double[numVariables];
		for (int i = 0; i < leftIndex.length; i++) {
			int variableIndex = leftIndex[i];
			variableValues[variableIndex] = b[i] / a[i][variableIndex];
		}
		return variableValues;
	}

	private int findPivotRow(int pivotColumn, double[][] a, double[] b) {
		double minValue = Double.MAX_VALUE;
		int minRow = 0;
		for (int i = 0; i < b.length; i++) {
			double value = b[i] / a[i][pivotColumn];
			if (value < minValue) {
				minValue = value;
				minRow = i;
			}
		}
		return minRow;
	}

	private int minIndex(double[] array) {
		double minElement = array[0];
		int minIndex = 0;
		for (int i = 1; i < array.length; i++) {
			double current = array[i];
			if (current < minElement) {
				minElement = current;
				minIndex = i;
			}
		}
		return minIndex;
	}

}
