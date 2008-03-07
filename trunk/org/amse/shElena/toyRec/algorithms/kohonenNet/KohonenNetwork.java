package org.amse.shElena.toyRec.algorithms.kohonenNet;

public class KohonenNetwork extends Network {

	/**
	 * The weights of the output neurons base on the input from the input
	 * neurons.
	 */
	double myWeights[][];

	/**
	 * The learning method.
	 */

	protected int myLearnMethod = 1;

	/**
	 * The learning rate.
	 */
	// // скорость обучения
	protected double myLearnRate = 0.5;

	/**
	 * Abort if error is beyond this
	 */
	protected double myQuitError = 0.1;
	//protected double myQuitError = 0.0;

	/**
	 * How many retries before quit.
	 */
	protected int myRetriesNumber = 10000;

	/**
	 * Reduction factor - value of reducing rate.
	 */
	protected double myRateReduction = .99;

	/**
	 * The owner object, to report to.
	 */
	protected NeuralReportable myReportable;

	/**
	 * Set to true to abort learning.
	 */
	public boolean myHalt = false;

	private final double MY_MIN_LENGTH = 1.E-30;

	/**
	 * The training set.
	 */
	protected TrainingSet myTrainingSet;

	/**
	 * The constructor.
	 * 
	 * @param inputCount
	 *            Number of input neurons
	 * @param outputCount
	 *            Number of output neurons
	 * @param owner
	 *            The owner object, for updates.
	 */
	public KohonenNetwork(int inputCount, int outputCount,
			NeuralReportable reportable) {
		myTotalError = 1.0;

		myInputNeuronCount = inputCount;
		myOutputNeuronCount = outputCount;
		myWeights = new double[myOutputNeuronCount][myInputNeuronCount + 1];
		myOutput = new double[myOutputNeuronCount];
		myReportable = reportable;
	}

	/**
	 * Set the training set to use.
	 * 
	 * @param set
	 *            The training set to use.
	 */
	public void setTrainingSet(TrainingSet set) {
		myTrainingSet = set;
	}

	/**
	 * Copy the weights from this network to another.
	 * 
	 * @param dest
	 *            The destination for the weights.
	 * @param source
	 */
	private static void copyWeights(KohonenNetwork dest, KohonenNetwork source) {
		for (int i = 0; i < source.myWeights.length; i++) {
			System.arraycopy(source.myWeights[i], 0, dest.myWeights[i], 0,
					source.myWeights[i].length);
		}
	}

	/**
	 * Clear the weights.
	 */
	private void clearWeights() {
		myTotalError = 1.0;
		for (int x = 0; x < myWeights.length; x++)
			for (int y = 0; y < myWeights[0].length; y++)
				myWeights[x][y] = 0;
	}

	/**
	 * Normalize the input.
	 * 
	 * @param input
	 *            input pattern
	 * @param normfac
	 *            the result
	 * @param synth
	 *            synthetic last input
	 */

	private void normalizeInput(final double input[], double normfac[], double synth[]) {
		double length;

		length = vectorLength(input);
		// just in case it gets too small
		if (length < MY_MIN_LENGTH)
			length = MY_MIN_LENGTH;

		normfac[0] = 1.0 / Math.sqrt(length);
		synth[0] = 0.0;

	}

	/**
	 * Normalize weights
	 * 
	 * @param w
	 *            Input weights
	 */

	private void normalizeWeight(double w[]) {
		int i;
		double len;

		len = vectorLength(w);
		// just incase it gets too small
		if (len < MY_MIN_LENGTH)
			len = MY_MIN_LENGTH;

		len = 1.0 / Math.sqrt(len);
		for (i = 0; i < myInputNeuronCount; i++)
			w[i] *= len;
		w[myInputNeuronCount] = 0;

	}

	/**
	 * Try an input patter. This can be used to present an input pattern to the
	 * network. Usually its best to call winner to get the winning neuron
	 * though.
	 * 
	 * @param input
	 *            Input pattern.
	 */

	protected void trial(double input[]) {
		double normfac[] = new double[1], synth[] = new double[1], optr[];
		normalizeInput(input, normfac, synth);

		for (int i = 0; i < myOutputNeuronCount; i++) {
			optr = myWeights[i];
			myOutput[i] = dotProduct(input, optr) * normfac[0] + synth[0]
					* optr[myInputNeuronCount];
			// Remap to bipolar (-1,1 to 0,1)
			myOutput[i] = 0.5 * (myOutput[i] + 1.0);
			// account for rounding
			if (myOutput[i] > 1.0)
				myOutput[i] = 1.0;
			if (myOutput[i] < 0.0)
				myOutput[i] = 0.0;
		}
	}

	/**
	 * Present an input pattern and get the winning neuron.
	 * 
	 * @param input
	 *            input pattern
	 * @param normfac
	 *            the result
	 * @param synth
	 *            synthetic last input
	 * @return The winning neuron number.
	 */
	private int winner(double input[], double normfac[], double synth[]) {
		int win = 0;
		/*
		 * нельзя заменить trial,т.к. задействованы массивы
		 */
		double biggest, optr[];

		normalizeInput(input, normfac, synth); // Normalize input

		// biggest = -1.E30;
		biggest = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < myOutputNeuronCount; i++) {
			optr = myWeights[i];
			myOutput[i] = dotProduct(input, optr) * normfac[0] + synth[0]
					* optr[myInputNeuronCount];
			// Remap to bipolar(-1,1 to 0,1)
			myOutput[i] = 0.5 * (myOutput[i] + 1.0);
			if (myOutput[i] > biggest) {
				biggest = myOutput[i];
				win = i;
			}
			// account for rounding
			if (myOutput[i] > 1.0)
				myOutput[i] = 1.0;
			if (myOutput[i] < 0.0)
				myOutput[i] = 0.0;
		}

		return win;
	}

	/**
	 * This method does much of the work of the learning process. This method
	 * evaluates the weights against the training set.
	 * 
	 * @param rate
	 *            learning rate
	 * @param learn_method
	 *            method(0=additive, 1=subtractive)
	 * @param won
	 *            a Holds how many times a given neuron won
	 * @param bigerr
	 *            a returns the error
	 * @param correc
	 *            a returns the correction
	 * @param work
	 *            a work area
	 * @exception java.lang.RuntimeException
	 */
	private void evaluateErrors(double rate, int learn_method, int won[],
			double bigerr[], double correc[][], double work[])
			throws RuntimeException {

		double[] dptr, normfac = new double[1];
		double synth[] = new double[1], cptr[], wptr[], length, diff;

		// reset correction and winner counts

		for (int x = 0; x < correc.length; x++) {
			for (int y = 0; y < correc[0].length; y++) {
				correc[x][y] = 0;
			}
		}

		for (int i = 0; i < won.length; i++)
			won[i] = 0;

		bigerr[0] = 0.0;

		int best;
		// loop through all training sets to determine correction
		for (int tset = 0; tset < myTrainingSet.getTrainingSetCount(); tset++) {
			dptr = myTrainingSet.getInputSet(tset);
			best = winner(dptr, normfac, synth);
			won[best]++;
			wptr = myWeights[best];
			cptr = correc[best];
			length = 0.0;

			for (int i = 0; i < myInputNeuronCount; i++) {
				diff = dptr[i] * normfac[0] - wptr[i];
				length += diff * diff;
				if (learn_method != 0)
					cptr[i] += diff;
				else
					work[i] = rate * dptr[i] * normfac[0] + wptr[i];
			}
			diff = synth[0] - wptr[myInputNeuronCount];
			length += diff * diff;
			if (learn_method != 0)
				cptr[myInputNeuronCount] += diff;
			else
				work[myInputNeuronCount] = rate * synth[0]
						+ wptr[myInputNeuronCount];

			if (length > bigerr[0])
				bigerr[0] = length;

			if (learn_method == 0) {
				normalizeWeight(work);
				for (int i = 0; i <= myInputNeuronCount; i++)
					cptr[i] += work[i] - wptr[i];
			}

		}

		bigerr[0] = Math.sqrt(bigerr[0]);
	}

	/**
	 * This method is called at the end of a training iteration. This method
	 * adjusts the weights based on the previous trial.
	 * 
	 * @param rate
	 *            learning rate
	 * @param learn_method
	 *            method(0=additive, 1=subtractive)
	 * @param won
	 *            a holds number of times each neuron won
	 * @param bigcorr
	 *            holds the error
	 * @param correc
	 *            holds the correction
	 */
	private void adjustWeights(double rate, int learn_method, int won[],
			double bigcorr[], double correc[][]) {

		bigcorr[0] = 0.0;

		for (int i = 0; i < myOutputNeuronCount; i++) {

			if (won[i] == 0)
				continue;

			double[] wptr = myWeights[i];
			double[] cptr = correc[i];

			double f = 1.0 / (double) won[i];
			if (learn_method != 0)
				f *= rate;

			double length = 0.0;
			double corr;
			for (int j = 0; j <= myInputNeuronCount; j++) {
				corr = f * cptr[j];
				wptr[j] += corr;
				length += corr * corr;
			}

			if (length > bigcorr[0])
				bigcorr[0] = length;
		}
		// scale the correction
		bigcorr[0] = Math.sqrt(bigcorr[0]) / rate;
	}

	/**
	 * If no neuron wins, then force a winner.
	 * 
	 * @param won
	 *            how many times each neuron won
	 * @exception java.lang.RuntimeException
	 */
	private void forceWin(int won[]) throws RuntimeException {
		int best, which = 0;
		double[] dptr, normfac = new double[1];
		double[] synth = new double[1];

		// double dist = 1.E30;
		double dist = Double.POSITIVE_INFINITY;
		for (int tset = 0; tset < myTrainingSet.getTrainingSetCount(); tset++) {
			dptr = myTrainingSet.getInputSet(tset);
			best = winner(dptr, normfac, synth);
			if (myOutput[best] < dist) {
				dist = myOutput[best];
				which = tset;
			}
		}

		dptr = myTrainingSet.getInputSet(which);
		best = winner(dptr, normfac, synth);

		// dist = -1.e30;
		dist = Double.NEGATIVE_INFINITY;
		int i = myOutputNeuronCount;
		while ((i--) > 0) {
			if (won[i] != 0)
				continue;
			if (myOutput[i] > dist) {
				dist = myOutput[i];
				which = i;
			}
		}

		double[] optr = myWeights[which];

		System.arraycopy(dptr, 0, optr, 0, dptr.length);

		optr[myInputNeuronCount] = synth[0] / normfac[0];
		normalizeWeight(optr);
	}

	private static final double MY_SOMETHING = 1E-5;
	
	/**
	 * This method is called to train the network. It can run for a very long
	 * time and will report progress back to the owner object.
	 * 
	 * @exception java.lang.RuntimeException
	 */
	public void learn() throws RuntimeException {
		double[] dptr;

		for (int tset = 0; tset < myTrainingSet.getTrainingSetCount(); tset++) {
			dptr = myTrainingSet.getInputSet(tset);
/*
 * ne byvaet, t.k. elementy vectorov +-0.5
 */
			if (vectorLength(dptr) < MY_MIN_LENGTH) {
				throw (new RuntimeException(
						"Multiplicative normalization has null training case"));
			}

		}

		myTotalError = 1.0;
		// Preserve best here
		KohonenNetwork bestnet = new KohonenNetwork(myInputNeuronCount,
				myOutputNeuronCount, myReportable);

		int[] won = new int[myOutputNeuronCount];
		double[][] correc = new double[myOutputNeuronCount][myInputNeuronCount + 1];
		double work[];
		if (myLearnMethod == 0)
			work = new double[myInputNeuronCount + 1];
		else
			work = null;

		double rate = myLearnRate;

		initialize();
		// double best_err = 1.e30;
		double best_err = Double.POSITIVE_INFINITY;
		// main loop:

		int n_retry = 0;

		double bigerr[] = new double[1];
		double bigcorr[] = new double[1];

		for (int iter = 0;; iter++) {

			evaluateErrors(rate, myLearnMethod, won, bigerr, correc, work);

			myTotalError = bigerr[0];

			if (myTotalError < best_err) {
				best_err = myTotalError;
				copyWeights(bestnet, this);
			}

			int winners = 0;
			for (int i = 0; i < won.length; i++)
				if (won[i] != 0)
					winners++;

			if (bigerr[0] < myQuitError)
				break;

			if ((winners < myOutputNeuronCount)
					&& (winners < myTrainingSet.getTrainingSetCount())) {
				forceWin(won);
				continue;
			}

			adjustWeights(rate, myLearnMethod, won, bigcorr, correc);

			myReportable.update(n_retry, myTotalError, best_err);
			if (myHalt) {
				myReportable.update(n_retry, myTotalError, best_err);
				break;
			}
			Thread.yield();

			/*
			 *  вот что это?! 
			 */
			if (bigcorr[0] < MY_SOMETHING) {
				if (++n_retry > myRetriesNumber)
					break;
				initialize();
				iter = -1;
				rate = myLearnRate;
				continue;
			}

			if (rate > 0.01)
				rate *= myRateReduction;

		}

		// done

		copyWeights(this, bestnet);

		for (int i = 0; i < myOutputNeuronCount; i++)
			normalizeWeight(myWeights[i]);

		myHalt = true;
		n_retry++;
		myReportable.update(n_retry, myTotalError, best_err);
	}

	/**
	 * Called to initialize the Kononen network.
	 */
	private void initialize() {
		clearWeights();
		randomizeWeights(myWeights);
		
		double optr[];
		for (int i = 0; i < myOutputNeuronCount; i++) {
			optr = myWeights[i];
			normalizeWeight(optr);
		}
	}

	/**
	 * My bonus. Doesn't change fields.
	 */
	public double[] getOutput(double[] input) {
		double[] out = new double[myOutputNeuronCount];
		double[] normfac = new double[1];
		double[] synth = new double[1];
		normalizeInput(input, normfac, synth); // Normalize input

		double[] optr;

		for (int i = 0; i < myOutputNeuronCount; i++) {
			optr = myWeights[i];
			out[i] = dotProduct(input, optr) * normfac[0] + synth[0]
					* optr[myInputNeuronCount];
			// Remap to bipolar(-1,1 to 0,1)
			out[i] = 0.5 * (out[i] + 1.0);

			// account for rounding
			if (out[i] > 1.0)
				out[i] = 1.0;
			if (out[i] < 0.0)
				out[i] = 0.0;
		}

		return out;
	}
	
	/**
	 * My bonus. 
	 */
	public int getWinner(double[] input) {
		return winner(input, new double[1], new double[1] );
	}
	
}