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
	void loadSampleBase(File file);
	
	void saveSampleBase(File file);
	/*
	 * don't forget to check sample size! 
	 */
	void addSampleBase(ISampleBase base);
	
	ISample[] getSamples();
	
	int getSampleSize();
	
	void clear();

	//boolean enlargeByImageFile(File file);
}
