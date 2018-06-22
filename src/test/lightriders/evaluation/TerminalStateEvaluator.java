package lightriders.evaluation;

import java.util.List;

import lightriders.ai.Player;
import lightriders.game.Board;
import lightriders.game.Move;

public class TerminalStateEvaluator implements IEvaluator {

	@Override
	public double evaluateBoard(Board board, Player player) {
		List<Move> moves = board.possibleMovesFor(player);
		Player opponent = player.opponent();
		List<Move> opponentMoves = board.possibleMovesFor(opponent);
		if (moves.isEmpty() && opponentMoves.isEmpty()) {
			return 0;
		} else if (moves.isEmpty()) {
			return -1;
		} else if (opponentMoves.isEmpty()) {
			return 1;
		}
		throw new IllegalArgumentException();
	}

}
