package org.amse.shElena.toyRec.view;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;

public class TestRecognition {
	private static final long serialVersionUID = 1L;

	public static void testRecognition(ISampleBase recBase, Tab tab,
			String filename) {
		List<Character> recList = new ArrayList<Character>();
		List<Character> unrecList = new ArrayList<Character>();
	
		tab.getManager().testRecognition(recBase, recList, unrecList);
	
		String report = makeReport(filename, recList, unrecList, tab);
	
		Object[] options = { "OK", "Save report...", "Close" };
		int opt = JOptionPane.showOptionDialog(null, report, "Result",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, options, options[0]);
	
		if (opt == 1) {
			save(report, tab);
		}
	}

	private static String makeReport(String filename, List<Character> recList,
			List<Character> unrecList, Tab myView) {
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

			sb.append("\n" + myView.getManager().getAlgorithm().toString());
			return sb.toString();
		}
	}

	private static void save(String report, Tab myView) {
		JFileChooser chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setDialogTitle("Save");

		chooser.setCurrentDirectory(new File("."));
		chooser.showDialog(myView, "Save");
		chooser.setVisible(true);

		File f = chooser.getSelectedFile();
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
				JOptionPane.showMessageDialog(myView, "The report wasn`t saved.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

}
