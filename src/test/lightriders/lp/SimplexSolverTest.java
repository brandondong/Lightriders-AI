package lightriders.lp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimplexSolverTest {

	private static final double TOL = 0.001;

	private SimplexSolver solver;

	@BeforeEach
	void setup() {
		solver = new SimplexSolver();
	}

	@Test
	void testSolve1() {
		// Problem taken from https://www.youtube.com/watch?v=yL7JByLlfrw.
		double[] c = new double[] { 7, 8, 10, 0, 0 };
		double[] b = new double[] { 1000, 800 };
		double[][] a = new double[][] { new double[] { 2, 3, 2, 1, 0 }, new double[] { 1, 1, 2, 0, 1 } };
		double[] variableValues = solver.findSolution(c, a, b);
		validateVariables(variableValues, 5);
		double x1 = variableValues[0];
		double x2 = variableValues[1];
		double x3 = variableValues[2];
		assertTrue(2 * x1 + 3 * x2 + 2 * x3 <= 1000);
		assertTrue(x1 + x2 + 2 * x3 <= 800);
		assertEquals(4400, 7 * x1 + 8 * x2 + 10 * x3, TOL);
	}

	@Test
	void testSolve2() {
		// Problem taken from https://en.wikipedia.org/wiki/Simplex_algorithm.
		double[] c = new double[] { 2, 3, 4, 0, 0 };
		double[] b = new double[] { 10, 15 };
		double[][] a = new double[][] { new double[] { 3, 2, 1, 1, 0 }, new double[] { 2, 5, 3, 0, 1 } };
		double[] variableValues = solver.findSolution(c, a, b);
		validateVariables(variableValues, 5);
		double x1 = variableValues[0];
		double x2 = variableValues[1];
		double x3 = variableValues[2];
		double s = variableValues[3];
		double t = variableValues[4];
		assertEquals(10, 3 * x1 + 2 * x2 + x3 + s, TOL);
		assertEquals(15, 2 * x1 + 5 * x2 + 3 * x3 + t, TOL);
		assertEquals(20, 2 * x1 + 3 * x2 + 4 * x3, TOL);
	}

	@Test
	void testSolve3() {
		// Problem taken from http://www.fuhuthu.com/CPSC420S2018/ps6.pdf.
		double[] c = new double[] { 1, 1, 0, 0 };
		double[] b = new double[] { 3, 5 };
		double[][] a = new double[][] { new double[] { 2, 1, 1, 0 }, new double[] { 1, 3, 0, 1 } };
		double[] variableValues = solver.findSolution(c, a, b);
		validateVariables(variableValues, 4);
		double x = variableValues[0];
		double y = variableValues[1];
		assertEquals(0.8, x, TOL);
		assertEquals(1.4, y, TOL);

	}

	private void validateVariables(double[] variableValues, int numVariables) {
		assertEquals(numVariables, variableValues.length);
		Arrays.stream(variableValues).forEach(d -> assertTrue(d >= 0));
	}

}
