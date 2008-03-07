package org.amse.shElena.toyRec.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.amse.shElena.toyRec.algorithms.kohonenNet.KohonenNetwork;
import org.amse.shElena.toyRec.algorithms.kohonenNet.NeuralReportable;
import org.amse.shElena.toyRec.algorithms.kohonenNet.TrainingSet;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class KohonenNetworkAlgorithm implements IAlgorithm, Runnable {
	private KohonenNetworkAlgorithm() {
		myReportable = new NeuralReportable() {
			public void update(int retry, double totalError, double bestError) {
			}
		};
	}

	private static IAlgorithm myInstance = new KohonenNetworkAlgorithm();

	public static IAlgorithm getInstance() {
		return myInstance;
	}

	public String getName() {
		return "Kohonen network algorithm";
	}

	private NeuralReportable myReportable;

	private KohonenNetwork myNetwork;

	/**
	 * The background thread used for training.
	 */
	// private Thread myTrainThread = null;
	private ISampleBase myBase;

	public void learnBase(ISampleBase base) {
		myBase = base;

		// myTrainThread = new Thread(this);
		// myTrainThread.start();
		int inputNeuron = myBase.getSampleSize();
		int outputNeuron = myBase.size();

		TrainingSet set = new TrainingSet(inputNeuron);
		set.setTrainingSetCount(myBase.size());

		ISample[] samples = myBase.getSamples();
		for (int i = 0; i < samples.length; i++) {
			ISample s = samples[i];
			boolean[] pic = s.getPicture();
			for (int j = 0; j < pic.length; j++) {

				set.setInput(i, j, pic[j] ? .5 : -.5); // не 0 1,а 0.5 и
				// -0.5
			}
		}

		myNetwork = new KohonenNetwork(inputNeuron, outputNeuron, myReportable);
		myNetwork.setTrainingSet(set);
		myNetwork.learn();
	}

	/**
	 * Run method for the background training thread. Now unused.
	 */
	// обучение сети)))
	public void run() {
		// try {
		/*
		 * int inputNeuron = MainEntry.DOWNSAMPLE_HEIGHT
		 * MainEntry.DOWNSAMPLE_WIDTH; int outputNeuron =
		 * letterListModel.size();
		 * 
		 * TrainingSet set = new TrainingSet(inputNeuron,outputNeuron);
		 * set.setTrainingSetCount(letterListModel.size());
		 */

		int inputNeuron = myBase.getSampleSize();

		TrainingSet set = new TrainingSet(inputNeuron);
		set.setTrainingSetCount(myBase.size());

		/*
		 * for (int t = 0; t < letterListModel.size(); t++) { int idx = 0;
		 * SampleData ds = (SampleData) letterListModel.getElementAt(t); for
		 * (int y = 0; y < ds.getHeight(); y++) { for (int x = 0; x <
		 * ds.getWidth(); x++) { set.setInput(t, idx++, ds.getData(x, y) ? .5 :
		 * -.5); /* не 0 1,а 0.5 и -0.5 } } }
		 */

		ISample[] samples = myBase.getSamples();
		for (int i = 0; i < samples.length; i++) {
			ISample s = samples[i];
			boolean[] pic = s.getPicture();
			for (int j = 0; j < pic.length; j++) {

				set.setInput(i, j, pic[j] ? .5 : -.5); // не 0 1,а 0.5 и
				// -0.5
			}
		}

		// myNetwork = new KohonenNetwork(inputNeuron, outputNeuron,
		// myReportable);
		myNetwork.setTrainingSet(set);
		myNetwork.learn();
		/*
		 * } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " +
		 * e, "Training", JOptionPane.ERROR_MESSAGE); }
		 */
	}

	public List<ComparisonResult> getWholeResults(boolean[] picture) {
		double[] input = new double[picture.length];

		for (int i = 0; i < picture.length; i++) {
			input[i] = picture[i] ? .5 : -.5;
		}

		double[] output = myNetwork.getOutput(input);
		
		Character[] map = mapNeurons();
	
		List<ComparisonResult> list = new ArrayList<ComparisonResult>(myBase
				.size());
		int percentage;
		for (int i = 0; i < map.length; i++) {
			percentage = (int) (100 * (1 - output[i]));
			if(map[i] != '?'){
			list.add(new ComparisonResult(map[i], percentage));
			}
			//System.out.println("" + map[i] + " " + output[i]);
		}
		return list;
	}

	private Character[] mapNeurons() {
		Character[] map = new Character[myBase.size()];

		for (int i = 0; i < map.length; i++) {
			map[i] = '?';
		}

		for (ISample s : myBase.getSamples()) {
			boolean[] pic = s.getPicture();

			double[] input = new double[pic.length];

			for (int x = 0; x < input.length; x++) {
				input[x] = pic[x] ? .5 : -.5;
			}

			int best = myNetwork.getWinner(input);
			map[best] = s.getSymbol();
		}

		/*
		 * cпецэффект )))
		 */
	/*	for (int x = 0; x < map.length; x++) {
			System.out.print(map[x] + " ");
		}
		System.out.println(" ");*/
		return map;
	}
}
