package org.amse.shElena.toyRec.view;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import org.amse.shElena.toyRec.algorithms.Algorithm;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class MassRecognitionManager {

	private MassRecognitionTab myTab;

	private ISampleBase myRecBase;

	private JFileChooser myAddFileChooser;

	private JFileChooser mySaveReportChooser;

	private String myLearnFileName;

	private String myRecFileName;

	public MassRecognitionManager(MassRecognitionTab tab) {
		myTab = tab;
		myRecBase = tab.getManager().createPictureSymbolBase(new File("Vasya"));

		myAddFileChooser = new JFileChooser();
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				return (file.isDirectory() || file.getName().endsWith(".sb") || file
						.getName().endsWith(".bmp"));
			}

			@Override
			public String getDescription() {
				return "All data sources";
			}
		};

		myAddFileChooser.addChoosableFileFilter(filter);
		myAddFileChooser
		.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		myAddFileChooser.setDialogTitle("Add");
		myAddFileChooser.setMultiSelectionEnabled(true);
		myAddFileChooser.setCurrentDirectory(new File("."));

		mySaveReportChooser = new JFileChooser();
		//mySaveReportChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		mySaveReportChooser.setDialogTitle("Save");
		mySaveReportChooser.setCurrentDirectory(new File("."));

	}

	public ISample[] getRecSamples() {
		return myRecBase.getSamples();
	}

	public void newRecBase() {
		myRecBase.clear();
		myRecFileName = null;
		myTab.getRecognizeListModel().clear();
	}

	public void newLearnBase() {
		myTab.getManager().newSymbolBase();
		myLearnFileName = null;
		myTab.getLearnListModel().clear();
	}

	public void addLearnBase() {
		File[] files = chooseFile();

		if (files == null) {
			return;
		}

		for (File f : files) {
			addLearnFile(f);
		}
	}

	public void addRecBase() {
		File[] files = chooseFile();

		if (files == null) {
			return;
		}

		for (File f : files) {
			addRecFile(f);
		}
	}

	private File[] chooseFile() {
		myAddFileChooser.setVisible(true);
		int res = myAddFileChooser.showOpenDialog(myTab);

		if (res != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		return myAddFileChooser.getSelectedFiles();
	}

	private void addLearnFile(File file) {
		String name = file.getName();

		if (file.isDirectory()) {
			ISampleBase base = myTab.getManager().createPictureSymbolBase(file);
			myTab.getManager().addSampleBase(base);
			for (ISample s : base.getSamples()) {
				myTab.getLearnListModel().addElement(s);
			}
		} else if (name.endsWith(".sb")) {
			ISampleBase base = myTab.getManager().createFileSymbolBase(file);
			myTab.getManager().addSampleBase(base);
			for (ISample s : base.getSamples()) {
				myTab.getLearnListModel().addElement(s);
			}
		} else if (name.endsWith(".bmp")) {
			ISample s = myTab.getManager().createSample(file);
			myTab.getManager().addSample(s);
			myTab.getLearnListModel().addElement(s);
		}

		if (myLearnFileName == null) {
			myLearnFileName = name;
		} else {
			myLearnFileName = myLearnFileName + ", " + name;
		}
	}

	private void addRecFile(File file) {
		String name = file.getName();

		if (file.isDirectory()) {
			ISampleBase base = myTab.getManager().createPictureSymbolBase(file);
			myRecBase.addSampleBase(base);
			for (ISample s : base.getSamples()) {
				myTab.getRecognizeListModel().addElement(s);
			}
		} else if (name.endsWith(".sb")) {
			ISampleBase base = myTab.getManager().createFileSymbolBase(file);
			myRecBase.addSampleBase(base);
			for (ISample s : base.getSamples()) {
				myTab.getRecognizeListModel().addElement(s);
			}
		} else if (name.endsWith(".bmp")) {
			ISample s = myTab.getManager().createSample(file);
			myRecBase.addSample(s);
			myTab.getRecognizeListModel().addElement(s);

		}

		if (myRecFileName == null) {
			myRecFileName = name;
		} else {
			myRecFileName = myRecFileName + ", " + name;
		}
	}

	public void testRecognition(Algorithm[] algorithms) {
		String report;
		if (myRecBase.size() == 0) {
			report = "No statistics available: base for recognition is empty.";
		} else if (algorithms.length == 0) {
			report = "No statistics available: no algorithms to test.";
		} else {
			List<Character> recList = new ArrayList<Character>();
			List<Character> unrecList = new ArrayList<Character>();

			Map<Algorithm, Integer> res = new HashMap<Algorithm, Integer>();

			int rec;
			int unrec;
			for (Algorithm alg : algorithms) {
				recList.clear();
				unrecList.clear();
				myTab.getManager().setAlgorithm(alg);
				myTab.getManager().testRecognition(myRecBase, recList,
						unrecList);
				rec = recList.size();
				unrec = unrecList.size();
				int percent = (int) (((double) 100 * rec) / (rec + unrec));
				res.put(alg, percent);
			}
			myTab.getManager().testRecognition(myRecBase, recList, unrecList);

			report = makeReport(res);
		}

		Object[] options = { "OK", "Save report...", "Close" };

		JTextArea tArea = new JTextArea(20, 10);
		tArea.setText(report);
		tArea.setEditable(false);
		tArea.setLineWrap(true);
		tArea.setWrapStyleWord(true);

		int opt = JOptionPane.showOptionDialog(null, new JScrollPane(tArea),
				"Result", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (opt == 1) {
			saveReport(report);
		}
	}

	private String makeReport(Map<Algorithm, Integer> res) {
		StringBuffer sb = new StringBuffer();
		sb.append("The best result:\n ");

		int max = Collections.max(res.values());

		for (Map.Entry<Algorithm, Integer> entry : res.entrySet()) {
			if (entry.getValue() == max) {
				sb.append(entry.getKey() + "\n");
			}
		}

		sb.append("\nAll results\n");

		for (Map.Entry<Algorithm, Integer> entry : res.entrySet()) {
			sb.append(entry.getKey() + "   " + entry.getValue() + "\n");
		}

		sb.append("\nRecognized file  " + myRecFileName + "\n");
		sb.append("Learned file  " + myLearnFileName + "\n");
		return sb.toString();
	}

	private void saveReport(String report) {
		mySaveReportChooser.showDialog(myTab, "Save");
		mySaveReportChooser.setVisible(true);

		File f = mySaveReportChooser.getSelectedFile();
		if (!f.exists() && !f.getName().endsWith(".txt")) {
			f = new File(f.getAbsolutePath() + ".txt");
		}
		if (f != null) {
			Writer wr;
			try {
				if (f.isFile()) {
					Reader read = new FileReader(f);
					StringBuffer sb = new StringBuffer();

					int c;
					while ((c = read.read()) != -1) {
						sb.append((char) c);
					}
					read.close();

					wr = new FileWriter(f);
					wr.write(sb.toString() + " " + '\n' + report);
				} else {
					wr = new FileWriter(f);
					wr.write(report);
				}
				wr.close();
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(myTab,
						"The report wasn`t saved.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
