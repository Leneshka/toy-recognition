package org.amse.shElena.toyRec.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.amse.shElena.toyRec.algorithms.ClassComparisonAlgorithm;
import org.amse.shElena.toyRec.algorithms.IAlgorithm;
import org.amse.shElena.toyRec.algorithms.KohonenNetworkAlgorithm;
import org.amse.shElena.toyRec.algorithms.SimpleComparisonAlgorithm;
import org.amse.shElena.toyRec.manager.IManager;
import org.amse.shElena.toyRec.samples.ISample;

public class PaintRecognitionTab extends Tab {
	private static final long serialVersionUID = 1L;

	private IManager myManager;

	private PaintingPanel myPaintingPanel;

	private SamplePainter mySamplePainter;

	private DefaultListModel myListModel;

	private JList myList;

	private PaintRecognitionManager myPaintRecognitionManager;

	private JMenuBar myMenu;

	public PaintRecognitionTab(IManager manager) {
		myManager = manager;
		myPaintRecognitionManager = new PaintRecognitionManager(this);

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		add(createListPanel());
		add(createPaintPanel());

		// setJMenuBar(createMenuBar());

		setSize(450, 300 + myManager.getHeight() * 10);
		setComponentSize(this, 450, 300 + myManager.getHeight() * 10);

		// setResizable(false);
	}

	private JPanel createPaintPanel() {
		JPanel panel = new JPanel();

		panel.setPreferredSize(new Dimension(200,
				270 + myManager.getHeight() * 10));

		myPaintingPanel = new PaintingPanel();
		myPaintingPanel.setPreferredSize(new Dimension(200, 220));

		panel.add(myPaintingPanel);

		mySamplePainter = new SamplePainter(myManager);

		JPanel buttons = createPaintButtonPanel();
		buttons.setPreferredSize(new Dimension(200, 40));
		panel.add(buttons);

		JPanel sample = new JPanel();
		sample.setLayout(new BoxLayout(sample, BoxLayout.Y_AXIS));

		mySamplePainter.setAlignmentX(Component.RIGHT_ALIGNMENT);
		sample.add(mySamplePainter);
		setComponentSize(mySamplePainter, 10 * myManager.getWidth(),
				10 * myManager.getHeight());
		setComponentSize(sample, 200, 5 + 10 * myManager.getHeight());

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

		JButton add = new JButton(new AddAction(this));
		upPan.add(add);

		JButton rec = new JButton(new RecognizeSymbolAction(this));
		upPan.add(rec);

		JButton clear = new JButton(new ClearAction(this));
		lowPan.add(clear);

		JButton show = new JButton(new ShowSampleAction(this));
		lowPan.add(show);

		panel.add(upPan);
		panel.add(lowPan);

		setComponentSize(add, 85, 20);
		setComponentSize(rec, 115, 20);
		setComponentSize(clear, 85, 20);
		setComponentSize(show, 115, 20);
		/*
		 * setComponentSize(upPan, 200, 20); setComponentSize(lowPan, 200, 20);
		 * setComponentSize(panel, 200, 40);
		 */

		return panel;
	}



	private JPanel createListPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 400));

		myList = createList();
		JScrollPane scroll = new JScrollPane(myList);
		scroll.setPreferredSize(new Dimension(200, 220));

		panel.add(scroll);

		JButton remove = new JButton(new RemoveAction());
		remove.setPreferredSize(new Dimension(200, 40));
		panel.add(remove);

		JPanel algpanel = new JPanel(new GridLayout(0, 1, 0, 0));
		// algpanel.setBorder(BorderFactory.createTitledBorder("Algorithm"));
		ButtonGroup bg = new ButtonGroup();

		final IAlgorithm simple = SimpleComparisonAlgorithm.getInstance();
		JRadioButton sBut = setRadioButton(simple, algpanel, bg);

		sBut.setSelected(true);
		sBut.setPreferredSize(new Dimension(200, 20));

		final IAlgorithm cl = ClassComparisonAlgorithm.getInstance();
		JRadioButton clBut = setRadioButton(cl, algpanel, bg);

		clBut.setPreferredSize(new Dimension(200, 20));

		final IAlgorithm kn = KohonenNetworkAlgorithm.getInstance();
		JRadioButton knBut = setRadioButton(kn, algpanel, bg);

		knBut.setPreferredSize(new Dimension(200, 20));

		panel.add(algpanel);

		return panel;
	}

	private JList createList() {
		myListModel = new DefaultListModel();

		updateListModel();

		JList list = new JList(myListModel);
		list.addListSelectionListener(new ListListener());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		return list;
	}

	private JRadioButton setRadioButton(final IAlgorithm algorithm,
			JPanel panel, ButtonGroup bg) {
		JRadioButton but = new JRadioButton();
		but.setText(algorithm.toString());

		ActionListener l = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myManager.setAlgorithm(algorithm);
			}
		};

		but.addActionListener(l);

		panel.add(but);
		bg.add(but);
		return but;
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

	private class ListListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent ev) {
			ISample sample = (ISample) myList.getSelectedValue();

			mySamplePainter.showSample(sample);
		}
	}

	private class RemoveAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public RemoveAction() {
			putValue(AbstractAction.NAME, "Remove sample");
			putValue(SHORT_DESCRIPTION, "Remove sample of selected symbol");
		}

		public void actionPerformed(ActionEvent arg0) {
			ISample sample = (ISample) myList.getSelectedValue();

			myManager.removeSample(sample);
			myListModel.removeElement(sample);
		}
	}

	public JMenuBar getMenu() {
		if (myMenu != null) {
			return myMenu;
		} else {
			JMenuBar menuBar = new JMenuBar();
			JMenu baseMenu = new JMenu("Symbol base");

			AbstractAction newBase = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent arg0) {
					myPaintRecognitionManager.newSymbolBase();
				}
			};

			JMenuItem newSymbolBase = new JMenuItem(newBase);
			newSymbolBase.setText("New symbol base");

			AbstractAction loadBase = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent arg0) {
					myPaintRecognitionManager.loadFileSymbolBase();
				}
			};

			JMenuItem load = new JMenuItem(loadBase);
			load.setText("Load  file base...");

			AbstractAction createBase = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent arg0) {
					myPaintRecognitionManager.loadPictureSymbolBase();
				}
			};

			JMenuItem create = new JMenuItem(createBase);

			create.setText("Load picture base...");

			AbstractAction saveBase = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent arg0) {
					myPaintRecognitionManager.save();
				}
			};

			JMenuItem save = new JMenuItem(saveBase);

			save.setText("Save symbol base...");

			AbstractAction saveBaseAs = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent arg0) {
					myPaintRecognitionManager.saveAs();
				}
			};

			JMenuItem saveAs = new JMenuItem(saveBaseAs);

			saveAs.setText("Save symbol base as...");

			AbstractAction exitAction = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent arg0) {
					myPaintRecognitionManager.exit();
				}
			};

			JMenuItem exit = new JMenuItem(exitAction);

			exit.setText("Exit");

			baseMenu.add(newSymbolBase);
			baseMenu.add(load);
			baseMenu.add(create);
			baseMenu.addSeparator();
			baseMenu.add(save);
			baseMenu.add(saveAs);
			baseMenu.addSeparator();

			baseMenu.add(exit);
			menuBar.add(baseMenu);

			myMenu = menuBar;

			return menuBar;
		}
	}
	
	public PaintRecognitionManager getSavingManager(){
		return myPaintRecognitionManager;
	}

}
