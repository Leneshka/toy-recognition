package org.amse.shElena.toyRec.manager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import org.amse.shElena.toyRec.algorithms.ComparisonResult;
import org.amse.shElena.toyRec.algorithms.Algorithm;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public interface IManager {
	ISample learnSymbol(Character c, BufferedImage image);

	List<ComparisonResult> recognizeImage(BufferedImage image);

	void removeSample(ISample sample);

	void saveSymbolBase(File file);
	
	void setSymbolBase(ISampleBase base);
//	ISampleBase getSymbolBase();
	
	ISampleBase createPictureSymbolBase(File file);
	ISample createSample(File file);
	ISampleBase createFileSymbolBase(File file);
	
	void addSampleBase(ISampleBase base);
	void addSample(ISample sample);
	void newSymbolBase();

	ISample makeSample(Character symbol, BufferedImage image);
	
	void testRecognition(ISampleBase base, List<Character> recognized, List<Character> unrecognized );

	int getWidth();

	int getHeight();
	
	//int getBaseStateID();

	ISample[] getSamples();
	
	void setAlgorithm(Algorithm algorithm);
	
	Algorithm getAlgorithm();
	
	/**
	 * If the picture is totaly white, returns 0
	 * Needed ridhtBorder = leftBorder in this case
	 */
	public int getRightBorder(BufferedImage image);
	/**
	 * If the picture is totaly white, returns 0
	 * Needed ridhtBorder = leftBorder in this case
	 */
	public  int getLeftBorder(BufferedImage image);
	/**
	 * If the picture is totaly white, returns 0
	 * Needed upperBorder = lowerBorder in this case
	 */
	public  int getUpperBorder(BufferedImage image);
	
	/**
	 * If the picture is totaly white, returns 0
	 * Needed upperBorder = lowerBorder in this case
	 */
	public  int getLowerBorder(BufferedImage image);
}
