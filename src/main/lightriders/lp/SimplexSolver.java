package lightriders.lp;

class SimplexSolver {

	private static final double TOL = 0.000001;

	/**
	 * Finds the optimal solution to a linear programming problem.
	 * <p>
	 * Note that the input arrays may be mutated.
	 * 
	 * @param c
	 *            Constants of the objective function in standard form
	 * @param a
	 *            Constants of constraint matrix in canonical tableau form with the
	 *            identity matrix on the right and indexed by [constraint][variable]
	 * @param b
	 *            Constants of the RHS of the constraints in standard form
	 * @return The optimal solution
	 * @throws RuntimeException
	 *             If the objective is unbounded
	 */
	public double[] findSolution(double[] c, double[][] a, double[] b) {
		return performSimplex(c, a, b, false);
	}

	/**
	 * Performs the phase 1 simplex algorithm on the input arrays.
	 * 
	 * @param c
	 *            Constants of the objective function in standard form
	 * @param a
	 *            Constants of constraint matrix in canonical tableau form with the
	 *            identity matrix on the right and indexed by [constraint][variable]
	 * @param b
	 *            Constants of the RHS of the constraints in standard form
	 * @throws RuntimeException
	 *             If the objective is unbounded
	 */
	public void performSimplexPhase1(double[] c, double[][] a, double[] b) {
		performSimplex(c, a, b, true);
	}

	private double[] performSimplex(double[] c, double[][] a, double[] b, boolean isPhase1) {
		// Algorithm detailed here: https://www.youtube.com/watch?v=yL7JByLlfrw.
		// Bland's rule to avoid cycles.
		int numVariables = c.length;
		int numConstraints = b.length;
		int[] basicVariablesIndex = new int[numConstraints];
		int startingSlackIndex = numVariables - numConstraints;
		for (int i = 0; i < numConstraints; i++) {
			basicVariablesIndex[i] = startingSlackIndex + i;
		}
		for (int i = 0; i < numVariables; i++) {
			c[i] *= -1;
		}
		while (true) {
			int pivotColumn = findPivotColumn(c);
			if (pivotColumn == -1) {
				// Simplex algorithm terminating condition.
				return getVariableValues(basicVariablesIndex, b, numVariables);
			}
			int pivotRow = findPivotRow(pivotColumn, a, b, basicVariablesIndex, isPhase1);
			double value = a[pivotRow][pivotColumn];
			basicVariablesIndex[pivotRow] = pivotColumn;
			// Scale the pivot row.
			for (int column = 0; column < numVariables; column++) {
				// Scaling factor nonzero because of row selection procedure.
				a[pivotRow][column] /= value;
			}
			b[pivotRow] /= value;
			for (int row = 0; row < numConstraints; row++) {
				// Zero out the other rows.
				double rowValue = a[row][pivotColumn];
				if (row != pivotRow && !areDoublesEqual(rowValue, 0)) {
					double scaling = -rowValue;
					for (int column = 0; column < numVariables; column++) {
						a[row][column] += scaling * a[pivotRow][column];
					}
					b[row] += scaling * b[pivotRow];
				}
			}
			// Including the objective row.
			double scaling = -c[pivotColumn];
			// Scaling is guaranteed to be nonzero because of column selection procedure.
			for (int column = 0; column < numVariables; column++) {
				c[column] += scaling * a[pivotRow][column];
			}
		}
	}

	private double[] getVariableValues(int[] basicVariablesIndex, double[] b, int numVariables) {
		double[] variableValues = new double[numVariables];
		for (int i = 0; i < basicVariablesIndex.length; i++) {
			int variableIndex = basicVariablesIndex[i];
			variableValues[variableIndex] = b[i];
		}
		return variableValues;
	}

	private int findPivotRow(int pivotColumn, double[][] a, double[] b, int[] basicVariablesIndex, boolean isPhase1) {
		// Now among the rows, choose the one with the lowest ratio between the
		// (transformed) right hand side and the coefficient in the pivot tableau where
		// the coefficient is greater than zero. If the minimum ratio is shared by
		// several rows, choose the row with the lowest-numbered column (variable) basic
		// in it.
		double minRatio = Double.MAX_VALUE;
		int rowChoice = -1;
		int minBasicVariableIndex = Integer.MAX_VALUE;
		// If performing phase 1, the first row should not be considered.
		int start = isPhase1 ? 1 : 0;
		for (int i = start; i < b.length; i++) {
			double pivotEntry = a[i][pivotColumn];
			// We expect at least one pivot entry to be positive. Otherwise, the objective
			// function is unbounded.
			if (pivotEntry > TOL) {
				double value = b[i] / pivotEntry;
				int basicVariableIndex = basicVariablesIndex[i];
				if (areDoublesEqual(minRatio, value)) {
					if (basicVariableIndex < minBasicVariableIndex) {
						minBasicVariableIndex = basicVariableIndex;
						rowChoice = i;
					}
				} else if (value < minRatio) {
					// We know the doubles are not equal so the imprecise comparison is valid.
					minRatio = value;
					rowChoice = i;
					minBasicVariableIndex = basicVariableIndex;
				}
			}
		}
		return rowChoice;
	}

	private boolean areDoublesEqual(double d1, double d2) {
		return Math.abs(d1 - d2) <= TOL;
	}

	private int findPivotColumn(double[] c) {
		// Choose the lowest-numbered (i.e., leftmost) nonbasic column with a negative
		// (reduced) cost.
		for (int i = 0; i < c.length; i++) {
			if (c[i] < -TOL) {
				return i;
			}
		}
		return -1;
	}

}
