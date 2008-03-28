package org.amse.shElena.toyRec.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.amse.shElena.toyRec.algorithms.myKohonenNetwork.MyKohonenNetwork;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class MyKohonenNetworkAlgorithm implements IAlgorithm {
	private static IAlgorithm myInstance = new MyKohonenNetworkAlgorithm();

	public static IAlgorithm getInstance() {
		return myInstance;
	}

	public String toString() {
		return "My Kohonen network algorithm";
	}

	private MyKohonenNetwork myNetwork;
	
	private ISampleBase myBase;

	public void learnBase(ISampleBase base) {
		myBase = base;

		myNetwork = new MyKohonenNetwork();
		myNetwork.setInputs(base, base.size());
		myNetwork.learn();
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

			int best = myNetwork.winner(input);
			map[best] = s.getSymbol();
		}

		return map;
	}
}
