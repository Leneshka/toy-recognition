package org.amse.shElena.toyRec.view;

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
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.amse.shElena.toyRec.algorithms.AlgorithmManager;
import org.amse.shElena.toyRec.algorithms.IAlgorithm;
import org.amse.shElena.toyRec.manager.IManager;
import org.amse.shElena.toyRec.samples.ISample;
import org.amse.shElena.toyRec.view.paintActions.AddSymbolAction;
import org.amse.shElena.toyRec.view.paintActions.ClearAction;
import org.amse.shElena.toyRec.view.paintActions.RecognizeSymbolAction;
import org.amse.shElena.toyRec.view.paintActions.ShowSampleAction;

public class PaintRecognitionTab extends JPanel {
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

		setSize(450, 300 + myManager.getHeight() * 10);
		View.setComponentSize(this, 450, 300 + myManager.getHeight() * 10);
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

		JButton add = new JButton(new AddSymbolAction(this));
		upPan.add(add);

		JButton rec = new JButton(new RecognizeSymbolAction(this));
		upPan.add(rec);

		JButton clear = new JButton(new ClearAction(this));
		lowPan.add(clear);

		JButton show = new JButton(new ShowSampleAction(this));
		lowPan.add(show);

		panel.add(upPan);
		panel.add(lowPan);

		View.setComponentSize(add, 85, 20);
		View.setComponentSize(rec, 115, 20);
		View.setComponentSize(clear, 85, 20);
		View.setComponentSize(show, 115, 20);

		return panel;
	}

	private JPanel createListPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 400));
		// panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		myList = createList();
		JScrollPane scroll = new JScrollPane(myList);
		scroll.setPreferredSize(new Dimension(200, 220));

		panel.add(scroll);

		JPanel buttons = createListButtonPanel();
		View.setComponentSize(buttons, 200, 60);
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
				if(ev.getStateChange() == ItemEvent.SELECTED){
					IAlgorithm alg = (IAlgorithm) ev.getItem();
					myManager.setAlgorithm(alg);
				}
			}
		};
		//box.add
		box.addItemListener(listener);
		
		box.setSelectedItem(myManager.getAlgorithm());
		return box;
	}

	private JPanel createListButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));

		JButton remove = new JButton(getRemoveSampleAction());

		JButton newBase = new JButton(getNewBaseAction());

		JButton addBase = new JButton(getAddBaseAction());

		JButton saveBase = new JButton(getSaveBaseAction());

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

		panel.add(panel1);
		panel.add(panel2);
		
		JPanel p = new JPanel();
		// почему-то это все спасает
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(remove);
		panel.add(p);

		

		return panel;
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
		act.putValue(AbstractAction.NAME, "Remove sample");
		act.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Remove sample of selected symbol");
		return act;
	}

	private AbstractAction getNewBaseAction() {
		AbstractAction newBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myPaintRecognitionManager.newSymbolBase();
			}
		};
		newBase.putValue(AbstractAction.NAME, "New");
		newBase.putValue(AbstractAction.SHORT_DESCRIPTION, "New symbol base");
		return newBase;
	}

	private AbstractAction getAddBaseAction() {
		AbstractAction addBase = new AbstractAction(){
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myPaintRecognitionManager.addBase();				
			}
			
		};
		addBase.putValue(AbstractAction.NAME, "Add base");
		addBase.putValue(AbstractAction.SHORT_DESCRIPTION, "Add symbol base");
		return addBase;
	}

	private AbstractAction getSaveBaseAction() {
		AbstractAction saveBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myPaintRecognitionManager.save();
			}
		};
		saveBase.putValue(AbstractAction.NAME, "Save base");
		saveBase.putValue(AbstractAction.SHORT_DESCRIPTION, "Save symbol base");
		return saveBase;
	}

	private AbstractAction getSaveBaseAsAction() {
		AbstractAction saveBaseAs = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myPaintRecognitionManager.saveAs();
			}
		};
		saveBaseAs.putValue(AbstractAction.NAME, "Save base as...");
		saveBaseAs.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Save symbol base as...");
		return saveBaseAs;
	}

	public JMenuBar getMenu() {
		if (myMenu != null) {
			return myMenu;
		} else {
			JMenuBar menuBar = new JMenuBar();
			return menuBar;
		}
	}

	public PaintRecognitionManager getSavingManager() {
		return myPaintRecognitionManager;
	}
}
