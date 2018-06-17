package lightriders.lp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lightriders.equilibrium.INashEquilibriumSolver;

class ZeroSumNashEquilibriumSolverTest {

	private static final double TOL = 0.001;

	private INashEquilibriumSolver solver;

	@BeforeEach
	void setup() {
		solver = new ZeroSumNashEquilibriumSolver();
	}

	@Test
	void testMatchingPennies() {
		double[][] matrix = new double[][] { new double[] { 1, -1 }, new double[] { -1, 1 } };
		assertEquals(0, solver.findNashEquilibriumValue(matrix), TOL);
		assertArrayEquals(new double[] { 0.5, 0.5 }, solver.findNashEquilibriumStrategy(matrix), TOL);
	}

	@Test
	void testBaseCase() {
		double[][] matrix = new double[][] { new double[] { 1 } };
		assertEquals(1, solver.findNashEquilibriumValue(matrix), TOL);
		assertArrayEquals(new double[] { 1 }, solver.findNashEquilibriumStrategy(matrix), TOL);
	}

}
