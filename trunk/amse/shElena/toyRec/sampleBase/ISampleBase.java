package org.amse.shElena.toyRec.sampleBase;

import java.io.File;

import org.amse.shElena.toyRec.samples.ISample;

public interface ISampleBase extends Iterable<ISample>{
	// Collection<Description> getDescription(char c);

	void addSample(ISample sample);

	void removeSample(ISample sample);
	
	// state id changes every time SampleBase changes
	//so, it helps testing if changes occured
	int getStateID();
	
	boolean isEmpty();

	//boolean isRemovable();

	//void setRemovable(boolean removable);

	// Collection<Character> charSet();

	int size();

	/*
	 * maybe, exception? No - too much exceptions
	 */
	boolean loadSampleBase(File file);
	
	boolean saveSampleBase(File file);
	
	ISample[] getSamples();
	
	int getSampleSize();
	
	

	//boolean enlargeByImageFile(File file);
}
