package org.amse.shElena.toyRec.algorithms;

import java.util.List;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;

public abstract class Algorithm {
	//void learnSampleBase(ISampleBase base);

	//maybe, just whole result?
	//Character getNearestSymbol(BufferedImage image);

	//Character getNearestSymbolClass(BufferedImage image);
	protected static Algorithm myInstance;

	public static Algorithm getInstance() {
		return myInstance;
	}
	public abstract void learnBase(ISampleBase base);
	
	public abstract List<ComparisonResult> getWholeResults(boolean[] picture);
	
	//Character recognize(boolean[] picture);
	
	public abstract String toString();
}
