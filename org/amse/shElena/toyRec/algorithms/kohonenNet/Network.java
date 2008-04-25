package org.amse.shElena.toyRec.algorithms.kohonenNet;

import java.util.*;

abstract public class Network {

	/**
	 * Output neuron activations
	 */
	protected double myOutput[];

	/**
	 * Mean square error of the network
	 */
	protected double myTotalError;

	/**
	 * Number of input neurons
	 */
	protected int myInputNeuronCount;

	/**
	 * Number of output neurons
	 */
	protected int myOutputNeuronCount;

	/**
	 * Random number generator
	 */
	protected Random myRandom = new Random(System.currentTimeMillis());

	/**
	 * Called to learn from training sets.
	 * 
	 * @exception java.lang.RuntimeException
	 */
	public abstract void learn() throws RuntimeException;

	/**
	 * Called to present an input pattern.
	 * 
	 * @param input
	 *            The input pattern
	 */
	protected abstract void trial(double[] input);
	

	/**
	 * Calculate the length of a vector.
	 * 
	 * @param v
	 *            vector
	 * @return Vector length.
	 */

	// норма вектора в квадрате
	protected double vectorLength(double v[]) {
		double length = 0.0;
		for (int i = 0; i < v.length; i++)
			length += v[i] * v[i];
		return length;
	}

	/**
	 * Called to calculate a dot product.
	 * 
	 * @param vec1
	 *            one vector
	 * @param vec2
	 *            another vector
	 * @return The dot product.
	 */
	// скалярное произведение
	protected double dotProduct(double[] vec1, double[] vec2) {
		double rtn = 0.0;

		for (int k = 0; k < vec1.length; k++) {
			rtn += vec1[k] * vec2[k];
		}
		return rtn;
	}

	/**
	 * Called to randomize weights.
	 * 
	 * @param weight
	 *            A weight matrix.
	 */

	protected void randomizeWeights(double weight[][]) {
		double r;

		int temp = (int) (3.464101615 / (2. * Math.random())); // SQRT(12)=3.464...

		for (int x = 0; x < weight.length; x++) {
			for (int y = 0; y < weight[0].length; y++) {
				r = (double) myRandom.nextInt(Integer.MAX_VALUE)
						+ (double) myRandom.nextInt(Integer.MAX_VALUE)
						- (double) myRandom.nextInt(Integer.MAX_VALUE)
						- (double) myRandom.nextInt(Integer.MAX_VALUE);
				weight[x][y] = temp * r;
			}
		}
	}

}