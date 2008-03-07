package org.amse.shElena.toyRec.algorithms;

//test

import java.util.List;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;

public interface IAlgorithm {
	//void learnSampleBase(ISampleBase base);

	//maybe, just whole result?
	//Character getNearestSymbol(BufferedImage image);

	//Character getNearestSymbolClass(BufferedImage image);
	
	void learnBase(ISampleBase base);
	
	List<ComparisonResult> getWholeResults(boolean[] picture);
	
	//Character recognize(boolean[] picture);
	
	String getName();
}
