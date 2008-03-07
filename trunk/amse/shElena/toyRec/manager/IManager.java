package org.amse.shElena.toyRec.manager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import org.amse.shElena.toyRec.algorithms.ComparisonResult;
import org.amse.shElena.toyRec.algorithms.IAlgorithm;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public interface IManager {
	ISample learnSymbol(Character c, BufferedImage image);

	List<ComparisonResult> recognizeImage(BufferedImage image);

	void removeSample(ISample sample);

	boolean saveSymbolBase(File file);

	boolean loadSymbolBase(File file);

	
	ISampleBase createPictureSymbolBase(File file);
	ISampleBase createXMLSymbolBase(File file);
	
	void newSymbolBase();

	public ISample makeSample(Character symbol, BufferedImage image);
	
	public void testRecognition(ISampleBase base, List<Character> recognized, List<Character> unrecognized );

	public int getWidth();

	public int getHeight();
	
	public int getBaseStateID();

	public ISample[] getSamples();
	
	public void setAlgorithm(IAlgorithm algorithm);
	
	public IAlgorithm getAlgorithm();
}
