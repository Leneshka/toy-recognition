package org.amse.shElena.toyRec.algorithms.myKohonenNetwork;

import java.util.Random;

import org.amse.shElena.toyRec.algorithms.kohonenNet.TrainingSet;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class MyKohonenNetwork {
	double myWeights[][];

	private double myLearnRate = 0.5;

	/**
	 * Abort if error is beyond this
	 */
	private double myQuitError = 0.1;

	/**
	 * How many retries before quit.
	 */
	private int myRetriesNumber = 10000;

	/**
	 * Reduction factor - value of reducing rate.
	 */
	private double myRateReduction = .99;

	/**
	 * Set to true to abort learning.
	 */
	private boolean myHalt = false;

	private final double MY_MIN_LENGTH = 1.E-30;

	/**
	 * Number of input neurons
	 */
	private int myInputNeuronCount;

	/**
	 * Number of output neurons
	 */
	private int myOutputNeuronCount;
	
	
	private double myCurrentBigerror;


	private double[][] myInputs;

	public void setInputs(ISampleBase base) {
		myWeights = new double[myOutputNeuronCount][myInputNeuronCount];
		
		myInputNeuronCount = base.getSampleSize();
		myOutputNeuronCount = base.size();
		myInputs = new double[myOutputNeuronCount][myInputNeuronCount];

		int i = 0;
		for (ISample s : base.getSamples()) {
			for (int j = 0; j < myInputNeuronCount; j++) {
				if (s.getPicture()[j]) {
					myInputs[i][j] = 0.5;
				} else {
					myInputs[i][j] = -0.5;
				}
			}
			i++;
		}
	}

	// норма вектора в квадрате
	private double vectorLength(double v[]) {
		double length = 0.0;
		for (int i = 0; i < v.length; i++)
			length += v[i] * v[i];
		return length;
	}

	// скалярное произведение
	private double dotProduct(double[] vec1, double[] vec2) {
		double rtn = 0.0;

		for (int k = 0; k < vec1.length; k++) {
			rtn += vec1[k] * vec2[k];
		}
		return rtn;
	}

	private void randomizeWeights(double weight[][]) {
		double r;
		Random random = new Random(System.currentTimeMillis());

		int temp = (int) (3.464101615 / (2. * Math.random())); // SQRT(12)=3.464...

		for (int x = 0; x < weight.length; x++) {
			for (int y = 0; y < weight[0].length; y++) {
				r = (double) random.nextInt(Integer.MAX_VALUE)
						+ (double) random.nextInt(Integer.MAX_VALUE)
						- (double) random.nextInt(Integer.MAX_VALUE)
						- (double) random.nextInt(Integer.MAX_VALUE);
				weight[x][y] = temp * r;
			}
		}
	}

	private void copyWeights(double[][] dest, double[][] source) {
		for (int i = 0; i < source.length; i++) {
			System.arraycopy(source[i], 0, dest[i], 0, source[i].length);
		}
	}

	private double getNormalizationFactor(double[] input) {
		double length = vectorLength(input);
		// just in case it gets too small
		if (length < MY_MIN_LENGTH)
			length = MY_MIN_LENGTH;

		return 1.0 / Math.sqrt(length);
	}

	private void normalizeWeight(double[] w) {
		double f = getNormalizationFactor(w);

		for (int i = 0; i < w.length; i++)
			w[i] *= f;
	}

	// ex - trial
	public double[] getOutput(double[] input) {
		double factor = getNormalizationFactor(input);
		double[] out = new double[myOutputNeuronCount];

		for (int i = 0; i < myOutputNeuronCount; i++) {
			out[i] = dotProduct(input, myWeights[i]) * factor;
			// Remap to bipolar (-1,1 to 0,1)
			out[i] = 0.5 * (out[i] + 1.0);
			// account for rounding
			if (out[i] > 1.0)
				out[i] = 1.0;
			if (out[i] < 0.0)
				out[i] = 0.0;
		}
		return out;
	}

	public int winner(double[] input) {
		int win = 0;

		double[] out = getOutput(input);

		double biggest = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < out.length; i++) {
			if (out[i] > biggest) {
				win = i;
				biggest = out[i];
			}
		}

		return win;
	}

	private void initialize() {
		randomizeWeights(myWeights);
		
		for (int i = 0; i < myOutputNeuronCount; i++) {
			normalizeWeight( myWeights[i]);
		}
	}
	
	/**
	 * Changes currentBigerror
	 * 
	 * @return correction
	 */
	
	private double[][] evaluateErrors(){
		myCurrentBigerror = 0;
		double[][] correc = new double[myOutputNeuronCount][myInputNeuronCount];
		
		for(int i = 0; i < myInputs.length; i++){
			double[] inp = myInputs[i];
			double factor = getNormalizationFactor(inp);
			int best = winner(inp);
			
			
			double[] w = myWeights[best];
			double[] cor = correc[best];
			
			for (int j = 0; i < myInputNeuronCount; i++) {			
				cor[j] += inp[j] * factor - w[j];
			}
				
			double length = vectorLength(cor);
	
			if (length > myCurrentBigerror)
				myCurrentBigerror = length;	
		}
		myCurrentBigerror = Math.sqrt(myCurrentBigerror);
		return correc;
	}
}
