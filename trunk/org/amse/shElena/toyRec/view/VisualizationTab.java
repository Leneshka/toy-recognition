package org.amse.shElena.toyRec.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import org.amse.shElena.toyRec.samples.Sampler;

public class VisualizationTab extends JPanel {
	private static final long serialVersionUID = 1L;

	private PaintingPanel myPaintingPanel;

	private VisualizationAutomataManager myManager;

	private JFileChooser myFileChooser;
	
	private List<AbstractAction> myTempActions;
	private AbstractAction myStartAction;
	
	public VisualizationTab() {
		myManager = new VisualizationAutomataManager();
		myTempActions = new ArrayList<AbstractAction>();
		
		add(createUpperPanel());
		add(createLowerPanel());
		
		myFileChooser = new JFileChooser();
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				return (file.isDirectory() || file.getName().endsWith(".bmp"));
			}

			@Override
			public String getDescription() {
				return ".bmp files";
			}
		};

		myFileChooser.addChoosableFileFilter(filter);
		myFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		myFileChooser.setDialogTitle("Load");
		myFileChooser.setCurrentDirectory(new File("."));
		
		updateActions();
	}

	private JPanel createUpperPanel() {
		JPanel panel = new JPanel();
		
		JPanel leftPanel = new JPanel();
		JLabel lab = new JLabel("Painting panel");
		View.setComponentSize(lab, 280, 15);
		leftPanel.add(lab);
		myPaintingPanel = new PaintingPanel();
		View.setComponentSize(myPaintingPanel, 280, 220);
		myPaintingPanel.setBorder(new LineBorder(Color.gray, 1));
		leftPanel.add(myPaintingPanel);
		
		JPanel buttons = createButtonPanel();
		panel.add(buttons);

		panel.add(leftPanel);
		panel.add(buttons);
	
		View.setComponentSize(leftPanel, 280, 255);
		View.setComponentSize(buttons, 140, 255);
		View.setComponentSize(panel, 450, 255);
		return panel;
	}
	
	private JPanel createLowerPanel() {
		JPanel panel = new JPanel();
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JButton clear = new JButton(getClearAction());
		panel.add(clear);

		JButton load = new JButton(getLoadAction());
		panel.add(load);
		
		View.setComponentSize(clear, 140, 25);
		View.setComponentSize(load, 140, 25);
		View.setComponentSize(panel, 430, 25);
		return panel;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel lab = new JLabel("");
		panel.add(lab);
		
		JButton start = new JButton(getStartAction());
		panel.add(start);
		

		JButton apply = new JButton(getApplyAction());
		panel.add(apply);
		
		JButton applyTen = new JButton(getApplyTenAction());
		panel.add(applyTen);


		JButton handle = new JButton(getHandleAction());
		panel.add(handle);
		
		
		View.setComponentSize(lab, 120, 25);
		View.setComponentSize(start,120, 25);
		View.setComponentSize(apply, 120, 25);
		View.setComponentSize(applyTen, 120, 25);
		View.setComponentSize(handle, 120, 25);
		
		return panel;
	}

	/**
	 * The picture should be set only in painting panel - 
	 * it would be taken to fsm only on Start
	 */
	private AbstractAction getLoadAction() {
		AbstractAction load = new AbstractAction() {
			private static final long serialVersionUID = 1L;
	
			public void actionPerformed(ActionEvent arg0) {
				File file = chooseFile();
				if (file != null) {
					myPaintingPanel.clear();
					BufferedImage image = null;
					try {
						image = ImageIO.read(file);
						image = Sampler.getEssentialRectangle(image);
						myPaintingPanel.setImage(image);
						myPaintingPanel.repaint();
						myManager.setHandlingStarted(false);
						updateActions();
					} catch (IOException e) {
						return;
					}
				}
			}
		};
		load.putValue(AbstractAction.NAME, "Load");
		load.putValue(AbstractAction.SHORT_DESCRIPTION, "Load a .bmp picture");
		return load;
	}

	private File chooseFile() {
		myFileChooser.setVisible(true);
		int res = myFileChooser.showOpenDialog(this);

		if (res != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		return myFileChooser.getSelectedFile();
	}
	
	private AbstractAction getClearAction() {
		AbstractAction clear = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myManager.clear();
				myManager.setHandlingStarted(false);
				updateActions();
				myPaintingPanel.clear();
				myPaintingPanel.repaint();
			}

		};
		clear.putValue(AbstractAction.NAME, "Clear");
		clear.putValue(AbstractAction.SHORT_DESCRIPTION, "Clear painting area");
		return clear;
	}

	
	private AbstractAction getStartAction() {
		AbstractAction start = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myManager.setImage(myPaintingPanel.getImage());
				myManager.setHandlingStarted(true);
				myPaintingPanel.clear();
				myPaintingPanel.setColors(myManager.getColors());
				myPaintingPanel.repaint();
				updateActions();
			}

		};
		start.putValue(AbstractAction.NAME, "Start");
		start.putValue(AbstractAction.SHORT_DESCRIPTION, "Start image handling");
		
		myStartAction = start;
		return start;
	}
	
	private AbstractAction getApplyAction() {
		AbstractAction apply = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myManager.applyNextRule();
				myPaintingPanel.setColors(myManager.getColors());
				myPaintingPanel.repaint();
			}

		};
		apply.putValue(AbstractAction.NAME, "Apply rule");
		apply.putValue(AbstractAction.SHORT_DESCRIPTION, "Apply next rule");
		myTempActions.add(apply);
		return apply;
	}
	
	private AbstractAction getApplyTenAction() {
		AbstractAction apply = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				for(int i = 0; i < 10; i++){
				myManager.applyNextRule();
				}
				myPaintingPanel.setColors(myManager.getColors());
				myPaintingPanel.repaint();
			}

		};
		apply.putValue(AbstractAction.NAME, "Apply 10 rules");
		apply.putValue(AbstractAction.SHORT_DESCRIPTION, "Apply 10 rules");
		myTempActions.add(apply);
		return apply;
	}

	private AbstractAction getHandleAction() {
		AbstractAction handle = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myManager.handle();
				myPaintingPanel.setColors(myManager.getColors());
				myPaintingPanel.repaint();
			}

		};
		handle.putValue(AbstractAction.NAME, "Handle");
		handle.putValue(AbstractAction.SHORT_DESCRIPTION, "Execule all rules");
		myTempActions.add(handle);
		return handle;
	}
	
	private void updateActions(){
		for(AbstractAction act : myTempActions){
			act.setEnabled(myManager.isHandlingStareted());
		}
		myStartAction.setEnabled(!myManager.isHandlingStareted());
	}
}
