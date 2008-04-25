package org.amse.shElena.toyRec.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;

public class PaintingPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final int MY_STROKE = 5;
	private static final int MY_RECTANGLE_STROKE = 1;


	private Collection<Point> myPoints;

	private int myRightRectangleBound;

	private int myLeftRectangleBound;

	private int myLowerRectangleBound;

	private int myUpperRectangleBound;

	private boolean myPaintRedRectangle;

	public PaintingPanel() {
		myPaintRedRectangle = false;
		myPoints = new LinkedList<Point>();
		setBackground(Color.white);
	//	setBorder(new LineBorder(Color.gray, 1));
		
		MouseInputListener listener = new MouseInputListener() {

			public void mouseClicked(MouseEvent e) {
				myPoints.add(e.getPoint());
				myPoints.add(e.getPoint());
				myPoints.add(null);
				repaint();
			};

			public void mouseEntered(MouseEvent e) {
			};

			public void mouseExited(MouseEvent e) {
			};

			public void mouseReleased(MouseEvent e) {
				myPoints.add(e.getPoint());
				myPoints.add(null);
				repaint();
			};

			public void mouseMoved(MouseEvent e) {
			};

			public void mouseDragged(MouseEvent e) {
				myPoints.add(e.getPoint());
				repaint();
			}

			public void mousePressed(MouseEvent e) {
				myPoints.add(e.getPoint());
				repaint();
			}
		};

		addMouseListener(listener);
		addMouseMotionListener(listener);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.GRAY);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		
		if (myPaintRedRectangle) {
			g.setColor(Color.red);
			((Graphics2D) g).setStroke(new BasicStroke(MY_RECTANGLE_STROKE));
			g.drawRect(myLeftRectangleBound, myLowerRectangleBound,
					myRightRectangleBound - myLeftRectangleBound + 1,
					myUpperRectangleBound - myLowerRectangleBound + 1);

		}
		
		g.setColor(Color.black);
		((Graphics2D) g).setStroke(new BasicStroke(MY_STROKE));
		Iterator<Point> it = myPoints.iterator();
		if (it.hasNext()) {
			Point firstPoint = it.next();
			for (Point p; it.hasNext();) {
				p = it.next();
				if (p != null) {
					g.drawLine(firstPoint.x, firstPoint.y, p.x, p.y);
					firstPoint = p;
				} else {
					if (it.hasNext()) {
						firstPoint = it.next();
					}
				}
			}
		}
	}

	public void clear() {
		myPoints.clear();
		myPaintRedRectangle = false;
		repaint();
	}

	/**
	 * Automatically removes red rectangle
	 * @return black-and-white image
	 */
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_BYTE_BINARY);
		boolean rectangle = myPaintRedRectangle;
		
		Graphics g = image.getGraphics();
		
		myPaintRedRectangle = false;
		paintComponent(g);
		myPaintRedRectangle = rectangle;
		
		return image;
	}

	public void paintRedRectangle(int right, int left, int upper, int lower) {
		myLeftRectangleBound = left;
		myLowerRectangleBound = lower;
		myRightRectangleBound = right;
		myUpperRectangleBound = upper;
		myPaintRedRectangle = true;
		repaint();
	}
	
	public void clearRedRectangle(){
		myPaintRedRectangle = false;
		repaint();
	}
}
