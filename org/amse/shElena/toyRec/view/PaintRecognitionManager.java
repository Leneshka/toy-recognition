package org.amse.shElena.toyRec.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.List;

import org.amse.shElena.toyRec.algorithms.ComparisonResult;
import org.amse.shElena.toyRec.algorithms.IAlgorithm;
import org.amse.shElena.toyRec.manager.IManager;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class PaintRecognitionManager {

	private IManager myManager;

	public PaintRecognitionManager(PaintRecognitionTab tab, IManager manager) {
		myManager = manager;
	}

	public ISample addSample(Character symbol, BufferedImage image) {
		return myManager.learnSymbol(symbol, image);
	}

	public String recognize(BufferedImage image) {
		List<ComparisonResult> result = myManager.recognizeImage(image);

		return makeReport(result);
	}

	public void removeSample(ISample sample) {
		myManager.removeSample(sample);
	}

	public void saveTo(File file) {
		if (!file.getName().endsWith(".sb")) {
			file = new File(file.getPath() + ".sb");
		}
		myManager.saveSymbolBase(file);
	}

	public void newSymbolBase() {
		myManager.newSymbolBase();
	}

	public ISample[] addFile(File file) {
		String name = file.getName();

		if (file.isDirectory()) {
			ISampleBase base = myManager.createPictureSymbolBase(file);
			myManager.addSampleBase(base);
			return base.getSamples();
		} else if (name.endsWith(".sb")) {
			ISampleBase base = myManager.createFileSymbolBase(file);
			myManager.addSampleBase(base);
			return base.getSamples();
		} else if (name.endsWith(".bmp")) {
			ISample s = myManager.createSample(file);
			myManager.addSample(s);
			return new ISample[] { s };
		}
		return null;
	}

	private String makeReport(List<ComparisonResult> result) {
		if (result.size() == 0) {
			return "The symbol base is empty. No recognition is available.";
		} else {
			Collections.sort(result);

			StringBuffer s = new StringBuffer();

			s.append("The most similar character in database is "
					+ result.get(0).getSymbol() + ".\n");
			s.append("Total results:\n" + "symbols and their similarity\n");

			for (ComparisonResult cr : result) {
				s.append(cr.getSymbol() + "    " + (100 - cr.getDifference())
						+ "%\n");
			}

			s.append("\n" + myManager.getAlgorithm().toString());

			return s.toString();
		}
	}

	public void setAlgorithm(IAlgorithm algorithm) {
		myManager.setAlgorithm(algorithm);
	}

	public IAlgorithm getAlgorithm() {
		return myManager.getAlgorithm();
	}
}
