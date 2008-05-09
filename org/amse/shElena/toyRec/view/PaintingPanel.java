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
import javax.swing.event.MouseInputListener;

public class PaintingPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final int MY_STROKE = 3;

	private static final int MY_RECTANGLE_STROKE = 1;

	/**
	 * The coefficient size of everything is multiplied
	 */
	private int myScale = 5;

	private BufferedImage myImage;

	private Collection<Point> myPoints;

	private int myRightRectangleBound;

	private int myLeftRectangleBound;

	private int myLowerRectangleBound;

	private int myUpperRectangleBound;

	private boolean myPaintRedRectangle;

	private Color[][] myColors;

	public PaintingPanel() {
		myPaintRedRectangle = false;
		myPoints = new LinkedList<Point>();
		setBackground(Color.white);

		MouseInputListener listener = new MouseInputListener() {

			public void mouseClicked(MouseEvent e) {
				Point p = new Point(e.getPoint().x / myScale, e.getPoint().y
						/ myScale);
				myPoints.add(p);
				myPoints.add(p);
				myPoints.add(null);
				repaint();
			};

			public void mouseEntered(MouseEvent e) {
			};

			public void mouseExited(MouseEvent e) {
			};

			public void mouseReleased(MouseEvent e) {
				Point p = new Point(e.getPoint().x / myScale, e.getPoint().y
						/ myScale);
				myPoints.add(p);
				myPoints.add(null);
				repaint();
			};

			public void mouseMoved(MouseEvent e) {
			};

			public void mouseDragged(MouseEvent e) {
				Point p = new Point(e.getPoint().x / myScale, e.getPoint().y
						/ myScale);
				myPoints.add(p);
				repaint();
			}

			public void mousePressed(MouseEvent e) {
				Point p = new Point(e.getPoint().x / myScale, e.getPoint().y
						/ myScale);
				myPoints.add(p);
				repaint();
			}
		};

		addMouseListener(listener);
		addMouseMotionListener(listener);
	}

	public void setImage(BufferedImage image) {
		myImage = image;
	}

	public void setColors(Color[][] colors) {
		myColors = colors;
	}

	/**
	 * first drawn 1-to-1, image is drawn myScale times larger
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		BufferedImage image = new BufferedImage(getWidth() / myScale,
				getHeight() / myScale, BufferedImage.TYPE_INT_RGB);

		Graphics gr = image.getGraphics();

		drawRealPicture(gr);

		g.drawImage(image, 0, 0, image.getWidth() * myScale, image.getHeight()
				* myScale, null);
	}

	/**
	 * Draw image 1-to-1
	 * 
	 * @param g
	 */
	public void drawRealPicture(Graphics g) {
		super.paintComponent(g);

		if (myImage != null) {
			drawImage(g, myImage);
		}

		if (myColors != null) {
			for (int i = 0; i < myColors.length; i++) {
				for (int j = 0; j < myColors[0].length; j++) {
					g.setColor(myColors[i][j]);
					g.fillRect(i, j, i, j);
				}
			}
		}

		if (myPaintRedRectangle) {
			g.setColor(Color.red);
			((Graphics2D) g).setStroke(new BasicStroke(MY_RECTANGLE_STROKE));
			g.drawRect(myLeftRectangleBound, myLowerRectangleBound,
					myRightRectangleBound - myLeftRectangleBound,
					myUpperRectangleBound - myLowerRectangleBound);

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

	/*
	 * used to draw loaded image
	 */
	private void drawImage(Graphics g, BufferedImage image) {
		int w = myImage.getWidth();
		int h = myImage.getHeight();

		int panelW = getWidth() / myScale;
		int panelH = getHeight() / myScale;

		int scale = myScale;

		while (w * scale > panelW || h * scale > panelH) {
			scale--;
		}

		if (scale == 0) {
			g.drawImage(myImage, 0, 0, Math.min(panelW, w),
					Math.min(panelH, h), null);
		} else {
			int x = (panelW - myScale * w) / 2;
			int y = (panelH - myScale * h) / 2;

			g.drawImage(myImage, x, y, Math.min(panelW, w),
					Math.min(panelH, h), null);
		}
	}

	public void clear() {
		myPoints.clear();
		myPaintRedRectangle = false;
		myImage = null;
		myColors = null;
		repaint();
	}

	/**
	 * Automatically removes red rectangle
	 * 
	 * @return black-and-white image
	 */
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_BYTE_BINARY);
		boolean rectangle = myPaintRedRectangle;

		Graphics g = image.getGraphics();

		myPaintRedRectangle = false;

		drawRealPicture(g);

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
}
