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
import javax.swing.filechooser.FileFilter;

import org.amse.shElena.toyRec.algorithms.IAlgorithm;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

/*
 * Never updates list model
 */
public class MassRecognitionManager {

	private MassRecognitionTab myTab;

	private ISampleBase myRecBase;

	private JFileChooser myFileChooser;

	private JFileChooser myDirChooser;

	private JFileChooser mySaveReportChooser;

	private File myLearnFile;

	private File myRecFile;

	public MassRecognitionManager(MassRecognitionTab tab) {
		myTab = tab;
		myRecBase = tab.getManager().createPictureSymbolBase(new File("Vasya"));

		myFileChooser = new JFileChooser();
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				return (file.isDirectory()) || (file.getName().endsWith(".sb"));
			}

			@Override
			public String getDescription() {
				return "Symbol base files (.sb)";
			}
		};

		myFileChooser.addChoosableFileFilter(filter);
		myFileChooser.setDialogTitle("Loading...");
		myFileChooser.setCurrentDirectory(new File("."));

		myDirChooser = new JFileChooser();
		myDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		myDirChooser.setDialogTitle("Choose source...");
		myDirChooser.setCurrentDirectory(new File("."));

		mySaveReportChooser = new JFileChooser();
		mySaveReportChooser
				.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		mySaveReportChooser.setDialogTitle("Save");
		mySaveReportChooser.setCurrentDirectory(new File("."));

	}

	public ISample[] getRecSamples() {
		return myRecBase.getSamples();
	}

	public void newRecBase() {
		myRecBase.clear();
		myRecFile = null;
	}

	public void newLearnBase() {
		myTab.getManager().newSymbolBase();
		myLearnFile = null;
	}


	public void loadLearnFileBase() {
		myFileChooser.setVisible(true);
		int res = myFileChooser.showOpenDialog(myTab);

		if (res == JFileChooser.APPROVE_OPTION) {
			File file = myFileChooser.getSelectedFile();
			if (file != null) {
				try {
					ISampleBase base = myTab.getManager().createFileSymbolBase(
							file);
					myTab.getManager().setSymbolBase(base);
					myLearnFile = file;
				} catch (RuntimeException e) {
					JOptionPane.showMessageDialog(myTab, e.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}


	public void loadRecFileBase() {
		myFileChooser.setVisible(true);
		int res = myFileChooser.showOpenDialog(myTab);

		if (res == JFileChooser.APPROVE_OPTION) {
			File file = myFileChooser.getSelectedFile();
			if (file != null) {
				try {
					ISampleBase base = myTab.getManager().createFileSymbolBase(
							file);
					myRecBase = base;
					myRecFile = file;
				} catch (RuntimeException e) {
					JOptionPane.showMessageDialog(myTab, e.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}


	public void loadLearnPictureBase() {
		myDirChooser.setVisible(true);
		int res = myDirChooser.showDialog(myTab, "Create");

		if (res == JFileChooser.APPROVE_OPTION) {
			File f = myDirChooser.getSelectedFile();
			if (f != null && f.isDirectory()) {
				ISampleBase base = myTab.getManager()
						.createPictureSymbolBase(f);
				myTab.getManager().setSymbolBase(base);
				myLearnFile = f;
			}
		}
	}


	public void loadRecPictureBase() {
		myDirChooser.setVisible(true);
		int res = myDirChooser.showDialog(myTab, "Create");

		if (res == JFileChooser.APPROVE_OPTION) {
			File f = myDirChooser.getSelectedFile();
			if (f != null && f.isDirectory()) {
				ISampleBase base = myTab.getManager()
						.createPictureSymbolBase(f);
				myRecBase = base;
				myRecFile = f;
			}
		}
	}

	public void testRecognition(IAlgorithm[] algorithms) {
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
				myTab.getManager().setAlgorithm(alg);
				myTab.getManager().testRecognition(myRecBase, recList,
						unrecList);
				rec = recList.size();
				unrec = unrecList.size();
				int percent = (int) (((double) 100 * rec) / (rec + unrec));
				res.put(alg, percent);
			}
			myTab.getManager().testRecognition(myRecBase, recList, unrecList);

			report = makeReport(res, myTab);
		}

		Object[] options = { "OK", "Save report...", "Close" };
		int opt = JOptionPane.showOptionDialog(null, report, "Result",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, options, options[0]);

		if (opt == 1) {
			save(report, myTab);
		}
	}

	private String makeReport(Map<IAlgorithm, Integer> res, Tab myView) {
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

		sb.append("\nRecognized file  " + myRecFile.getName() + "\n");
		sb.append("Learned file  " + myLearnFile.getName() + "\n");
		return sb.toString();
	}

	private void save(String report, Tab myView) {
		mySaveReportChooser.showDialog(myView, "Save");
		mySaveReportChooser.setVisible(true);

		File f = mySaveReportChooser.getSelectedFile();
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
				JOptionPane.showMessageDialog(myView,
						"The report wasn`t saved.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
