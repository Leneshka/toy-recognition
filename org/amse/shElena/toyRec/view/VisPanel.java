package org.amse.shElena.toyRec.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class VisPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final int myHeight;

	private final int myWidth;

	private final int myRatio;

	private VisualizationAutomataManager myManager;

	public VisPanel(VisualizationAutomataManager manager, int width,
			int height, int ratio) {
		myManager = manager;
		myRatio = ratio;
		myHeight = height;
		myWidth = width;

		setBackground(Color.white);

		MouseInputListener listener = new MouseInputListener() {

			public void mouseClicked(MouseEvent e) {
				setBlackPoint(e.getPoint());
				repaint();
			};

			public void mouseEntered(MouseEvent e) {
			};

			public void mouseExited(MouseEvent e) {
			};

			public void mouseReleased(MouseEvent e) {
				setBlackPoint(e.getPoint());
				repaint();
			};

			public void mouseMoved(MouseEvent e) {
			};

			public void mouseDragged(MouseEvent e) {
				setBlackPoint(e.getPoint());
				repaint();
			}

			public void mousePressed(MouseEvent e) {
				setBlackPoint(e.getPoint());
				repaint();
			}
		};

		addMouseListener(listener);
		addMouseMotionListener(listener);
	}

	private void setBlackPoint(Point p) {
		int x = p.x / myRatio;
		int y = p.y / myRatio;

		int r = myRatio / 2;

		for (int i = 0; i < myRatio; i++) {
			for (int j = 0; j < myRatio; j++) {
				myManager.setBlackPoint(x - r + i, y - r + j);
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				g.setColor(myManager.getColor(i, j));
				g.fillRect(i * myRatio, j * myRatio, myRatio, myRatio);
			}
		}
	}
}
