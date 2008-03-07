package org.amse.shElena.toyRec.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
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

public class View extends JFrame {
	private static final long serialVersionUID = 1L;

	private IManager myManager;

	private PaintingPanel myPaintingPanel;

	private SamplePainter mySamplePainter;

	private DefaultListModel myListModel;

	private JList myList;

	private SavingManager mySavingManager;

	private List<AbstractAction> myAlgorithmActions;

	public View(IManager manager) {
		myManager = manager;
		mySavingManager = new SavingManager(this);
		myAlgorithmActions = new ArrayList<AbstractAction>();

		Container c = new JPanel();
		c.setLayout(new BoxLayout(c, BoxLayout.X_AXIS));

		c.add(createListPanel());
		c.add(createPaintPanel());

		setContentPane(c);

		setJMenuBar(createMenuBar());

		setSize(430, 340 + myManager.getHeight() * 10);

		setResizable(false);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		WindowListener l = new WindowAdapter() {

			public void windowClosing(WindowEvent event) {
				mySavingManager.exit();
			}
		};
		addWindowListener(l);

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

		JButton rec = new JButton(new RecognizeAction(this));
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
	/*	setComponentSize(upPan, 200, 20);
		setComponentSize(lowPan, 200, 20);
		setComponentSize(panel, 200, 40);*/

		return panel;
	}

	private void setComponentSize(JComponent comp, int width, int height) {
		Dimension d = new Dimension(width, height);
		comp.setMaximumSize(d);
		comp.setMinimumSize(d);
		comp.setPreferredSize(d);
	}

	private JPanel createListPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 400));
		
		myList = createList();
		//myList.setPreferredSize(new Dimension(170, 220));
		JScrollPane scroll = new JScrollPane(myList);
		scroll.setPreferredSize(new Dimension(200, 220));

		panel.add(scroll);

		JButton remove = new JButton(new RemoveAction());
		remove.setPreferredSize(new Dimension(200, 40));
		panel.add(remove);

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

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu baseMenu = new JMenu("Symbol base");
		// baseMenu.setMnemonic('S');

		AbstractAction newBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				mySavingManager.newSymbolBase();
			}
		};
		JMenuItem newSymbolBase = new JMenuItem(newBase);
		// newSymbolBase.setIcon(null);
		newSymbolBase.setText("New symbol base");
		// newSymbolBase.setMnemonic('N');

		AbstractAction loadBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				mySavingManager.load();
			}
		};
		JMenuItem load = new JMenuItem(loadBase);
		// newSymbolBase.setIcon(null);
		load.setText("Load symbol base...");
		// load.setMnemonic('L');

		AbstractAction saveBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				mySavingManager.save();
			}
		};

		JMenuItem save = new JMenuItem(saveBase);

		// newSymbolBase.setIcon(null);
		save.setText("Save symbol base...");
		// save.setMnemonic('S');

		AbstractAction saveBaseAs = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				mySavingManager.saveAs();
			}
		};

		JMenuItem saveAs = new JMenuItem(saveBaseAs);

		// newSymbolBase.setIcon(null);
		saveAs.setText("Save symbol base as...");
		// saveAs.setMnemonic('a');

		
		AbstractAction createBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				mySavingManager.createSymbolBase();
			}
		};

		JMenuItem create = new JMenuItem(createBase);

		// newSymbolBase.setIcon(null);
		create.setText("Create symbol base...");
		// save.setMnemonic('S');

		
		AbstractAction exitAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				mySavingManager.exit();
			}
		};

		JMenuItem exit = new JMenuItem(exitAction);

		// newSymbolBase.setIcon(null);
		exit.setText("Exit");
		// exit.setMnemonic('E');

		baseMenu.add(newSymbolBase);
		baseMenu.add(load);
		baseMenu.addSeparator();
		baseMenu.add(save);
		baseMenu.add(saveAs);
		baseMenu.addSeparator();
		baseMenu.add(create);
		baseMenu.addSeparator();
		baseMenu.add(exit);
		menuBar.add(baseMenu);

		JMenu algMenu = new JMenu("Set algorithm");

		final IAlgorithm simple = SimpleComparisonAlgorithm.getInstance();
		JMenuItem sMenu = getMenuItem(simple);

		final IAlgorithm cl = ClassComparisonAlgorithm.getInstance();
		JMenuItem clMenu = getMenuItem(cl);
		
		final IAlgorithm kn = KohonenNetworkAlgorithm.getInstance();
		JMenuItem knMenu = getMenuItem(kn);
		

		algMenu.add(sMenu);
		algMenu.add(clMenu);
		algMenu.add(knMenu);
		menuBar.add(algMenu);

		updateAlgMenu();
		
		JMenu  test = new JMenu("Test recognition");

		JMenuItem testXML = new JMenuItem(TestRecognition.getTestRecognitionXMLAction(this));
		JMenuItem testPicture = new JMenuItem(TestRecognition.getTestRecognitionPictureAction(this));
		
		test.add(testXML);
		test.add(testPicture);
		menuBar.add(test);
		
		return menuBar;
	}

	private JMenuItem getMenuItem(final IAlgorithm algorithm) {
		AbstractAction sAction = new AbstractAction() {
			private IAlgorithm myAlgorithm = algorithm;

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myManager.setAlgorithm(algorithm);
				updateAlgMenu();
			}

			public boolean isEnabled() {
				return myManager.getAlgorithm() != myAlgorithm;
			}
		};

		myAlgorithmActions.add(sAction);

		JMenuItem menu = new JMenuItem(sAction);

		menu.setText(algorithm.getName());

		return menu;
	}

	private void updateAlgMenu() {
		for (AbstractAction act : myAlgorithmActions) {
			act.setEnabled(act.isEnabled());
		}
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
}
