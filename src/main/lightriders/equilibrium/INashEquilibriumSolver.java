package lightriders.equilibrium;

public interface INashEquilibriumSolver {

	/**
	 * Calculates the Nash equilibrium value of a zero sum payoff matrix.
	 * 
	 * @param matrix
	 *            A payoff matrix for a zero sum game
	 * @return The Nash equilibrium payoff
	 */
	double findNashEquilibriumValue(double[][] matrix);

	/**
	 * Calculates a Nash equilibrium strategy in a zero sum game.
	 * 
	 * @param matrix
	 *            A row player payoff matrix for a zero sum game indexed in the form
	 *            [row][column]
	 * @return An array of probabilities corresponding to each of the row player's
	 *         actions
	 */
	double[] findNashEquilibriumStrategy(double[][] matrix);

}