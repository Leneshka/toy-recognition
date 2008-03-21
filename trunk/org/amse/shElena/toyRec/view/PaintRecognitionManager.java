package org.amse.shElena.toyRec.view;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class PaintRecognitionManager {
	private PaintRecognitionTab myTab;

	private int mySavedStateID;

	private File mySource;

	private JFileChooser mySaveFileChooser;
	
	private JFileChooser myAddFileChooser;

	public PaintRecognitionManager(PaintRecognitionTab tab) {
		myTab = tab;
		mySavedStateID = myTab.getManager().getBaseStateID();

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
					mySavedStateID = myTab.getManager().getBaseStateID();
				} catch (RuntimeException e) {
					JOptionPane.showMessageDialog(myTab, e.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
					// mySource = null;
				}
			}
		}
		// myView.updateSaveAction();
	}

	public void save() {
		if (mySource != null) {
			myTab.getManager().saveSymbolBase(mySource);
			mySavedStateID = myTab.getManager().getBaseStateID();
		} else {
			saveAs();
		}
		// myView.updateSaveAction();
	}

	public void newSymbolBase() {
		if (mySavedStateID != myTab.getManager().getBaseStateID()) {
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
		mySavedStateID = myTab.getManager().getBaseStateID();
		myTab.clear();
		// myView.updateSaveAction();
	}

	
	
	public void exit() {
		if (mySavedStateID != myTab.getManager().getBaseStateID()) {
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

		if (name.endsWith(".sb")) {
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
}
