package org.amse.shElena.toyRec.algorithms;

/**
 * Provides array of all algorithms
 * available on first and second tabs.
 */
public class AlgorithmProvider {
	private AlgorithmProvider() {
	}

	
	/**
	 * @return array of all algorithms.
	 */
	public static IAlgorithm[] getAlgorithms() {
		return new IAlgorithm[] {
				new SimpleComparisonAlgorithm(5,7),
				new ClassComparisonAlgorithm(5,7),
				new KohonenNetworkAlgorithm(5,7),
				new FSMAlgorithm()
		};
	}

}
