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
import org.amse.shElena.toyRec.samples.SampleFactory;

public class Manager implements IManager {
	private int myWidth;

	private int myHeight;

	private int myLastLearnedBaseID;

	private ISampleBase myBase;

	private IAlgorithm myAlgorithm;

	private SampleFactory myFactory;

	public Manager(int width, int height, IAlgorithm algorithm) {
		super();
		myWidth = width;
		myHeight = height;
		myBase = new SampleBase(width * height);
		myAlgorithm = algorithm;
		myFactory = new SampleFactory(width, height);
		myLastLearnedBaseID = Integer.MIN_VALUE;
	}

	public ISample learnSymbol(Character symbol, BufferedImage image) {
		ISample sample = makeSample(symbol, image);

		myBase.addSample(sample);

		return sample;
	}

	public ISample makeSample(Character symbol, BufferedImage image) {
		image = getEssentialRectangle(image);

		return myFactory.makeRelativeSample(symbol, image);
	}

	/*
	 * public void loadSymbolBase(File file) { myLastLearnedBaseID =
	 * Integer.MIN_VALUE; myBase.loadSampleBase(file); }
	 */

	public void newSymbolBase() {
		myBase = new SampleBase(myWidth * myHeight);
		myLastLearnedBaseID = Integer.MIN_VALUE;
	}

	public List<ComparisonResult> recognizeImage(BufferedImage image) {
		if (myLastLearnedBaseID != myBase.getStateID()) {
			myAlgorithm.learnBase(myBase);
			myLastLearnedBaseID = myBase.getStateID();
		}

		image = getEssentialRectangle(image);
		ISample sample = myFactory.makeRelativeSample(' ', image);
		return myAlgorithm.getWholeResults(sample.getPicture());
	}

	public void removeSample(ISample sample) {
		myBase.removeSample(sample);
	}

	public void saveSymbolBase(File file) {
		myBase.saveSampleBase(file);
	}

	private BufferedImage getEssentialRectangle(BufferedImage image) {
		int leftBorder = 0;
		int i = 0;
		boolean found = false;

		while (!found && i < image.getWidth()) {
			if (!columnIsWhite(i, image)) {
				found = true;
				leftBorder = i;
			}
			i++;
		}

		int rightBorder = 0;
		int k = image.getWidth() - 1;
		found = false;

		while (!found && k > -1) {
			if (!columnIsWhite(k, image)) {
				found = true;
				rightBorder = k;
			}
			k--;
		}

		int lowerBorder = 0;
		int j = 0;
		found = false;

		while (!found && j < image.getHeight()) {
			if (!rowIsWhite(j, image)) {
				found = true;
				lowerBorder = j;
			}
			j++;
		}

		int upperBorder = 0;
		int m = image.getHeight() - 1;
		found = false;

		while (!found && m > -1) {
			if (!rowIsWhite(m, image)) {
				found = true;
				upperBorder = m;
			}
			m--;
		}

		return image.getSubimage(leftBorder, lowerBorder, rightBorder
				- leftBorder + 1, upperBorder - lowerBorder + 1);
	}

	private boolean rowIsWhite(int number, BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++) {
			if (image.getRGB(i, number) == -16777216) {
				return false;
			}
		}
		return true;
	}

	private boolean columnIsWhite(int number, BufferedImage image) {
		for (int j = 0; j < image.getHeight(); j++) {
			if (image.getRGB(number, j) == -16777216) {
				return false;
			}
		}
		return true;
	}

	public int getWidth() {
		return myWidth;
	}

	public int getHeight() {
		return myHeight;
	}

	public ISample[] getSamples() {
		return myBase.getSamples();
	}

	public int getBaseStateID() {
		return myBase.getStateID();
	}

	public void setAlgorithm(IAlgorithm algorithm) {
		myLastLearnedBaseID = Integer.MIN_VALUE;
		myAlgorithm = algorithm;
	}

	public IAlgorithm getAlgorithm() {
		return myAlgorithm;
	}

	public ISampleBase createFileSymbolBase(File file) {
		ISampleBase b = new SampleBase(myWidth * myHeight);
		b.loadSampleBase(file);
		return b;
	}

	/**
	 * Only for directories
	 */
	public ISampleBase createPictureSymbolBase(File file) {
		ISampleBase b = new SampleBase(myWidth * myHeight);
		
		if (file != null &&  file.isDirectory()){
				// f - directory
				File[] files = file.listFiles();
				for (File f : files) {
					loadPicture(f, b);
				}		
		}
		return b;
	}

	private void loadPicture(File file, ISampleBase base) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			return;
		}

		String filename = file.getName();

		if (filename.length() == 1 || image == null) {
			return;
		}

		ISample sample = makeSample(filename.charAt(0), image);

		base.addSample(sample);
	}

	public void testRecognition(ISampleBase base, List<Character> recognized,
			List<Character> unrecognized) {
		if (myLastLearnedBaseID != myBase.getStateID()) {
			myAlgorithm.learnBase(myBase);
			myLastLearnedBaseID = myBase.getStateID();
		}

		for (ISample s : base) {
			boolean[] pic = s.getPicture();

			List<ComparisonResult> res = myAlgorithm.getWholeResults(pic);
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

	public void setSymbolBase(ISampleBase base) {
		myLastLearnedBaseID = Integer.MIN_VALUE;
		myBase = base;
	}
}
