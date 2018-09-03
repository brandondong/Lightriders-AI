package lightriders.ai;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PayoffMatrixResolverTest {

	private static class CountResolved implements Function<Double, Double> {

		private int numResolved = 0;

		@Override
		public Double apply(Double i) {
			numResolved++;
			return i;
		}

		public int numResolved() {
			return numResolved;
		}
	}

	private static final double TOL = 0.0001;

	private PayoffMatrixResolver<Double> resolver;

	private CountResolved countResolved;

	@BeforeEach
	void setup() {
		resolver = new PayoffMatrixResolver<>();
		countResolved = new CountResolved();
	}

	@Test
	void testMatchingPenniesNoPrune() {
		Double[][] matchingPennies = new Double[][] { new Double[] { 1., -1. }, new Double[] { -1., 1. } };
		assertEquals(0, resolver.findNashEquilibriumValue(matchingPennies, countResolved), TOL);
		assertEquals(4, countResolved.numResolved());
		assertArrayEquals(new double[] { 0.5, 0.5 },
				resolver.findNashEquilibriumStrategy(matchingPennies, countResolved), TOL);
		assertEquals(8, countResolved.numResolved());
	}

	@Test
	void test2By3Prune() {
		Double[][] matrix = new Double[][] { new Double[] { 1., -1., -2. }, new Double[] { -1., 1., -3. } };
		assertEquals(-2, resolver.findNashEquilibriumValue(matrix, countResolved), TOL);
		assertEquals(4, countResolved.numResolved());
		assertArrayEquals(new double[] { 1, 0 }, resolver.findNashEquilibriumStrategy(matrix, countResolved), TOL);
		assertEquals(8, countResolved.numResolved());
	}

}
