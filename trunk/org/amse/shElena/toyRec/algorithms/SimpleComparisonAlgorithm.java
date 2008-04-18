package org.amse.shElena.toyRec.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class SimpleComparisonAlgorithm extends Algorithm {
	private static ISampleBase myBase;
	
	private SimpleComparisonAlgorithm() {
	}

	private static Algorithm myInstance = new SimpleComparisonAlgorithm();

	public static Algorithm getInstance() {
		return myInstance;
	}
	
	public void learnBase(ISampleBase base){
		myBase = base;	
	}
	
	public List<ComparisonResult> getWholeResults(boolean[] picture) {
		ArrayList<ComparisonResult> list = new ArrayList<ComparisonResult>(myBase.size());
		int length = picture.length;
		
		for (ISample s : myBase) {
				int intDist = dist(picture, s.getPicture());
				int dist = (100 * intDist) / length;
				list.add(new ComparisonResult(s.getSymbol(), dist));
		}

		//Collections.sort(list);
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

	public String toString() {
		return "Simple comparison algorithm";
	}
}
