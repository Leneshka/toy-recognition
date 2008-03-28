package org.amse.shElena.toyRec.algorithms.kohonenNet;

/// класс дл€ хранени€ всех входов, на которых будет тренироватьс€ сеть

public class TrainingSet {

	protected int myInputCount;

	protected double[][] myInput;

	protected int myTrainingSetCount;

	/**
	 * The constructor.
	 * 
	 * @param inputCount
	 *            Number of input neurons
	 */
	public TrainingSet(int inputCount) {
		myInputCount = inputCount;
		myTrainingSetCount = 0;
	}

	/**
	 * Get the input neuron count
	 * 
	 * @return The input neuron count
	 */
	public int getInputCount() {
		return myInputCount;
	}

	// ///////// пишем все входы и выходы, видимо
	/**
	 * Set the number of entries in the training set. This method also allocates
	 * space for them.
	 * 
	 * @param trainingSetCount
	 *            How many entries in the training set.
	 */
	public void setTrainingSetCount(int trainingSetCount) {
		myTrainingSetCount = trainingSetCount;
		myInput = new double[trainingSetCount][myInputCount];
	}

	/**
	 * Get the training set data.
	 * 
	 * @return Training set data.
	 */
	public int getTrainingSetCount() {
		return myTrainingSetCount;
	}

	/**
	 * Set one of the training set's inputs.
	 * 
	 * @param set
	 *            The entry number
	 * @param index
	 *            The index(which item in that set)
	 * @param value
	 *            The value
	 * @exception java.lang.RuntimeException
	 */
	public void setInput(int set, int index, double value)
			throws RuntimeException {
		if ((set < 0) || (set >= myTrainingSetCount))
			throw (new RuntimeException("Training set out of range:" + set));
		if ((index < 0) || (index >= myInputCount))
			throw (new RuntimeException("Training input index out of range:"
					+ index));
		myInput[set][index] = value;
	}

	/**
	 * Get a specified input value.
	 * 
	 * @param set
	 *            The input entry.
	 * @param index
	 *            The index
	 * @return An individual input
	 * @exception java.lang.RuntimeException
	 */
	public double getInput(int set, int index) throws RuntimeException {
		if ((set < 0) || (set >= myTrainingSetCount))
			throw (new RuntimeException("Training set out of range:" + set));
		if ((index < 0) || (index >= myInputCount))
			throw (new RuntimeException("Training input index out of range:"
					+ index));
		return myInput[set][index];
	}

	/**
	 * Get an input set.
	 * 
	 * @param set
	 *            The entry requested.
	 * @return The complete input set as an array.
	 * @exception java.lang.RuntimeException
	 */

	public double[] getInputSet(int set) throws RuntimeException {
		if ((set < 0) || (set >= myTrainingSetCount))
			throw (new RuntimeException("Training set out of range:" + set));
		return myInput[set];
	}

}
