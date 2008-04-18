package org.amse.shElena.toyRec.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.amse.shElena.toyRec.algorithms.kohonenNet.KohonenNetwork;
import org.amse.shElena.toyRec.algorithms.kohonenNet.NeuralReportable;
import org.amse.shElena.toyRec.algorithms.kohonenNet.TrainingSet;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class KohonenNetworkAlgorithm extends Algorithm{
	private KohonenNetworkAlgorithm() {
		myReportable = new NeuralReportable() {
			public void update(int retry, double totalError, double bestError) {
			}
		};
	}

	private static Algorithm myInstance = new KohonenNetworkAlgorithm();

	public static Algorithm getInstance() {
		return myInstance;
	}

	public String toString() {
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
/*	public void run() {
		int inputNeuron = myBase.getSampleSize();

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

		// myNetwork = new KohonenNetwork(inputNeuron, outputNeuron,
		// myReportable);
		myNetwork.setTrainingSet(set);
		myNetwork.learn();
		
	}*/

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
