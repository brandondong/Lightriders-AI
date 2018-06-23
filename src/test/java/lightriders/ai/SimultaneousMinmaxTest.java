package lightriders.ai;

import static org.junit.Assert.assertArrayEquals;

import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

class SimultaneousMinmaxTest {

	private static final double TOL = 0.001;

	@Test
	void testDepth1() {
		SimultaneousMinmax<Double> minmax = new SimultaneousMinmax<>(1, (d, p) -> {
			if (d == 1 && p == Player.ZERO) {
				return new Double[][] { new Double[] { 5., 3. }, new Double[] { 10., 2. } };
			}
			throw new RuntimeException();
		}, identityEvaluation());
		assertArrayEquals(new double[] { 1, 0 }, minmax.optimalDecisionProbabilities(1., Player.ZERO), TOL);
	}

	@Test
	void testUnlimitedDepth() {
		SimultaneousMinmax<Double> minmax = new SimultaneousMinmax<>(0, (d, p) -> {
			if (d == 1 && p == Player.ZERO) {
				return new Double[][] { new Double[] { 5., 3. }, new Double[] { 10., 2. } };
			}
			return new Double[][] {};
		}, identityEvaluation());
		assertArrayEquals(new double[] { 1, 0 }, minmax.optimalDecisionProbabilities(1., Player.ZERO), TOL);
	}

	private BiFunction<Double, Player, Double> identityEvaluation() {
		return (d, p) -> p == Player.ZERO ? d : -1;
	}

}
