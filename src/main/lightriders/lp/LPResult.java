package lightriders.lp;

class LPResult {

	private final double objectiveValue;

	private final double[] variableValues;

	public LPResult(double objectiveValue, double[] variableValues) {
		this.objectiveValue = objectiveValue;
		this.variableValues = variableValues;
	}

	public double objectiveValue() {
		return objectiveValue;
	}

	public double[] variableValues() {
		return variableValues;
	}

}
