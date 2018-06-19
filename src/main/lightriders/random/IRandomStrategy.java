package lightriders.random;

import java.util.List;

import lightriders.game.Move;

public interface IRandomStrategy {

	Move chooseMove(List<Move> moves, double[] probabilities);

}
