package org.amse.shElena.toyRec.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class VisualizationTab extends JPanel {
	private static final long serialVersionUID = 1L;

	private VisPanel myVisPanel;
	private FactorizationPanel myFPanel;

	private VisualizationAutomataManager myManager;

	private final int myWidth;

	private final int myHeight;

	private final int myRatio;
	
	private AbstractAction myFactorizeAction;

	public VisualizationTab() {
		myWidth = 60;
		myHeight = 80;
		myRatio = 3;
		myManager = new VisualizationAutomataManager(this, myWidth, myHeight);
		// setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(createPaintPanel());
	}

	private JPanel createPaintPanel() {
		JPanel panel = new JPanel();
	//	panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		myVisPanel = new VisPanel(myManager, myWidth, myHeight, myRatio);
		myVisPanel.setPreferredSize(new Dimension(myRatio * myWidth, myRatio
				* myHeight));
		myVisPanel.setBorder(new LineBorder(Color.black, 1));

		panel.add(myVisPanel);

		JPanel buttons = createButtonPanel();
		buttons.setPreferredSize(new Dimension(100, 150));
		panel.add(buttons);

		myFPanel = new FactorizationPanel();
		myFactorizeAction.setEnabled(false);
	
		setSize(myFPanel, new Dimension(70, 70));
		panel.add(myFPanel);

		return panel;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JButton clear = new JButton(getClearAction());
		panel.add(clear);

		JButton apply = new JButton(getApplyAction());
		panel.add(apply);

		JButton load = new JButton(getLoadAction());
		panel.add(load);

		JButton handle = new JButton(getHandleAction());
		panel.add(handle);
		
		JButton factor = new JButton(getFactorizeAction());
		panel.add(factor);
		
		Dimension d = new Dimension(100, 30);
		setSize(clear, d);
		setSize(apply, d);
		setSize(load, d);
		setSize(handle, d);
		setSize(factor, d);
	
		return panel;
	}
	
	private void setSize(Component c, Dimension d){
		c.setSize(d);
		c.setPreferredSize(d);
		c.setMaximumSize(d);
		c.setMinimumSize(d);
	}

	private AbstractAction getClearAction() {
		AbstractAction clear = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myManager.clear();
				myFactorizeAction.setEnabled(false);
				myVisPanel.repaint();
			}

		};
		clear.putValue(AbstractAction.NAME, "Clear");
		clear.putValue(AbstractAction.SHORT_DESCRIPTION, "Clear painting area");
		return clear;
	}

	private AbstractAction getApplyAction() {
		AbstractAction apply = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myManager.applyNextRule();
				//myFactorizeAction.setEnabled(true);
				myVisPanel.repaint();
			}

		};
		apply.putValue(AbstractAction.NAME, "Apply rule");
		apply.putValue(AbstractAction.SHORT_DESCRIPTION, "Apply next rule");
		return apply;
	}

	private AbstractAction getLoadAction() {
		AbstractAction load = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myManager.loadPicture();
				myFactorizeAction.setEnabled(false);
				myVisPanel.repaint();
			}

		};
		load.putValue(AbstractAction.NAME, "Load");
		load.putValue(AbstractAction.SHORT_DESCRIPTION, "Load a .bmp picture");
		return load;
	}

	private AbstractAction getHandleAction() {
		AbstractAction handle = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				myManager.handle();
				myFactorizeAction.setEnabled(true);
				myVisPanel.repaint();
			}

		};
		handle.putValue(AbstractAction.NAME, "Handle");
		handle.putValue(AbstractAction.SHORT_DESCRIPTION, "Execule all rules");
		return handle;
	}
	
	private AbstractAction getFactorizeAction() {
		myFactorizeAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				Color[][] pic = myManager.getFactorization();
				myFPanel.setPicture(pic);
				myFPanel.repaint();
			}

		};
		myFactorizeAction.putValue(AbstractAction.NAME, "Factorize");
		myFactorizeAction.putValue(AbstractAction.SHORT_DESCRIPTION, "Factorize result");
		return myFactorizeAction;
	}

	/*
	 * public VisPanel getVisPanel() { return myVisPanel; }
	 */

	private class FactorizationPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private Color[][] myPicture;

		private final int myRatio;

		public FactorizationPanel() {
			myRatio = 10;
		}

		public void setPicture(Color[][] pic) {
			myPicture = pic;
		}

		public void paint(Graphics g) {
			if (myPicture == null || myPicture.length == 0) {
				g.setColor(Color.white);
				g.fillRect(0, 0, 70 , 70) ;
				return;
			}

			int w = myPicture.length;
			int h = myPicture[0].length;
			
			int width = w * myRatio;
			int height = h * myRatio;
			
			VisualizationTab.this.setSize(this,new Dimension(width + 1, height + 1));

			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					g.setColor(myPicture[x][y]);
					g.fillRect(x * myRatio, y * myRatio, myRatio, myRatio);
				}
			}
			
			g.setColor(Color.black);
			g.drawRect(0, 0, width , height);

			for (int y = 0; y < h; y++)
				g.drawLine(0, y * myRatio,width, y * myRatio);
			for (int x = 0; x < w; x++)
				g.drawLine(x * myRatio, 0, x * myRatio,height);

		}
	}
}
