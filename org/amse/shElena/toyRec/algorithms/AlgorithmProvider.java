package org.amse.shElena.toyRec.algorithms;

public class AlgorithmProvider {
	private AlgorithmProvider() {
	}

	public static IAlgorithm[] getAlgorithms() {
		return new IAlgorithm[] {
				new SimpleComparisonAlgorithm(5,7),
				new ClassComparisonAlgorithm(5,7),
				new KohonenNetworkAlgorithm(5,7),
				new FSMAlgorithm()
		};
	}

}
