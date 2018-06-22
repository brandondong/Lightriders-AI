package lightriders.random;

import java.util.List;

import lightriders.game.Move;

public class DeterministicMajorityStrategy implements IRandomStrategy {

	@Override
	public Move chooseMove(List<Move> moves, double[] probabilities) {
		double max = Double.MIN_VALUE;
		int maxIndex = -1;
		for (int i = 0; i < probabilities.length; i++) {
			double value = probabilities[i];
			if (value > max) {
				max = value;
				maxIndex = i;
			}
		}
		return moves.get(maxIndex);
	}

}
