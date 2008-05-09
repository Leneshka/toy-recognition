package org.amse.shElena.toyRec.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;
import org.amse.shElena.toyRec.samples.Sampler;

public class SimpleComparisonAlgorithm implements IAlgorithm {
	private static ISampleBase myBase;

	private final int MY_WIDTH;

	private final int MY_HEIGHT;

	public SimpleComparisonAlgorithm(int width, int height) {
		MY_WIDTH = width;
		MY_HEIGHT = height;
	}

	public void learnBase(ISampleBase base) {
		myBase = base;
	}

	public List<ComparisonResult> getWholeResults(boolean[][] image) {
		ArrayList<ComparisonResult> list = new ArrayList<ComparisonResult>(
				myBase.size());
		boolean[] sample = Sampler.makeAlgorithmRelativeSample(image, MY_WIDTH,
				MY_HEIGHT);
		int length = sample.length;

		for (ISample s : myBase) {
			int intDist = dist(sample, Sampler.makeAlgorithmRelativeSample(s.getImage(),
					MY_WIDTH, MY_HEIGHT));
			int dist = (100 * intDist) / length;
			list.add(new ComparisonResult(s.getSymbol(), dist));
		}

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
