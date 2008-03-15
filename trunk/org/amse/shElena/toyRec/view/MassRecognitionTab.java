package org.amse.shElena.toyRec.view;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.amse.shElena.toyRec.algorithms.ClassComparisonAlgorithm;
import org.amse.shElena.toyRec.algorithms.IAlgorithm;
import org.amse.shElena.toyRec.algorithms.KohonenNetworkAlgorithm;
import org.amse.shElena.toyRec.algorithms.SimpleComparisonAlgorithm;
import org.amse.shElena.toyRec.manager.IManager;
import org.amse.shElena.toyRec.samples.ISample;

public class MassRecognitionTab extends Tab {
	private static final long serialVersionUID = 1L;

	private IManager myManager;

	// private SamplePainter mySamplePainter;
	
	private MassRecognitionManager myMassRecognitionManager;

	private DefaultListModel myLearnListModel;

	private DefaultListModel myRecListModel;

	private DefaultListModel myAlgorithmListModel;

	private JMenuBar myMenu;
	
	private List<IAlgorithm> myAlgorithms;
	
	private final int MY_WIDTH = 100;

	public MassRecognitionTab(IManager manager) {
		myManager = manager;
		myAlgorithms = new ArrayList<IAlgorithm>();
		myAlgorithms.add(SimpleComparisonAlgorithm.getInstance());
		myAlgorithms.add(ClassComparisonAlgorithm.getInstance());
		myAlgorithms.add(KohonenNetworkAlgorithm.getInstance());
		
		myMassRecognitionManager = new MassRecognitionManager(this);
		
		JPanel learn = createLearnPanel();
		setComponentSize(learn, MY_WIDTH, 720);
		add(learn);
		
		JPanel rec = createRecognizePanel();
		setComponentSize(rec, MY_WIDTH, 720);
		add(rec);
		
		JPanel alg = createAlgorithmPanel();
		setComponentSize(alg, 2*MY_WIDTH, 720);
		add(alg);
		
		// setJMenuBar(createMenuBar());

		//setSize(450, 300 + myManager.getHeight() * 10);
		// setComponentSize(this, 450, 300 + myManager.getHeight() * 10);
	}

	public IManager getManager() {
		return myManager;
	}
	
	private JPanel createLearnPanel() {
		JPanel panel = new JPanel();
		JScrollPane scroll = new JScrollPane(createLearnList());

		setComponentSize(scroll, MY_WIDTH, 320);
		panel.add(scroll);
		
		//JButton learn = new JButton(/*new RemoveAction()*/);
		//panel.add(learn);
		//setComponentSize(learn, MY_WIDTH, 20);

		return panel;
	}

	private JPanel createRecognizePanel() {
		JPanel panel = new JPanel();
		JScrollPane scroll = new JScrollPane(createRecList());

		setComponentSize(scroll, MY_WIDTH, 320);
		panel.add(scroll);
		
		//JButton recognize = new JButton(/*new RemoveAction()*/);
		//panel.add(recognize);
		//setComponentSize(recognize, MY_WIDTH, 20);

		return panel;
	}
	
	private JPanel createAlgorithmPanel() {
		JPanel panel = new JPanel();
		JScrollPane scroll = new JScrollPane(createAlgList());
	
		setComponentSize(scroll, 2*MY_WIDTH, 320);
		panel.add(scroll);

		

		AbstractAction recognize = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				IAlgorithm[] ar = new IAlgorithm[myAlgorithmListModel.size()];
				int i = 0;
				for(Object alg : myAlgorithmListModel.toArray()){
					ar[i] = (IAlgorithm) alg;
					i++;
				}
				myMassRecognitionManager.testRecognition(ar);
			}
		};		
		
		JButton rec = new JButton(recognize);
		rec.setText("Test recognition");
		panel.add(rec);
		setComponentSize(rec, 2*MY_WIDTH, 20);
		
		return panel;
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

	public void updateListModel(DefaultListModel listModel, ISample[] samples) {
		listModel.clear();
		for (ISample s : samples) {
			listModel.addElement(s);
		}
	}

	@Override
	public JMenuBar getMenu() {
		if (myMenu != null) {
			return myMenu;
		} else {
			JMenuBar menuBar = new JMenuBar();
			
			menuBar.add(getLearnMenu());
			
			menuBar.add(getRecognizeMenu());
			
			menuBar.add(getAlgorithmMenu());
			
			myMenu = menuBar;

			return menuBar;
		}
	}
	
	private JMenu getLearnMenu(){
		JMenu baseMenu = new JMenu("Learn base");

		AbstractAction newBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myMassRecognitionManager.newLearnBase();
				updateListModel(myLearnListModel, myManager.getSamples());
			}
		};

		JMenuItem newSymbolBase = new JMenuItem(newBase);
		newSymbolBase.setText("New symbol base");

		AbstractAction loadFileBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myMassRecognitionManager.loadLearnFileBase();
				updateListModel(myLearnListModel, myManager.getSamples());
			}
		};
		

		JMenuItem loadFile = new JMenuItem(loadFileBase);
		loadFile.setText("Load  file base...");

		AbstractAction loadPictureBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myMassRecognitionManager.loadLearnPictureBase();
				updateListModel(myLearnListModel, myManager.getSamples());
			}
		};

		JMenuItem loadPicture = new JMenuItem(loadPictureBase);

		loadPicture.setText("Load picture base...");

		baseMenu.add(newSymbolBase);
		baseMenu.add(loadFile);
		baseMenu.add(loadPicture);
		return baseMenu;
	}
	
	private JMenu getRecognizeMenu(){
		JMenu baseMenu = new JMenu("Recognize base");

		AbstractAction newBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myMassRecognitionManager.newRecBase();
				updateListModel(myRecListModel, myMassRecognitionManager.getRecSamples());
			}
		};

		JMenuItem newSymbolBase = new JMenuItem(newBase);
		newSymbolBase.setText("New symbol base");

		AbstractAction loadFileBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myMassRecognitionManager.loadRecFileBase();
				updateListModel(myRecListModel, myMassRecognitionManager.getRecSamples());
			}
		};
		

		JMenuItem loadFile = new JMenuItem(loadFileBase);
		loadFile.setText("Load  file base...");

		AbstractAction loadPictureBase = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myMassRecognitionManager.loadRecPictureBase();
				updateListModel(myRecListModel, myMassRecognitionManager.getRecSamples());
			}
		};

		JMenuItem loadPicture = new JMenuItem(loadPictureBase);

		loadPicture.setText("Load picture base...");

		baseMenu.add(newSymbolBase);
		baseMenu.add(loadFile);
		baseMenu.add(loadPicture);
		return baseMenu;
	}
	
	private JMenu getAlgorithmMenu(){
		JMenu baseMenu = new JMenu("Algorithms");

		for(final IAlgorithm alg : myAlgorithms){
			AbstractAction algAction = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent arg0) {
					if(myAlgorithmListModel.contains(alg)){
						myAlgorithmListModel.removeElement(alg);
					} else {
						myAlgorithmListModel.addElement(alg);
					}
				}
			};

			JMenuItem item = new JMenuItem(algAction);
			item.setText(alg.toString());
			
			baseMenu.add(item);
		}
		
		return baseMenu;
	}
}
