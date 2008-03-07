package org.amse.shElena.toyRec.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class ClassComparisonAlgorithm implements IAlgorithm {
	private ISampleBase myBase;

	private static final double MY_COEFFICIENT = 100;

	private ClassComparisonAlgorithm() {
	}

	private static IAlgorithm myInstance = new ClassComparisonAlgorithm();

	public static IAlgorithm getInstance() {
		return myInstance;
	}

	public void learnBase(ISampleBase base) {
		myBase = base;
	}

	public List<ComparisonResult> getWholeResults(boolean[] picture) {
		Map<Character, Double> potentials = new HashMap<Character, Double>();
		Map<Character, Integer> quantity = new HashMap<Character, Integer>();
		int length = picture.length;

		for (ISample s : myBase) {
			int intDist = dist(picture, s.getPicture());
			double dist = ((double) intDist) / length;
			double pot = MY_COEFFICIENT / (1 + dist * dist);
			Character symbol = s.getSymbol();

			if (potentials.containsKey(symbol)) {
				potentials.put(symbol, potentials.get(symbol) + pot);
				quantity.put(symbol, quantity.get(symbol) + 1);
			} else {
				potentials.put(symbol, pot);
				quantity.put(symbol, 1);
			}
		}

		ArrayList<ComparisonResult> list = new ArrayList<ComparisonResult>(
				potentials.size());

		for (Character c : potentials.keySet()) {
			/*
			 * normalize to [0, 100] interval
			 */
			// int distance = (int)((100 * potentials.get(c))/(quantity.get(c) *
			// MY_COEFFICIENT));
			int distance = (int) ((100 * quantity.get(c) * MY_COEFFICIENT) / potentials
					.get(c));
			list.add(new ComparisonResult(c, distance - 100));
		}

		// Collections.sort(list);
		return list;
	}

	private int dist(boolean[] firstArray, boolean[] secondArray) {
		if (firstArray.length != secondArray.length) {
			return Integer.MAX_VALUE;
		}

		int d = 0;
		for (int i = 0; i < firstArray.length; i++) {
			if (firstArray[i] != secondArray[i]) {
				d++;
			}
		}
		return d;
	}

	public String getName() {
		return "Class comparison algorithm";
	}

}
