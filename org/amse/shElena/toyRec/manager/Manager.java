package org.amse.shElena.toyRec.manager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import org.amse.shElena.toyRec.algorithms.ComparisonResult;
import org.amse.shElena.toyRec.algorithms.Algorithm;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.sampleBase.SampleBase;
import org.amse.shElena.toyRec.samples.ISample;
import org.amse.shElena.toyRec.samples.Sampler;

public class Manager implements IManager {
	private int myWidth;

	private int myHeight;

	//private int myLastLearnedBaseID;

	private ISampleBase myBase;

	private Algorithm myAlgorithm;

	private Sampler mySampler;

	public Manager(int width, int height, Algorithm algorithm) {
		super();
		myWidth = width;
		myHeight = height;
		myBase = new SampleBase(width * height);
		myAlgorithm = algorithm;
		mySampler = new Sampler(width, height);
	//	myLastLearnedBaseID = Integer.MIN_VALUE;
	}

	public ISample learnSymbol(Character symbol, BufferedImage image) {
		ISample sample = makeSample(symbol, image);

		myBase.addSample(sample);

		return sample;
	}

	public ISample makeSample(Character symbol, BufferedImage image) {
		image = getEssentialRectangle(image);

		return mySampler.makeRelativeSample(symbol, image);
	}

	/*
	 * public void loadSymbolBase(File file) { myLastLearnedBaseID =
	 * Integer.MIN_VALUE; myBase.loadSampleBase(file); }
	 */

	public void newSymbolBase() {
		myBase = new SampleBase(myWidth * myHeight);
		//myLastLearnedBaseID = Integer.MIN_VALUE;
	}

	public List<ComparisonResult> recognizeImage(BufferedImage image) {
		/*if (myLastLearnedBaseID != myBase.getStateID()) {
			myAlgorithm.learnBase(myBase);
			myLastLearnedBaseID = myBase.getStateID();
		}*/
		myAlgorithm.learnBase(myBase);
		
		image = getEssentialRectangle(image);
		ISample sample = mySampler.makeRelativeSample(' ', image);
		return myAlgorithm.getWholeResults(sample.getPicture());
	}

	public void removeSample(ISample sample) {
		myBase.removeSample(sample);
	}

	public void saveSymbolBase(File file) {
		myBase.saveSampleBase(file);
	}

	/**
	 * Math.max(right - left,1), Math.max(upper - lower,1)
	 * for case picture is totally white
	 * 
	 * @param image
	 * @return
	 */
	private BufferedImage getEssentialRectangle(BufferedImage image) {
		int right = getRightBorder(image);
		int left = getLeftBorder(image);
		int upper = getUpperBorder(image);
		int lower = getLowerBorder(image);
		return image.getSubimage(left, lower, Math.max(right - left + 1,1), Math.max(upper - lower + 1,1));
	}

	/**
	 * If the picture is totaly white, returns 0 Needed ridhtBorder = leftBorder
	 * in this case
	 */
	public int getRightBorder(BufferedImage image) {
		int i = image.getWidth() - 1;
		while (i > 0) {
			if (!columnIsWhite(i, image)) {
				return i;
			}
			i--;
		}
		return 0;
	}

	/**
	 * If the picture is totaly white, returns 0 Needed ridhtBorder = leftBorder
	 * in this case
	 */
	public int getLeftBorder(BufferedImage image) {
		int i = 0;
		while (i < image.getWidth()) {
			if (!columnIsWhite(i, image)) {
				return i;
			}
			i++;
		}
		return 0;
	}

	/**
	 * If the picture is totaly white, returns 0 Needed upperBorder =
	 * lowerBorder in this case
	 */
	public int getUpperBorder(BufferedImage image) {
		int j = image.getHeight() - 1;
		while (j > 0) {
			if (!rowIsWhite(j, image)) {
				return j;
			}
			j--;
		}
		return 0;
	}

	/**
	 * If the picture is totaly white, returns 0 Needed upperBorder =
	 * lowerBorder in this case
	 */
	public int getLowerBorder(BufferedImage image) {
		int j = 0;
		while (j < image.getHeight()) {
			if (!rowIsWhite(j, image)) {
				return j;
			}
			j++;
		}
		return 0;
	}

	private boolean rowIsWhite(int number, BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++) {
			if (image.getRGB(i, number) == -16777216) {
				return false;
			}
			
		/*	if (image.getRGB(i, number) != -16777216 && image.getRGB(i, number) != -1) {
				System.out.println(image.getRGB(i, number));
			}*/
			
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

	/*public int getBaseStateID() {
		return myBase.getStateID();
	}*/

	public void setAlgorithm(Algorithm algorithm) {
	//	myLastLearnedBaseID = Integer.MIN_VALUE;
		myAlgorithm = algorithm;
	}

	public Algorithm getAlgorithm() {
		return myAlgorithm;
	}

	public ISampleBase createFileSymbolBase(File file) {
		ISampleBase b = new SampleBase(myWidth * myHeight);
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

		ISample sample = makeSample(filename.charAt(0), image);
		return sample;
	}

	/**
	 * Only for directories
	 */
	public ISampleBase createPictureSymbolBase(File file) {
		ISampleBase b = new SampleBase(myWidth * myHeight);

		if (file != null && file.isDirectory()) {
			// f - directory
			File[] files = file.listFiles();
			for (File f : files) {
				ISample s = createSample(f);
				if(s!=null){
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
		//myLastLearnedBaseID = Integer.MIN_VALUE;
		myBase = base;
	}

	public void testRecognition(ISampleBase base, List<Character> recognized,
			List<Character> unrecognized) {
	/*	if (myLastLearnedBaseID != myBase.getStateID()) {
			myAlgorithm.learnBase(myBase);
			myLastLearnedBaseID = myBase.getStateID();
		}*/
		
		myAlgorithm.learnBase(myBase);

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

	public void addSampleBase(ISampleBase base) {
		myBase.addSampleBase(base);
	}

	public void addSample(ISample sample) {
		myBase.addSample(sample);
	}
}