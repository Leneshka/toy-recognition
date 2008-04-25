package org.amse.shElena.toyRec.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import org.amse.shElena.toyRec.algorithms.ComparisonResult;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class PaintRecognitionManager {
	private PaintRecognitionTab myTab;
	
	/* The base should be asked to save iff a drown symbol
	 * was added.
	 */
	private boolean myIsToSave;

	private File mySource;

	private JFileChooser mySaveFileChooser;

	private JFileChooser myAddFileChooser;

	public PaintRecognitionManager(PaintRecognitionTab tab) {
		myTab = tab;
		myIsToSave = false;
		
		mySaveFileChooser = new JFileChooser();
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				return (file.isDirectory()) || (file.getName().endsWith(".sb"));
			}

			@Override
			public String getDescription() {
				return "Symbol base files (.sb)";
			}
		};

		mySaveFileChooser.addChoosableFileFilter(filter);
		mySaveFileChooser.setDialogTitle("Loading...");
		mySaveFileChooser.setCurrentDirectory(new File("."));

		myAddFileChooser = new JFileChooser();
		FileFilter filt = new FileFilter() {
			public boolean accept(File file) {
				return (file.isDirectory() || file.getName().endsWith(".sb") || file
						.getName().endsWith(".bmp"));
			}

			@Override
			public String getDescription() {
				return "All data sources";
			}
		};

		myAddFileChooser
		.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		myAddFileChooser.addChoosableFileFilter(filt);
		myAddFileChooser.setDialogTitle("Add");
		myAddFileChooser.setMultiSelectionEnabled(true);
		myAddFileChooser.setCurrentDirectory(new File("."));
	}

	public void saveAs() {
		mySaveFileChooser.setVisible(true);
		int res = mySaveFileChooser.showSaveDialog(myTab);

		if (res == JFileChooser.APPROVE_OPTION) {

			File f = mySaveFileChooser.getSelectedFile();
			if (f != null) {
				if (!f.getName().endsWith(".sb")) {
					f = new File(f.getPath() + ".sb");
				}

				try {
					myTab.getManager().saveSymbolBase(f);
					mySource = f;
					myIsToSave = false;
				} catch (RuntimeException e) {
					JOptionPane.showMessageDialog(myTab, e.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public void save() {
		if (mySource != null) {
			myTab.getManager().saveSymbolBase(mySource);
			myIsToSave = false;
		} else {
			saveAs();
		}
	}

	public void newSymbolBase() {
		if (myIsToSave) {
			int result = JOptionPane.showConfirmDialog(myTab, "Save changes?",
					"Symbol recognition", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				save();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}

		myTab.getManager().newSymbolBase();
		mySource = null;
		myIsToSave = false;
		myTab.clear();
	}

	public void exit() {
		if (myIsToSave) {
			int result = JOptionPane.showConfirmDialog(myTab, "Save changes?",
					"Symbol recognition", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				save();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}

		System.exit(0);
	}

	public void addBase() {
		File[] files = chooseFile();

		if (files == null) {
			return;
		}

		for (File f : files) {
			addFile(f);
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

	private void addFile(File file) {
		String name = file.getName();

		if (file.isDirectory()) {
			ISampleBase base = myTab.getManager().createPictureSymbolBase(file);
			myTab.getManager().addSampleBase(base);
			for (ISample s : base.getSamples()) {
				myTab.getListModel().addElement(s);
			}
		} else if (name.endsWith(".sb")) {
			ISampleBase base = myTab.getManager().createFileSymbolBase(file);
			myTab.getManager().addSampleBase(base);
			for (ISample s : base.getSamples()) {
				myTab.getListModel().addElement(s);
			}
		} else if (name.endsWith(".bmp")) {
			ISample s = myTab.getManager().createSample(file);
			myTab.getManager().addSample(s);
			myTab.getListModel().addElement(s);

		}
	}

	public void clearPaints() {
		myTab.getPaintingPanel().clear();
		myTab.getSamplePainter().clear();
		myTab.getSamplePainter().repaint();
	}

	private void paintRedRectangle(BufferedImage image) {
		int right = myTab.getManager().getRightBorder(image);
		int left = myTab.getManager().getLeftBorder(image);
		int upper = myTab.getManager().getUpperBorder(image);
		int lower = myTab.getManager().getLowerBorder(image);
		myTab.getPaintingPanel().paintRedRectangle(right, left, upper, lower);
	}

	public void showSample() {
		BufferedImage image = myTab.getPaintingPanel().getImage();
		ISample sample = myTab.getManager().makeSample(' ', image);

		myTab.getSamplePainter().showSample(sample);

		paintRedRectangle(image);
	}

	public void addSample() {
		BufferedImage image = myTab.getPaintingPanel().getImage();

		String letter = JOptionPane
				.showInputDialog("Please enter a letter you would like to assign this sample to.");

		if (letter == null) {
			return;
		}

		if (letter.length() == 0) {
			JOptionPane.showMessageDialog(myTab, "A letter is necessary.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (letter.length() > 1) {
			JOptionPane.showMessageDialog(myTab,
					"Only a single letter is required.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		ISample s = myTab.getManager().learnSymbol(letter.charAt(0), image);
		myTab.getListModel().addElement(s);
		myTab.getList().setSelectedIndex(myTab.getListModel().getSize() - 1);

		paintRedRectangle(image);
		myIsToSave = true;
	}

	public void recognize() {
		BufferedImage image = myTab.getPaintingPanel().getImage();
		ISample sample = myTab.getManager().makeSample(' ', image);

		myTab.getSamplePainter().showSample(sample);
		
		List<ComparisonResult> result = myTab.getManager().recognizeImage(image);
		
		paintRedRectangle(image);
		
		JTextArea tArea = new JTextArea(20, 10);
		tArea.setText(makeReport(result));
		tArea.setEditable(false);
		tArea.setLineWrap(true);
		tArea.setWrapStyleWord(true);

		JOptionPane.showMessageDialog(myTab,
				new JScrollPane(tArea), "Recognition result",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	private String makeReport(List<ComparisonResult> result){
		if (result.size() == 0){
			return "The symbol base is empty. No recognition is available.";
		} else {
			Collections.sort(result);
			
			StringBuffer s = new StringBuffer();
			
			s.append("The most similar character in database is "+result.get(0).getSymbol()+".\n");
			s.append("Total results:\n" +
					"symbols and their similarity\n");
			
			for (ComparisonResult cr: result){
				s.append(cr.getSymbol() + "    "+ (100 - cr.getDifference())+"%\n");
			}
			
			s.append("\n" + myTab.getManager().getAlgorithm().toString());
			
			return s.toString();
		}
	}
	
	public boolean isBaseToSave(){
		return myIsToSave;
	}
}
