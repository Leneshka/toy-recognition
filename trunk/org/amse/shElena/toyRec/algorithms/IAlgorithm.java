package org.amse.shElena.toyRec.algorithms;

import java.util.List;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;

public interface IAlgorithm {
	public void learnBase(ISampleBase base);

	public List<ComparisonResult> getWholeResults(boolean[][] image);

	public String toString();
}
