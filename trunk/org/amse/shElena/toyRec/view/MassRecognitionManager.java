package org.amse.shElena.toyRec.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.amse.shElena.toyRec.algorithms.IAlgorithm;
import org.amse.shElena.toyRec.manager.IManager;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class MassRecognitionManager {

	private ISampleBase myRecBase;

	private String myLearnFileName;

	private String myRecFileName;

	private IManager myManager;

	public MassRecognitionManager(IManager manager) {
		myManager = manager;
		myRecBase = myManager.createPictureSymbolBase(null);
	}

	public ISample[] getRecSamples() {
		return myRecBase.getSamples();
	}

	public void newRecBase() {
		myRecBase.clear();
		myRecFileName = null;
	}

	public void newLearnBase() {
		myManager.newSymbolBase();
		myLearnFileName = null;
	}

	public ISample[] addLearnFile(File file) {
		String name = file.getName();
		ISample[] samples = null;
		if (file.isDirectory()) {
			ISampleBase base = myManager.createPictureSymbolBase(file);
			myManager.addSampleBase(base);
			samples = base.getSamples();
		} else if (name.endsWith(".sb")) {
			ISampleBase base = myManager.createFileSymbolBase(file);
			myManager.addSampleBase(base);
			samples = base.getSamples();
		} else if (name.endsWith(".bmp")) {
			ISample s = myManager.createSample(file);
			myManager.addSample(s);
			samples = new ISample[] { s };
		}

		if (myLearnFileName == null) {
			myLearnFileName = name;
		} else {
			myLearnFileName = myLearnFileName + ", " + name;
		}
		return samples;
	}

	public ISample[] addRecFile(File file) {
		String name = file.getName();
		ISample[] samples = null;
		if (file.isDirectory()) {
			ISampleBase base = myManager.createPictureSymbolBase(file);
			myRecBase.addSampleBase(base);
			samples = base.getSamples();
		} else if (name.endsWith(".sb")) {
			ISampleBase base = myManager.createFileSymbolBase(file);
			myRecBase.addSampleBase(base);
			samples = base.getSamples();
		} else if (name.endsWith(".bmp")) {
			ISample s = myManager.createSample(file);
			myRecBase.addSample(s);
			samples = new ISample[] { s };
		}

		if (myRecFileName == null) {
			myRecFileName = name;
		} else {
			myRecFileName = myRecFileName + ", " + name;
		}
		return samples;
	}

	public String testRecognition(IAlgorithm[] algorithms) {
		String report;
		if (myRecBase.size() == 0) {
			report = "No statistics available: base for recognition is empty.";
		} else if (algorithms.length == 0) {
			report = "No statistics available: no algorithms to test.";
		} else {
			List<Character> recList = new ArrayList<Character>();
			List<Character> unrecList = new ArrayList<Character>();

			Map<IAlgorithm, Integer> res = new HashMap<IAlgorithm, Integer>();

			int rec;
			int unrec;
			for (IAlgorithm alg : algorithms) {
				recList.clear();
				unrecList.clear();
				myManager.setAlgorithm(alg);
				myManager.testRecognition(myRecBase, recList, unrecList);
				rec = recList.size();
				unrec = unrecList.size();
				int percent = (int) (((double) 100 * rec) / (rec + unrec));
				res.put(alg, percent);
			}
			myManager.testRecognition(myRecBase, recList, unrecList);

			report = makeReport(res);
		}

		return report;
	}

	private String makeReport(Map<IAlgorithm, Integer> res) {
		StringBuffer sb = new StringBuffer();
		sb.append("The best result:\n ");

		int max = Collections.max(res.values());

		for (Map.Entry<IAlgorithm, Integer> entry : res.entrySet()) {
			if (entry.getValue() == max) {
				sb.append(entry.getKey() + "\n");
			}
		}

		sb.append("\nAll results\n");

		for (Map.Entry<IAlgorithm, Integer> entry : res.entrySet()) {
			sb.append(entry.getKey() + "   " + entry.getValue() + "\n");
		}

		sb.append("\nRecognized file  " + myRecFileName + "\n");
		sb.append("Learned file  " + myLearnFileName + "\n");
		return sb.toString();
	}

}
