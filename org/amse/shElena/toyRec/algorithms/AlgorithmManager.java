package org.amse.shElena.toyRec.algorithms;

public class AlgorithmManager {

	/*private static IAlgorithm[] myAlgorithms = new IAlgorithm[] {
			SimpleComparisonAlgorithm.getInstance(),
			ClassComparisonAlgorithm.getInstance(),
			KohonenNetworkAlgorithm.getInstance() };*/

	private AlgorithmManager() {
	}

	public static IAlgorithm[] getAlgorithms() {
		return new IAlgorithm[] {
				SimpleComparisonAlgorithm.getInstance(),
				ClassComparisonAlgorithm.getInstance(),
				KohonenNetworkAlgorithm.getInstance(),
				MyKohonenNetworkAlgorithm.getInstance(),
				MyClassKohonenNetworkAlgorithm.getInstance(),
				ClassKohonenAlgorithm.getInstance(),
				OptimalAlgorithm.getInstance() };
	}

}
