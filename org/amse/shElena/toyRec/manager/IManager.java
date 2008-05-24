package org.amse.shElena.toyRec.manager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import org.amse.shElena.toyRec.algorithms.ComparisonResult;
import org.amse.shElena.toyRec.algorithms.IAlgorithm;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public interface IManager {
	// algorithm
	void setAlgorithm(IAlgorithm algorithm);

	IAlgorithm getAlgorithm();

	// learning & recognition
	ISample learnSymbol(Character c, BufferedImage image);

	List<ComparisonResult> recognizeSampleImage(boolean[][] image);

	List<ComparisonResult> recognizeImage(BufferedImage image);

	void testRecognition(ISampleBase base, List<Character> recognized,
			List<Character> unrecognized);

	// samples
	ISample createSample(File file);

	ISample[] getSamples();

	// symbol base
	void newSymbolBase();

	void setSymbolBase(ISampleBase base);

	void addSampleBase(ISampleBase base);

	void addSample(ISample sample);

	void removeSample(ISample sample);

	// symbol base % files
	void saveSymbolBase(File file);

	ISampleBase createPictureSymbolBase(File file);

	ISampleBase createFileSymbolBase(File file);

}
