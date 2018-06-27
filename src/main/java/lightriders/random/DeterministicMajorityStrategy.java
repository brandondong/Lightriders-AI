package lightriders.random;

import java.util.List;

import lightriders.game.Move;

public class DeterministicMajorityStrategy implements IRandomStrategy {

	@Override
	public Move chooseMove(List<Move> moves, double[] probabilities) {
		double maxProbability = 0;
		int maxIndex = -1;
		for (int i = 0; i < probabilities.length; i++) {
			double value = probabilities[i];
			if (value > maxProbability) {
				maxProbability = value;
				maxIndex = i;
			}
		}
		return moves.get(maxIndex);
	}

}
