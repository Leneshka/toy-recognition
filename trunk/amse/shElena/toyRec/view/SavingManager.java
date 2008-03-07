package org.amse.shElena.toyRec.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.amse.shElena.toyRec.samples.ISample;

public class SavingManager {
	private View myView;

	private int mySavedStateID;

	private File mySource;

	public SavingManager(View view) {
		myView = view;
		mySavedStateID = myView.getManager().getBaseStateID();
	}

	public void saveAs() {
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				return (file.isDirectory()) || (file.getName().endsWith(".sb"));
			}

			@Override
			public String getDescription() {
				return "Symbol base files (.sb)";
			}
		};

		chooser.addChoosableFileFilter(filter);
		chooser.setDialogTitle("Saving...");
		chooser.setVisible(true);
		// chooser.setCurrentDirectory(new File("../"));
		/**
		 * change later
		 */
		chooser.setCurrentDirectory(new File("."));
		chooser.showSaveDialog(myView);

		File f = chooser.getSelectedFile();
		if (f != null) {
			if (!f.getName().endsWith(".sb")) {
				f = new File(f.getPath() + ".sb");
			}

			if (myView.getManager().saveSymbolBase(f)) {
				mySource = f;
				mySavedStateID = myView.getManager().getBaseStateID();
			} else {
				JOptionPane.showMessageDialog(myView,
						"Symbol base wasn`t saved.", "Error",
						JOptionPane.ERROR_MESSAGE);
				// mySource = null;
			}
		}
		// myView.updateSaveAction();
	}

	public void save() {
		if (mySource != null) {
			myView.getManager().saveSymbolBase(mySource);
			mySavedStateID = myView.getManager().getBaseStateID();
		} else {
			saveAs();
		}
		// myView.updateSaveAction();
	}

	public void newSymbolBase() {
		if (mySavedStateID != myView.getManager().getBaseStateID()) {
			int result = JOptionPane.showConfirmDialog(myView, "Save changes?",
					"Symbol recognition", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				save();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}

		myView.getManager().newSymbolBase();
		mySource = null;
		mySavedStateID = myView.getManager().getBaseStateID();
		myView.clear();
		// myView.updateSaveAction();
	}

	public void load() {
		if (mySavedStateID != myView.getManager().getBaseStateID()) {
			int result = JOptionPane.showConfirmDialog(myView, "Save changes?",
					"Character recognition", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				save();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}

		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				return (file.isDirectory()) || (file.getName().endsWith(".sb"));
			}

			@Override
			public String getDescription() {
				return "Symbol base files (.sb)";
			}
		};

		chooser.addChoosableFileFilter(filter);
		chooser.setDialogTitle("Loading...");
		chooser.setVisible(true);
		// chooser.setCurrentDirectory(new File("../"));
		/**
		 * change later
		 */
		chooser.setCurrentDirectory(new File("."));
		chooser.showOpenDialog(myView);

		File file = chooser.getSelectedFile();
		if (file != null) {
			if (myView.getManager().loadSymbolBase(file)) {
				mySource = file;
				mySavedStateID = myView.getManager().getBaseStateID();

				myView.updateListModel();
			} else {
				JOptionPane.showMessageDialog(myView,
						"Symbol base wasn`t loaded.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void exit() {
		if (mySavedStateID != myView.getManager().getBaseStateID()) {
			int result = JOptionPane.showConfirmDialog(myView, "Save changes?",
					"Symbol recognition", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				save();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}

		System.exit(0);
	}

	/**
	 * Creates base from picture files:
	 * from a file or a directory with files
	 * 
	 */
	public void createSymbolBase() {
		newSymbolBase();

		JFileChooser chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setDialogTitle("Choose source...");

		chooser.setCurrentDirectory(new File("."));
		chooser.showDialog(myView, "Create");
		chooser.setVisible(true);

		File f = chooser.getSelectedFile();
		if (f != null) {
			if (!f.isDirectory()) {
				learnFile(f);
				return;
			}

			// f - directory
			File[] files = f.listFiles();

			for (File file : files) {
				learnFile(file);
			}

		}

	}

	/**
	 * Format for symbol A is of  Aanything.
	 * @param filename
	 * @return
	 */
	private Character getSymbol(File file) {
		String filename = file.getName();
		if(filename.length() == 0){
			return null;
		}
		return filename.charAt(0);
	}
	
	private void learnFile(File file){
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			return;
		}
		
		Character sym = getSymbol(file);

		if (sym == null || image == null) {
			return;
		}

		ISample s = myView.getManager().learnSymbol(sym, image);
		myView.getListModel().addElement(s);
		myView.getList().setSelectedIndex(myView.getListModel().getSize() - 1);
	}
}
