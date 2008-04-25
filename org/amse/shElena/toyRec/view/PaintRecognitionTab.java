package org.amse.shElena.toyRec.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.amse.shElena.toyRec.algorithms.AlgorithmManager;
import org.amse.shElena.toyRec.algorithms.Algorithm;
import org.amse.shElena.toyRec.manager.IManager;
import org.amse.shElena.toyRec.samples.ISample;

public class PaintRecognitionTab extends JPanel {
	private static final long serialVersionUID = 1L;

	private IManager myManager;

	private PaintingPanel myPaintingPanel;

	private SamplePainter mySamplePainter;

	private DefaultListModel myListModel;

	private JList myList;

	private AbstractAction mySaveAction;

	private PaintRecognitionManager myPaintRecognitionManager;

	public PaintRecognitionTab(IManager manager) {
		myManager = manager;
		myPaintRecognitionManager = new PaintRecognitionManager(this);

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		add(createListPanel());
		add(createPaintPanel());

		setSize(450, 300 + myManager.getHeight() * 10);
		View.setComponentSize(this, 450, 300 + myManager.getHeight() * 10);
	}

	private JPanel createPaintPanel() {
		JPanel panel = new JPanel();

		panel.setPreferredSize(new Dimension(200,
				270 + myManager.getHeight() * 10));
		
		JLabel lab = new JLabel("Painting panel");
		View.setComponentSize(lab, 100, 15);
		panel.add(lab);
		
		myPaintingPanel = new PaintingPanel();
		myPaintingPanel.setPreferredSize(new Dimension(200, 220));
		//myPaintingPanel.setBorder(new LineBorder(Color.black, 1));
		
		panel.add(myPaintingPanel);

		mySamplePainter = new SamplePainter(myManager);

		JPanel buttons = createPaintButtonPanel();
		buttons.setPreferredSize(new Dimension(200, 40));
		panel.add(buttons);

		JPanel sample = new JPanel();
		sample.setLayout(new BoxLayout(sample, BoxLayout.Y_AXIS));

		mySamplePainter.setAlignmentX(Component.RIGHT_ALIGNMENT);
		sample.add(mySamplePainter);
		View.setComponentSize(mySamplePainter, 10 * myManager.getWidth(),
				10 * myManager.getHeight());
		View.setComponentSize(sample, 200, 5 + 10 * myManager.getHeight());

		panel.add(sample);

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
		upPan.add(rec);

		JButton clear = new JButton(getClearAction());
		lowPan.add(clear);

		JButton show = new JButton(getShowSampleAction());
		lowPan.add(show);

		panel.add(upPan);
		panel.add(lowPan);

		View.setComponentSize(add, 85, 20);
		View.setComponentSize(rec, 115, 20);
		View.setComponentSize(clear, 85, 20);
		View.setComponentSize(show, 115, 20);

		return panel;
	}

	private AbstractAction getClearAction() {
		AbstractAction clear = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myPaintRecognitionManager.clearPaints();
			}

		};
		clear.putValue(AbstractAction.NAME, "Clear");
		clear.putValue(AbstractAction.SHORT_DESCRIPTION, "Clear painting area");
		return clear;
	}

	private AbstractAction getShowSampleAction() {
		AbstractAction show = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myPaintRecognitionManager.showSample();
			}

		};
		show.putValue(AbstractAction.NAME, "Show sample");
		show.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Shows sample of symbol");

		return show;
	}

	private AbstractAction getAddSampleAction() {
		AbstractAction add = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myPaintRecognitionManager.addSample();
				updateSaveAction();
			}

		};
		add.putValue(AbstractAction.NAME, "Add");
		add.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Add a symbol to the base");

		return add;
	}

	private AbstractAction getRecognizeAction() {
		AbstractAction recognize = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myPaintRecognitionManager.recognize();
			}

		};
		recognize.putValue(AbstractAction.NAME, "Recognize");
		recognize
				.putValue(AbstractAction.SHORT_DESCRIPTION, "Recognize symbol");

		return recognize;
	}

	private JPanel createListPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 400));
		// panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel lab = new JLabel("Learning base");
		View.setComponentSize(lab, 100, 15);
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
		JComboBox box = new JComboBox(AlgorithmManager.getAlgorithms());

		ItemListener listener = new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				if (ev.getStateChange() == ItemEvent.SELECTED) {
					Algorithm alg = (Algorithm) ev.getItem();
					myManager.setAlgorithm(alg);
				}
			}
		};
		// box.add
		box.addItemListener(listener);

		box.setSelectedItem(myManager.getAlgorithm());
		return box;
	}

	private JPanel createListButtonPanel() {
		JPanel panel = new JPanel();
		/*panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));

		JButton remove = new JButton(getRemoveSampleAction());

		JButton newBase = new JButton(getNewBaseAction());

		JButton addBase = new JButton(getAddBaseAction());

		mySaveAction = getSaveBaseAction();
		// иначе сначала не реботает
		updateSaveAction();
		JButton saveBase = new JButton(mySaveAction);

		JButton saveBaseAs = new JButton(getSaveBaseAsAction());

		panel1.add(newBase);
		panel1.add(addBase);

		panel2.add(saveBase);
		panel2.add(saveBaseAs);

		View.setComponentSize(remove, 200, 20);
		View.setComponentSize(newBase, 80, 20);
		View.setComponentSize(addBase, 120, 20);
		View.setComponentSize(saveBase, 80, 20);
		View.setComponentSize(saveBaseAs, 120, 20);
		
		JPanel p = new JPanel();
		// почему-то это все спасает
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(remove);
		panel.add(p);*/
		
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

		updateListModel();

		JList list = new JList(myListModel);
		ListSelectionListener listener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent ev) {
				ISample sample = (ISample) myList.getSelectedValue();

				mySamplePainter.showSample(sample);
			}
		};
		list.addListSelectionListener(listener);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		return list;
	}

	public IManager getManager() {
		return myManager;
	}

	public PaintingPanel getPaintingPanel() {
		return myPaintingPanel;
	}

	public SamplePainter getSamplePainter() {
		return mySamplePainter;
	}

	public DefaultListModel getListModel() {
		return myListModel;
	}

	public JList getList() {
		return myList;
	}

	public void clear() {
		myListModel.clear();
		myPaintingPanel.clear();
		mySamplePainter.clear();
	}

	public void updateListModel() {
		myListModel.clear();

		ISample[] samples = myManager.getSamples();

		for (ISample s : samples) {
			myListModel.addElement(s);
		}

	}

	private AbstractAction getRemoveSampleAction() {
		AbstractAction act = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				ISample sample = (ISample) myList.getSelectedValue();

				myManager.removeSample(sample);
				myListModel.removeElement(sample);
			}

		};
		//act.putValue(AbstractAction.NAME, "Remove sample");
		act.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Remove sample of selected symbol");
		View.setIcon("Remove.png", act);
		return act;
	}

	private AbstractAction getNewBaseAction() {
		AbstractAction newBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myPaintRecognitionManager.newSymbolBase();
				updateSaveAction();
			}
		};
		//newBase.putValue(AbstractAction.NAME, "New");
		newBase.putValue(AbstractAction.SHORT_DESCRIPTION, "New symbol base");
		View.setIcon("New.png", newBase);
		return newBase;
	}

	private AbstractAction getAddBaseAction() {
		AbstractAction addBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myPaintRecognitionManager.addBase();
			}

		};
		//addBase.putValue(AbstractAction.NAME, "Add base");
		addBase.putValue(AbstractAction.SHORT_DESCRIPTION, "Add symbol base");
		View.setIcon("Add.png", addBase);
		return addBase;
	}

	private AbstractAction getSaveBaseAction() {
		AbstractAction saveBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myPaintRecognitionManager.save();
				updateSaveAction();
			}

			public boolean isEnabled() {
				return myPaintRecognitionManager.isBaseToSave();
			}
		};
		//saveBase.putValue(AbstractAction.NAME, "Save base");
		saveBase.putValue(AbstractAction.SHORT_DESCRIPTION, "Save symbol base");
		View.setIcon("Save.png", saveBase);
		return saveBase;
	}

	private AbstractAction getSaveBaseAsAction() {
		AbstractAction saveBaseAs = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myPaintRecognitionManager.saveAs();
				updateSaveAction();
			}
		};
		//saveBaseAs.putValue(AbstractAction.NAME, "Save base as...");
		saveBaseAs.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Save symbol base as...");
		View.setIcon("SaveAs.png", saveBaseAs);
		return saveBaseAs;
	}

	public PaintRecognitionManager getSavingManager() {
		return myPaintRecognitionManager;
	}
}
