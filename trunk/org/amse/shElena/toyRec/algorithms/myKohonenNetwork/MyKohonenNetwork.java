package org.amse.shElena.toyRec.algorithms.myKohonenNetwork;

import java.util.Random;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class MyKohonenNetwork {
	private double myWeights[][];

	/**
	 * Number of input neurons
	 */
	private int myInputNeuronCount;

	/**
	 * Number of output neurons
	 */
	private int myOutputNeuronCount;

	private double[][] myInputs;

	private final double MY_MIN_LENGTH = 1.E-30;

	private double[] myNormFactors;

	public void setInputs(ISampleBase base, int outputNeuronCount) {
		myInputNeuronCount = base.getSampleSize();
		myOutputNeuronCount = outputNeuronCount;

		myWeights = new double[myOutputNeuronCount][myInputNeuronCount];
		myInputs = new double[base.size()][myInputNeuronCount];

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

		myNormFactors = new double[myInputs.length];
		for (i = 0; i < myInputs.length; i++) {
			myNormFactors[i] = getNormalizationFactor(myInputs[i]);
		}
	}

	private double getNormalizationFactor(double[] input) {
		double length = vectorLength(input);
		// just in case it gets too small
		if (length < MY_MIN_LENGTH)
			length = MY_MIN_LENGTH;

		return 1.0 / Math.sqrt(length);
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
			normalizeWeight(myWeights[i]);
		}
	}

	private class Corrector {
		private double[][] myCorrection = new double[myOutputNeuronCount][myInputNeuronCount];

		private double[] myWinners = new double[myOutputNeuronCount];

		/**
		 * Ex - evaluateError. Evaluates correction and corresponding erro
		 * 
		 * @return correction error - ex- bigerror
		 */
		public double evaluate() {
			double error = 0;
			clearArrays();

			for (int i = 0; i < myInputs.length; i++) {
				int best = winner(myInputs[i]);
				myWinners[best]++;

				double[] w = myWeights[best];
				double[] cor = myCorrection[best];

				for (int j = 0; j < myInputNeuronCount; j++) {
					cor[j] += myInputs[i][j] * myNormFactors[i] - w[j];
				}

				double length = vectorLength(cor);

				if (length > error)
					error = length;
			}

			error = Math.sqrt(error);
			return error;
		}

		public int getWinningNeuronsNumber() {
			int winners = 0;
			for (int i = 0; i < myWinners.length; i++)
				if (myWinners[i] != 0)
					winners++;
			return winners;
		}

		public void forceWin() {
			// gain is output of a winning neuron
			double minGain = Double.POSITIVE_INFINITY;
			double[] out;
			double gain;
			double[] minGainInput = null;

			for (int i = 0; i < myInputs.length; i++) {
				out = getOutput(myInputs[i]);

				gain = Double.NEGATIVE_INFINITY;
				for (int j = 0; j < myOutputNeuronCount; j++) {
					gain = Math.max(gain, out[j]);
				}

				if (gain < minGain) {
					minGain = gain;
					minGainInput = myInputs[i];
				}
			}
			// we foung min of all gaing of winning neurons
			// and corresponding input minGainInput

			out = getOutput(minGainInput);
			double maxOutput = Double.NEGATIVE_INFINITY;
			int neuronToForse = 0;

			for (int i = myOutputNeuronCount - 1; i > -1; i--) {
				if (myWinners[i] == 0) {
					if (out[i] > maxOutput) {
						maxOutput = out[i];
						neuronToForse = i;
					}
				}
			}
			// we found output neuron that never won
			// and has max output on minGainInput.
			// We will change its weights.

			System.arraycopy(minGainInput, 0, myWeights[neuronToForse], 0,
					minGainInput.length);
			normalizeWeight(myWeights[neuronToForse]);
		}

		/**
		 * Ex - adjustWeights
		 * 
		 * @return correction error - ex- bigcorr
		 */
		public double applyCorrection(double rate) {
			double error = 0;

			for (int i = 0; i < myOutputNeuronCount; i++) {

				if (myWinners[i] == 0)
					continue;

				// double f = 1.0 / (double) won[i];
				double f = rate / myWinners[i];

				for (int j = 0; j < myInputNeuronCount; j++) {
					myWeights[i][j] += f * myCorrection[i][j];
				}

				double length = f * vectorLength(myCorrection[i]);

				error = Math.max(error, length);
			}
			// scale the correction
			error = Math.sqrt(error) / rate;

			return error;
		}

		private void clearArrays() {
			for (int i = 0; i < myOutputNeuronCount; i++) {
				for (int j = 0; j < myInputNeuronCount; j++) {
					myCorrection[i][j] = 0;
				}
			}

			for (int j = 0; j < myOutputNeuronCount; j++) {
				myWinners[j] = 0;
			}
		}
	}

	/**
	 * How many retries before quit.
	 */
	private final int myRetriesNumber = 10000;

	/**
	 * Reduction factor - value of reducing rate.
	 */
	private final double myRateReduction = .99;

	/**
	 * Abort if error is beyond this
	 */
	private final double myQuitError = 0.1;

	private double myLearnRate = 0.5;

	private final static double MY_CORRECTION_LIMIT = 1E-5;

	public void learn() {
		for (int i = 0; i < myInputs.length; i++) {
			if (vectorLength(myInputs[i]) < 1.E-30) {
				throw (new RuntimeException(
						"Multiplicative normalization has null training case"));
			}
		}

		// ex - bestnet;
		double[][] bestWeights = new double[myOutputNeuronCount][myInputNeuronCount];

		double rate = myLearnRate;

		initialize();
		double error;
		double bestError = Double.POSITIVE_INFINITY;

		int retryNumber = 0;

		Corrector cor = new Corrector();

		for (int iter = 0;; iter++) {
			error = cor.evaluate();

			// if error became less, save weights
			if (error < bestError) {
				bestError = error;
				copyWeights(bestWeights, myWeights);
			}

			if (bestError < myQuitError)
				break;

			int winners = cor.getWinningNeuronsNumber();

			if ((winners < myOutputNeuronCount) && (winners < myInputs.length)) {
				cor.forceWin();
				continue;
			}

			error = cor.applyCorrection(rate);

			if (error < MY_CORRECTION_LIMIT) {
				if (++retryNumber > myRetriesNumber)
					break;
				initialize();
				rate = myLearnRate;
				continue;
			}

			if (rate > 0.01)
				rate *= myRateReduction;

		}

		// done

		copyWeights(myWeights, bestWeights);

		for (int i = 0; i < myOutputNeuronCount; i++)
			normalizeWeight(myWeights[i]);
	}
}
