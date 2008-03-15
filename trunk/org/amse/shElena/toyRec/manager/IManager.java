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

	void saveSymbolBase(File file);
	
	void setSymbolBase(ISampleBase base);
	
	ISampleBase createPictureSymbolBase(File file);
	ISampleBase createFileSymbolBase(File file);
	
	void newSymbolBase();

	ISample makeSample(Character symbol, BufferedImage image);
	
	void testRecognition(ISampleBase base, List<Character> recognized, List<Character> unrecognized );

	int getWidth();

	int getHeight();
	
	int getBaseStateID();

	ISample[] getSamples();
	
	void setAlgorithm(IAlgorithm algorithm);
	
	IAlgorithm getAlgorithm();
}
