package org.amse.shElena.toyRec.view;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;

public class PaintRecognitionManager {
	private PaintRecognitionTab myTab;

	private int mySavedStateID;

	private File mySource;

	private JFileChooser myFileChooser;

	private JFileChooser myDirChooser;

	public PaintRecognitionManager(PaintRecognitionTab tab) {
		myTab = tab;
		mySavedStateID = myTab.getManager().getBaseStateID();

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

	}

	public void saveAs() {
		myFileChooser.setVisible(true);
		int res = myFileChooser.showSaveDialog(myTab);

		if (res == JFileChooser.APPROVE_OPTION) {

			File f = myFileChooser.getSelectedFile();
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

	public void loadFileSymbolBase() {
		if (mySavedStateID != myTab.getManager().getBaseStateID()) {
			int result = JOptionPane.showConfirmDialog(myTab, "Save changes?",
					"Character recognition", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				save();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}

		myFileChooser.setVisible(true);
		int res = myFileChooser.showOpenDialog(myTab);

		if (res == JFileChooser.APPROVE_OPTION) {
			File file = myFileChooser.getSelectedFile();
			if (file != null) {
				try {
					ISampleBase base = myTab.getManager().createFileSymbolBase(
							file);
					myTab.getManager().setSymbolBase(base);
					mySource = file;
					mySavedStateID = myTab.getManager().getBaseStateID();
					myTab.updateListModel();
				} catch (RuntimeException e) {
					JOptionPane.showMessageDialog(myTab, e.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * Creates base from picture files: from a file or a directory with files
	 * 
	 */
	public void loadPictureSymbolBase() {
		newSymbolBase();

		myDirChooser.setVisible(true);
		int res = myDirChooser.showDialog(myTab, "Create");

		if (res == JFileChooser.APPROVE_OPTION) {
			File f = myDirChooser.getSelectedFile();
			ISampleBase base = myTab.getManager().createPictureSymbolBase(f);
			myTab.getManager().setSymbolBase(base);
			mySource = null;
			mySavedStateID = Integer.MIN_VALUE;
			myTab.updateListModel();
		}
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
}
