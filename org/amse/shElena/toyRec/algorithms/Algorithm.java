package org.amse.shElena.toyRec.algorithms;

import java.util.List;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

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
	
	public  List<ComparisonResult> getWholeResults(ISample sample){
		return getWholeResults(sample.getPicture());
	}
	
	//Character recognize(boolean[] picture);
	
	public abstract String toString();
}
