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
	void testBaseCase() {
		double[][] matrix = new double[][] { new double[] { 1 } };
		assertEquilibrium(matrix, 1, new double[] { 1 });
	}

	@Test
	void testMatchingPennies() {
		double[][] matrix = new double[][] { new double[] { 1, -1 }, new double[] { -1, 1 } };
		assertEquilibrium(matrix, 0, new double[] { 0.5, 0.5 });
	}

	@Test
	void testRockPaperScissors() {
		double[][] matrix = new double[][] { new double[] { 0, -1, 1 }, new double[] { 1, 0, -1 },
				new double[] { -1, 1, 0 } };
		assertEquilibrium(matrix, 0, new double[] { 1. / 3, 1. / 3, 1. / 3 });
	}

	@Test
	void testPureStrategy() {
		double[][] matrix = new double[][] { new double[] { 0, -1 }, new double[] { -2, -3 } };
		assertEquilibrium(matrix, -1, new double[] { 1, 0 });
	}

	private void assertEquilibrium(double[][] matrix, int equilibriumValue, double[] probabilities) {
		assertEquals(equilibriumValue, solver.findNashEquilibriumValue(matrix), TOL);
		assertArrayEquals(probabilities, solver.findNashEquilibriumStrategy(matrix), TOL);
	}

}
