package org.amse.shElena.toyRec.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import org.amse.shElena.toyRec.algorithms.AlgorithmProvider;
import org.amse.shElena.toyRec.algorithms.IAlgorithm;
import org.amse.shElena.toyRec.manager.IManager;
import org.amse.shElena.toyRec.samples.ISample;
import org.amse.shElena.toyRec.samples.Sampler;

public class PaintRecognitionTab extends JPanel {
	private static final long serialVersionUID = 1L;

	private PaintingPanel myPaintingPanel;

	private DefaultListModel myListModel;

	private JList myList;

	private AbstractAction mySaveAction;

	private PaintRecognitionManager myPaintRecognitionManager;

	/*
	 * The base should be asked to save iff a drawn symbol was added.
	 */
	private boolean myIsToSave;

	private File mySource;

	private JFileChooser mySaveFileChooser;

	private JFileChooser myAddFileChooser;

	public PaintRecognitionTab(IManager manager) {
		myPaintRecognitionManager = new PaintRecognitionManager(this, manager);

		JPanel listPan = createListPanel();
		View.setComponentSize(listPan, 200, 720);
		JPanel paintPan = createPaintPanel();
		View.setComponentSize(paintPan, 200, 720);
		add(listPan);
		add(paintPan);

		setSize(450, 350);
		View.setComponentSize(this, 450, 350);

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
		mySaveFileChooser.setDialogTitle("Save");
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

	private JPanel createPaintPanel() {
		JPanel panel = new JPanel();

		JLabel lab = new JLabel("Painting panel");
		View.setComponentSize(lab, 200, 15);
		panel.add(lab);

		myPaintingPanel = new PaintingPanel();
		myPaintingPanel.setPreferredSize(new Dimension(200, 220));
		myPaintingPanel.setBorder(new LineBorder(Color.gray, 1));

		panel.add(myPaintingPanel);

		JPanel buttons = createPaintButtonPanel();
		buttons.setPreferredSize(new Dimension(200, 40));
		panel.add(buttons);

		JPanel sample = new JPanel();
		sample.setLayout(new BoxLayout(sample, BoxLayout.Y_AXIS));

		panel.setPreferredSize(new Dimension(200, 270));
		return panel;
	}

	private JPanel createPaintButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel upPan = new JPanel();
		upPan.setLayout(new BoxLayout(upPan, BoxLayout.X_AXIS));

		JPanel lowPan = new JPanel();
		lowPan.setLayout(new BoxLayout(lowPan, BoxLayout.X_AXIS));

		JButton add = new JButton(getAddSampleAction());
		upPan.add(add);

		JButton rec = new JButton(getRecognizeAction());
		lowPan.add(rec);

		JButton clear = new JButton(getClearAction());
		upPan.add(clear);

		panel.add(upPan);
		panel.add(lowPan);

		View.setComponentSize(add, 100, 20);
		View.setComponentSize(rec, 200, 20);
		View.setComponentSize(clear, 100, 20);

		return panel;
	}

	private AbstractAction getClearAction() {
		AbstractAction clear = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myPaintingPanel.clear();
			}

		};
		clear.putValue(AbstractAction.NAME, "Clear");
		clear.putValue(AbstractAction.SHORT_DESCRIPTION, "Clear painting area");
		return clear;
	}

	private AbstractAction getAddSampleAction() {
		AbstractAction add = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				addSample();
				updateSaveAction();
			}

		};
		add.putValue(AbstractAction.NAME, "Add");
		add.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Add a symbol to the base");

		return add;
	}

	private void addSample() {
		BufferedImage image = myPaintingPanel.getImage();

		String letter = JOptionPane
				.showInputDialog("Please enter a letter you would like to assign this sample to.");

		if (letter == null) {
			return;
		}

		if (letter.length() == 0) {
			JOptionPane.showMessageDialog(this, "A letter is necessary.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (letter.length() > 1) {
			JOptionPane.showMessageDialog(this,
					"Only a single letter is required.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		ISample s = myPaintRecognitionManager
				.addSample(letter.charAt(0), image);
		myListModel.addElement(s);
		myList.setSelectedIndex(myListModel.getSize() - 1);

		paintRedRectangle(image);
		myIsToSave = true;
	}

	private void paintRedRectangle(BufferedImage image) {
		int right = Sampler.getRightBorder(image);
		int left = Sampler.getLeftBorder(image);
		int upper = Sampler.getUpperBorder(image);
		int lower = Sampler.getLowerBorder(image);
		myPaintingPanel.paintRedRectangle(right, left, upper, lower);
	}

	private AbstractAction getRecognizeAction() {
		AbstractAction recognize = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				recognize();
			}

		};
		recognize.putValue(AbstractAction.NAME, "Recognize");
		recognize
				.putValue(AbstractAction.SHORT_DESCRIPTION, "Recognize symbol");

		return recognize;
	}

	private void recognize() {
		BufferedImage image = myPaintingPanel.getImage();
		String report = myPaintRecognitionManager.recognize(image);

		JTextArea tArea = new JTextArea(20, 10);
		tArea.setText(report);
		tArea.setEditable(false);
		tArea.setLineWrap(true);
		tArea.setWrapStyleWord(true);

		JOptionPane.showMessageDialog(this, new JScrollPane(tArea),
				"Recognition result", JOptionPane.PLAIN_MESSAGE);
	}

	private JPanel createListPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 400));

		JLabel lab = new JLabel("Learning base");
		View.setComponentSize(lab, 200, 15);
		panel.add(lab);

		myList = createList();
		JScrollPane scroll = new JScrollPane(myList);
		scroll.setPreferredSize(new Dimension(200, 220));

		panel.add(scroll);

		JPanel buttons = createListButtonPanel();
		View.setComponentSize(buttons, 200, 40);
		panel.add(buttons);

		JComboBox algs = getAlgComboBox();
		View.setComponentSize(algs, 200, 20);
		panel.add(algs);

		return panel;
	}

	private JComboBox getAlgComboBox() {
		JComboBox box = new JComboBox(AlgorithmProvider.getAlgorithms());

		ItemListener listener = new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				if (ev.getStateChange() == ItemEvent.SELECTED) {
					IAlgorithm alg = (IAlgorithm) ev.getItem();
					myPaintRecognitionManager.setAlgorithm(alg);
				}
			}
		};
		box.addItemListener(listener);

		box.setSelectedItem(box.getItemAt(0));
		myPaintRecognitionManager.setAlgorithm((IAlgorithm) box.getItemAt(0));
		return box;
	}

	private JPanel createListButtonPanel() {
		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JButton remove = new JButton(getRemoveSampleAction());
		JButton newBase = new JButton(getNewBaseAction());
		JButton addBase = new JButton(getAddBaseAction());

		mySaveAction = getSaveBaseAction();
		// иначе сначала не работает
		updateSaveAction();
		JButton saveBase = new JButton(mySaveAction);

		JButton saveBaseAs = new JButton(getSaveBaseAsAction());

		panel.add(newBase);
		panel.add(addBase);
		panel.add(remove);
		panel.add(saveBase);
		panel.add(saveBaseAs);

		View.setComponentSize(remove, 40, 40);
		View.setComponentSize(newBase, 40, 40);
		View.setComponentSize(addBase, 40, 40);
		View.setComponentSize(saveBase, 40, 40);
		View.setComponentSize(saveBaseAs, 40, 40);

		return panel;
	}

	private void updateSaveAction() {
		mySaveAction.setEnabled(mySaveAction.isEnabled());
	}

	private JList createList() {
		myListModel = new DefaultListModel();

		return new JList(myListModel);
	}

	public PaintingPanel getPaintingPanel() {
		return myPaintingPanel;
	}

	public DefaultListModel getListModel() {
		return myListModel;
	}

	public JList getList() {
		return myList;
	}

	private AbstractAction getRemoveSampleAction() {
		AbstractAction act = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				ISample sample = (ISample) myList.getSelectedValue();

				myPaintRecognitionManager.removeSample(sample);
				myListModel.removeElement(sample);
			}

		};
		// act.putValue(AbstractAction.NAME, "Remove sample");
		act.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Remove sample of selected symbol");
		View.setIcon("Remove.png", act);
		return act;
	}

	private AbstractAction getNewBaseAction() {
		AbstractAction newBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				if (myIsToSave) {
					int result = JOptionPane.showConfirmDialog(
							PaintRecognitionTab.this, "Save changes?",
							"Symbol recognition",
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						save();
					} else if (result == JOptionPane.CANCEL_OPTION) {
						return;
					}
				}

				mySource = null;
				myIsToSave = false;
				myListModel.clear();
				myPaintingPanel.clear();
				myPaintRecognitionManager.newSymbolBase();
				updateSaveAction();
			}
		};
		// newBase.putValue(AbstractAction.NAME, "New");
		newBase.putValue(AbstractAction.SHORT_DESCRIPTION, "New symbol base");
		View.setIcon("New.png", newBase);
		return newBase;
	}

	private AbstractAction getAddBaseAction() {
		AbstractAction addBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				File[] files = chooseAddFile();

				if (files == null) {
					return;
				}
				ISample[] samples;
				for (File f : files) {
					samples = myPaintRecognitionManager.addFile(f);
					for (ISample s : samples) {
						myListModel.addElement(s);
					}
				}
			}

		};
		// addBase.putValue(AbstractAction.NAME, "Add base");
		addBase.putValue(AbstractAction.SHORT_DESCRIPTION, "Add symbol base");
		View.setIcon("Add.png", addBase);
		return addBase;
	}

	private File[] chooseAddFile() {
		myAddFileChooser.setVisible(true);
		int res = myAddFileChooser.showOpenDialog(this);

		if (res != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		return myAddFileChooser.getSelectedFiles();
	}

	private AbstractAction getSaveBaseAction() {
		AbstractAction saveBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				save();
				updateSaveAction();
			}

			public boolean isEnabled() {
				return myIsToSave;
			}
		};
		// saveBase.putValue(AbstractAction.NAME, "Save base");
		saveBase.putValue(AbstractAction.SHORT_DESCRIPTION, "Save symbol base");
		View.setIcon("Save.png", saveBase);
		return saveBase;
	}

	private void save() {
		if (mySource != null) {
			myPaintRecognitionManager.saveTo(mySource);
			myIsToSave = false;
		} else {
			saveAs();
		}
	}

	private AbstractAction getSaveBaseAsAction() {
		AbstractAction saveBaseAs = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				saveAs();
				updateSaveAction();
			}
		};
		// saveBaseAs.putValue(AbstractAction.NAME, "Save base as...");
		saveBaseAs.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Save symbol base as...");
		View.setIcon("SaveAs.png", saveBaseAs);
		return saveBaseAs;
	}

	private void saveAs() {
		mySaveFileChooser.setVisible(true);
		int res = mySaveFileChooser.showSaveDialog(this);

		if (res == JFileChooser.APPROVE_OPTION) {

			File f = mySaveFileChooser.getSelectedFile();

			try {
				myPaintRecognitionManager.saveTo(f);
				myIsToSave = false;
			} catch (RuntimeException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void exit() {
		if (myIsToSave) {
			int result = JOptionPane.showConfirmDialog(this, "Save changes?",
					"Symbol recognition", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				save();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}

		System.exit(0);
	}

	public PaintRecognitionManager getSavingManager() {
		return myPaintRecognitionManager;
	}
}
