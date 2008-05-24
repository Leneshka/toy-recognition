package org.amse.shElena.toyRec.algorithms;

import java.util.List;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;

/**
 * Provides signature of mandatory methods used during learning and recognition
 * processes.
 */
public interface IAlgorithm {
	/**
	 * Learns given base. Usually translates given samples to some new format
	 * and saves obtained information.
	 * 
	 * @param base
	 *            samples to learn.
	 */
	public void learnBase(ISampleBase base);

	/**
	 * Compares properties of the given image with properties of known symbols
	 * and determines their difference.
	 * 
	 * @param image
	 *            image to compare.
	 * @return list of pairs, each consists of a learned symbol and its
	 *         difference from the symbol on the image. Difference is measured
	 *         in percents.
	 */
	public List<ComparisonResult> getWholeResults(boolean[][] image);

	/**
	 * Used in gui in lists of algorithms. It's value would be written as a list
	 * item.
	 * 
	 * @return name of the algorithm.
	 */
	public String toString();
}
