package org.amse.shElena.toyRec.algorithms;

public class AlgorithmManager {

	/*private static IAlgorithm[] myAlgorithms = new IAlgorithm[] {
			SimpleComparisonAlgorithm.getInstance(),
			ClassComparisonAlgorithm.getInstance(),
			KohonenNetworkAlgorithm.getInstance() };*/

	private AlgorithmManager() {
	}

	public static Algorithm[] getAlgorithms() {
		return new Algorithm[] {
				SimpleComparisonAlgorithm.getInstance(),
				ClassComparisonAlgorithm.getInstance(),
				KohonenNetworkAlgorithm.getInstance(),
				MyKohonenNetworkAlgorithm.getInstance(),
				MyClassKohonenNetworkAlgorithm.getInstance(),
				ClassKohonenAlgorithm.getInstance(),
				OptimalAlgorithm.getInstance() };
	}

}
