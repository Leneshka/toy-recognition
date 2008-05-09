package org.amse.shElena.toyRec.manager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import org.amse.shElena.toyRec.algorithms.ComparisonResult;
import org.amse.shElena.toyRec.algorithms.IAlgorithm;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.sampleBase.SampleBase;
import org.amse.shElena.toyRec.samples.ISample;
import org.amse.shElena.toyRec.samples.Sampler;

public class Manager implements IManager {
	private ISampleBase myBase;

	private IAlgorithm myAlgorithm;

	public Manager() {
		super();
		myBase = new SampleBase();
	}

	public ISample learnSymbol(Character symbol, BufferedImage image) {
		ISample sample = Sampler.makeSample(symbol, image);

		myBase.addSample(sample);

		return sample;
	}

	public void newSymbolBase() {
		myBase = new SampleBase();
	}

	public List<ComparisonResult> recognizeImage(BufferedImage image) {
		myAlgorithm.learnBase(myBase);

		image = Sampler.getEssentialRectangle(image);
		return myAlgorithm.getWholeResults(Sampler.makeSampleImage(image));
	}

	public List<ComparisonResult> recognizeSampleImage(boolean[][] image) {
		return myAlgorithm.getWholeResults(image);
	}

	public void removeSample(ISample sample) {
		myBase.removeSample(sample);
	}

	public void saveSymbolBase(File file) {
		myBase.saveSampleBase(file);
	}

	public ISample[] getSamples() {
		return myBase.getSamples();
	}

	public void setAlgorithm(IAlgorithm algorithm) {
		myAlgorithm = algorithm;
	}

	public IAlgorithm getAlgorithm() {
		return myAlgorithm;
	}

	public ISampleBase createFileSymbolBase(File file) {
		ISampleBase b = new SampleBase();
		b.loadSampleBase(file);
		return b;
	}

	public ISample createSample(File file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			return null;
		}

		String filename = file.getName();

		if (filename.length() == 0 || image == null) {
			return null;
		}

		ISample sample = Sampler.makeSample(filename.charAt(0), image);
		return sample;
	}

	/**
	 * Only for directories
	 */
	public ISampleBase createPictureSymbolBase(File file) {
		ISampleBase b = new SampleBase();

		if (file != null && file.isDirectory()) {
			// f - directory
			File[] files = file.listFiles();
			for (File f : files) {
				ISample s = createSample(f);
				if (s != null) {
					b.addSample(s);
				}
			}
		}
		return b;
	}

	public ISampleBase getSymbolBase() {
		return myBase;
	}

	public void setSymbolBase(ISampleBase base) {
		myBase = base;
	}

	public void testRecognition(ISampleBase base, List<Character> recognized,
			List<Character> unrecognized) {

		myAlgorithm.learnBase(myBase);

		for (ISample s : base) {
			List<ComparisonResult> res = myAlgorithm.getWholeResults(s
					.getImage());

			Character real = s.getSymbol();

			if (res.size() == 0) {
				unrecognized.add(real);
				continue;
			}

			Collections.sort(res);
			Character guess = res.get(0).getSymbol();

			if (guess == real) {
				recognized.add(real);
			} else {
				unrecognized.add(real);
			}
		}

	}

	public void addSampleBase(ISampleBase base) {
		myBase.addSampleBase(base);
	}

	public void addSample(File file) {
		myBase.addSample(createSample(file));
	}

	public void addSample(ISample sample) {
		myBase.addSample(sample);
	}
}
