package org.amse.shElena.toyRec.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.amse.shElena.toyRec.algorithms.kohonenNetwork.KohonenNetwork;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;
import org.amse.shElena.toyRec.samples.Sampler;

public class KohonenNetworkAlgorithm implements IAlgorithm {
	private final int MY_WIDTH;

	private final int MY_HEIGHT;

	public KohonenNetworkAlgorithm(int width, int height) {
		MY_WIDTH = width;
		MY_HEIGHT = height;
	}

	public String toString() {
		return "My Kohonen network algorithm";
	}

	private KohonenNetwork myNetwork;

	private ISampleBase myBase;

	public void learnBase(ISampleBase base) {
		myBase = base;

		myNetwork = new KohonenNetwork(MY_WIDTH, MY_HEIGHT);
		myNetwork.setInputs(base, base.size());
		myNetwork.learn();
	}

	public List<ComparisonResult> getWholeResults(boolean[][] image) {
		boolean[] sample = Sampler.makeAlgorithmRelativeSample(image, MY_WIDTH,
				MY_HEIGHT);
		double[] input = new double[sample.length];

		for (int i = 0; i < sample.length; i++) {
			input[i] = sample[i] ? .5 : -.5;
		}

		double[] output = myNetwork.getOutput(input);

		Character[] map = mapNeurons();

		List<ComparisonResult> list = new ArrayList<ComparisonResult>(myBase
				.size());
		int percentage;
		for (int i = 0; i < map.length; i++) {
			percentage = (int) (100 * (1 - output[i]));
			if (map[i] != '?') {
				list.add(new ComparisonResult(map[i], percentage));
			}
		}
		return list;
	}

	private Character[] mapNeurons() {
		Character[] map = new Character[myBase.size()];

		for (int i = 0; i < map.length; i++) {
			map[i] = '?';
		}

		for (ISample s : myBase.getSamples()) {
			boolean[] pic = Sampler.makeAlgorithmRelativeSample(s.getImage(), MY_WIDTH,
					MY_HEIGHT);

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
