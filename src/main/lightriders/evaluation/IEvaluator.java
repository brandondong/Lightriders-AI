package lightriders.evaluation;

import lightriders.ai.Player;
import lightriders.game.Board;

public interface IEvaluator {

	double evaluateBoard(Board board, Player player);

}
