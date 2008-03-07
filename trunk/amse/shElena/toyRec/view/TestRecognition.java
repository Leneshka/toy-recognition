package org.amse.shElena.toyRec.view;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;

public class TestRecognition {
	private static final long serialVersionUID = 1L;

	private static TestRecognitionXMLAction myTestRecognitionXMLAction;

	private static TestRecognitionPictureAction myTestRecognitionPictureAction;

	public static AbstractAction getTestRecognitionPictureAction(View view) {
		if (myTestRecognitionPictureAction == null) {
			myTestRecognitionPictureAction = new TestRecognitionPictureAction();
		}
		myTestRecognitionPictureAction.setView(view);
		return myTestRecognitionPictureAction;
	}

	
	public static AbstractAction getTestRecognitionXMLAction(View view) {
		if (myTestRecognitionXMLAction == null) {
			myTestRecognitionXMLAction = new TestRecognitionXMLAction();
		}
		myTestRecognitionXMLAction.setView(view);
		return myTestRecognitionXMLAction;
	}

	private static String makeReport(String filename, List<Character> recList,
			List<Character> unrecList, View view) {
		int rec = recList.size();
		int unrec = unrecList.size();
		if (rec == 0 && unrec == 0) {
			return ("Test is impossible: empty base or empty test directory.");
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("The percent of proper recognized pictures in ");
			sb.append(filename + " is ");
			sb.append((int) (((double) 100 * rec) / (rec + unrec)));
			if (rec != 0) {
				sb.append(".\n The recognized symbols are:\n ");
				for (Character c : recList) {
					sb.append(c + "   ");
				}
			}
			if (unrec != 0) {
				sb.append("\n The unrecognized symbols are:\n ");
				for (Character c : unrecList) {
					sb.append(c + "   ");
				}
			}

			sb.append("\n" + view.getManager().getAlgorithm().getName());
			return sb.toString();
		}
	}

	private static void save(String report, View view) {
		JFileChooser chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setDialogTitle("Save");

		chooser.setCurrentDirectory(new File("."));
		chooser.showDialog(view, "Save");
		chooser.setVisible(true);

		File f = chooser.getSelectedFile();
		if (f != null) {
			try {
				Reader read = new FileReader(f);
				StringBuffer sb = new StringBuffer();

				int c;
				while ((c = read.read()) != -1) {
					sb.append((char) c);
				}
				read.close();
				Writer wr = new FileWriter(f);
				wr.write(sb.toString() + " " + '\n' + report);
				wr.close();
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(view, "The report wasn`t saved.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	private static File choosePictureFile(View view) {
		JFileChooser chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setDialogTitle("Choose source...");

		chooser.setCurrentDirectory(new File("."));
		chooser.showDialog(view, "Recognize");
		chooser.setVisible(true);

		return chooser.getSelectedFile();
	}

	private static File chooseSymbolBaseFile(View view) {
		JFileChooser chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setDialogTitle("Choose source...");
		
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
		chooser.setCurrentDirectory(new File("."));
		chooser.showDialog(view, "Recognize");
		chooser.setVisible(true);

		return chooser.getSelectedFile();
	}

	
	private static void reportRecognition(ISampleBase test, View view, String filename) {
		List<Character> recList = new ArrayList<Character>();
		List<Character> unrecList = new ArrayList<Character>();

		view.getManager().testRecognition(test, recList, unrecList);

		String report = makeReport(filename, recList, unrecList, view);

		Object[] options = { "OK", "Save report...", "Close" };
		int opt = JOptionPane.showOptionDialog(null, report, "Result",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, options, options[0]);

		if (opt == 1) {
			save(report, view);
		}
	}

	private static class TestRecognitionPictureAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		private View myView;

		public TestRecognitionPictureAction() {
			putValue(AbstractAction.NAME, "On pictures");
			// putValue(SHORT_DESCRIPTION, "Tests recognition on a test
			// directory");
		}

		public void setView(View view) {
			myView = view;
		}

		public void actionPerformed(ActionEvent arg0) {
			File f = choosePictureFile(myView);

			ISampleBase test = myView.getManager().createPictureSymbolBase(f);

			reportRecognition(test, myView, f.getName());
		}

	}

	private static class TestRecognitionXMLAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		private View myView;

		public TestRecognitionXMLAction() {
			putValue(AbstractAction.NAME, "On symbol base file");
			// putValue(SHORT_DESCRIPTION, "Tests recognition on a test
			// directory");
		}

		public void setView(View view) {
			myView = view;
		}

		public void actionPerformed(ActionEvent arg0) {
			File f = chooseSymbolBaseFile(myView);

			ISampleBase test = myView.getManager().createXMLSymbolBase(f);

			reportRecognition(test, myView, f.getName());
		}

	}
}
