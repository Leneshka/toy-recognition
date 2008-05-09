package org.amse.shElena.toyRec.sampleBase;

import java.io.File;

import org.amse.shElena.toyRec.samples.ISample;

public interface ISampleBase extends Iterable<ISample> {
	void addSample(ISample sample);

	void removeSample(ISample sample);

	boolean isEmpty();

	int size();

	void loadSampleBase(File file);

	void saveSampleBase(File file);

	void addSampleBase(ISampleBase base);

	ISample[] getSamples();

	void clear();
}
