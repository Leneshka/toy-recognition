package org.amse.shElena.toyRec.view;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;

import org.amse.shElena.toyRec.algorithms.AlgorithmProvider;
import org.amse.shElena.toyRec.algorithms.IAlgorithm;
import org.amse.shElena.toyRec.manager.IManager;
import org.amse.shElena.toyRec.samples.ISample;

public class MassRecognitionTab extends JPanel {
	private static final long serialVersionUID = 1L;

	private IManager myManager;

	private MassRecognitionManager myMassRecognitionManager;

	private DefaultListModel myLearnListModel;

	private DefaultListModel myRecListModel;

	private DefaultListModel myAlgorithmListModel;

	private JFileChooser myAddFileChooser;

	private JFileChooser mySaveReportChooser;

	private final int MY_WIDTH = 100;

	public MassRecognitionTab(IManager manager) {
		myManager = manager;

		myMassRecognitionManager = new MassRecognitionManager(manager);

		JPanel learn = createLearnPanel();
		View.setComponentSize(learn, MY_WIDTH, 720);
		add(learn);

		JPanel rec = createRecognizePanel();
		View.setComponentSize(rec, MY_WIDTH, 720);
		add(rec);

		JPanel alg = createAlgorithmPanel();
		View.setComponentSize(alg, 2 * MY_WIDTH, 720);
		add(alg);

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
		mySaveReportChooser.setDialogTitle("Save");
		mySaveReportChooser.setCurrentDirectory(new File("."));

	}

	public IManager getManager() {
		return myManager;
	}

	private JPanel createLearnPanel() {
		JPanel panel = new JPanel();
		JLabel lab = new JLabel("Learn base");
		View.setComponentSize(lab, MY_WIDTH, 15);
		panel.add(lab);
		JScrollPane scroll = new JScrollPane(createLearnList());

		View.setComponentSize(scroll, MY_WIDTH, 260);
		panel.add(scroll);

		panel.add(createLearnButtons());

		return panel;
	}

	private JPanel createLearnButtons() {
		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JButton newBase = new JButton(getNewLearnBaseAction());
		panel.add(newBase);

		JButton add = new JButton(getAddLearnBaseAction());
		panel.add(add);

		View.setComponentSize(add, MY_WIDTH / 2, 25);
		View.setComponentSize(newBase, MY_WIDTH / 2, 25);

		return panel;
	}

	private JPanel createRecButtons() {
		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JButton newBase = new JButton(getNewRecBaseAction());
		panel.add(newBase);

		JButton add = new JButton(getAddRecBaseAction());
		panel.add(add);

		View.setComponentSize(add, MY_WIDTH / 2, 25);
		View.setComponentSize(newBase, MY_WIDTH / 2, 25);

		return panel;
	}

	private AbstractAction getAddLearnBaseAction() {
		AbstractAction addBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				File[] files = chooseFile();

				if (files == null) {
					return;
				}
				ISample[] samples;
				for (File f : files) {
					samples = myMassRecognitionManager.addLearnFile(f);
					for (ISample s : samples) {
						myLearnListModel.addElement(s);
					}
				}
			}

		};
		// addBase.putValue(AbstractAction.NAME, "Add base");
		addBase.putValue(AbstractAction.SHORT_DESCRIPTION, "Add symbol base");
		View.setIcon("Add.png", addBase);
		return addBase;
	}

	private File[] chooseFile() {
		myAddFileChooser.setVisible(true);
		int res = myAddFileChooser.showOpenDialog(this);

		if (res != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		return myAddFileChooser.getSelectedFiles();
	}

	private AbstractAction getAddRecBaseAction() {
		AbstractAction addBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				File[] files = chooseFile();

				if (files == null) {
					return;
				}
				ISample[] samples;
				for (File f : files) {
					samples = myMassRecognitionManager.addRecFile(f);
					for (ISample s : samples) {
						myRecListModel.addElement(s);
					}
				}
			}

		};
		// addBase.putValue(AbstractAction.NAME, "Add base");
		addBase.putValue(AbstractAction.SHORT_DESCRIPTION, "Add symbol base");
		View.setIcon("Add.png", addBase);
		return addBase;
	}

	private AbstractAction getNewLearnBaseAction() {
		AbstractAction newBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myMassRecognitionManager.newLearnBase();
				myLearnListModel.clear();
			}
		};
		// newBase.putValue(AbstractAction.NAME, "New");
		newBase.putValue(AbstractAction.SHORT_DESCRIPTION, "New symbol base");
		View.setIcon("New.png", newBase);
		return newBase;
	}

	private AbstractAction getNewRecBaseAction() {
		AbstractAction newBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myMassRecognitionManager.newRecBase();
				myRecListModel.clear();
			}
		};
		// newBase.putValue(AbstractAction.NAME, "New");
		newBase.putValue(AbstractAction.SHORT_DESCRIPTION, "New symbol base");
		View.setIcon("New.png", newBase);
		return newBase;
	}

	private JPanel createRecognizePanel() {
		JPanel panel = new JPanel();
		JLabel lab = new JLabel("Recognition base");
		View.setComponentSize(lab, MY_WIDTH, 15);
		panel.add(lab);
		JScrollPane scroll = new JScrollPane(createRecList());

		View.setComponentSize(scroll, MY_WIDTH, 260);
		panel.add(scroll);

		panel.add(createRecButtons());

		return panel;
	}

	private JPanel createAlgorithmPanel() {
		JPanel panel = new JPanel();

		JLabel lab = new JLabel("Tested algorithms");
		View.setComponentSize(lab, 2 * MY_WIDTH, 15);
		panel.add(lab);

		JScrollPane scroll = new JScrollPane(createAlgList());

		View.setComponentSize(scroll, 2 * MY_WIDTH, 210);
		panel.add(scroll);

		JPanel con = createAlgorithmsControl();
		View.setComponentSize(con, 2 * MY_WIDTH, 45);
		panel.add(con);

		JButton rec = new JButton(getTestRecognitionAction());
		rec.setText("Test recognition");
		panel.add(rec);
		View.setComponentSize(rec, 2 * MY_WIDTH, 25);

		return panel;
	}

	private JPanel createAlgorithmsControl() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		final JComboBox box = new JComboBox(AlgorithmProvider.getAlgorithms());

		AbstractAction addAlg = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				Object alg = box.getSelectedItem();
				if (!myAlgorithmListModel.contains(alg)) {
					myAlgorithmListModel.addElement(alg);
				}
			}

		};

		addAlg.putValue(AbstractAction.NAME, "+");
		addAlg.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Add algorithm to tested ones");

		JPanel butPanel = new JPanel();
		butPanel.setLayout(new BoxLayout(butPanel, BoxLayout.X_AXIS));
		JButton add = new JButton(addAlg);
		butPanel.add(add);

		AbstractAction removeAlg = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				Object alg = box.getSelectedItem();
				myAlgorithmListModel.removeElement(alg);
			}

		};

		removeAlg.putValue(AbstractAction.NAME, "-");
		removeAlg.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Remove algorithm from tested ones");
		JButton remove = new JButton(removeAlg);
		butPanel.add(remove);

		View.setComponentSize(add, MY_WIDTH, 20);
		View.setComponentSize(remove, MY_WIDTH, 20);
		View.setComponentSize(butPanel, MY_WIDTH * 2 + 1, 20);

		panel.add(box);
		panel.add(butPanel);

		return panel;
	}

	private AbstractAction getTestRecognitionAction() {
		return new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				IAlgorithm[] ar = new IAlgorithm[myAlgorithmListModel.size()];
				int i = 0;
				for (Object alg : myAlgorithmListModel.toArray()) {
					ar[i] = (IAlgorithm) alg;
					i++;
				}
				String report = myMassRecognitionManager.testRecognition(ar);

				Object[] options = { "OK", "Save report...", "Close" };

				JTextArea tArea = new JTextArea(20, 10);
				tArea.setText(report);
				tArea.setEditable(false);
				tArea.setLineWrap(true);
				tArea.setWrapStyleWord(true);

				int opt = JOptionPane.showOptionDialog(MassRecognitionTab.this,
						new JScrollPane(tArea), "Result",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
						null, options, options[0]);

				if (opt == 1) {
					saveReport(report);
				}
			}
		};
	}

	private void saveReport(String report) {
		mySaveReportChooser.showDialog(this, "Save");
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
				JOptionPane.showMessageDialog(this, "The report wasn`t saved.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private JList createLearnList() {
		myLearnListModel = new DefaultListModel();
		JList list = new JList(myLearnListModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		return list;
	}

	private JList createRecList() {
		myRecListModel = new DefaultListModel();
		JList list = new JList(myRecListModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		return list;
	}

	private JList createAlgList() {
		myAlgorithmListModel = new DefaultListModel();
		JList list = new JList(myAlgorithmListModel);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		return list;
	}

	public DefaultListModel getLearnListModel() {
		return myLearnListModel;
	}

	public DefaultListModel getRecognizeListModel() {
		return myRecListModel;
	}
}
