package org.amse.shElena.toyRec.view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.amse.shElena.toyRec.manager.IManager;
import org.amse.shElena.toyRec.samples.ISample;

public class SamplePainter extends JPanel {
	private static final long serialVersionUID = 1L;

	private ISample mySample;

	private IManager myManager;

	public SamplePainter(IManager manager) {
		super();
		myManager = manager;
	}
	
	public void showSample(ISample sample){
		mySample = sample;
		repaint();
	}
	
	public void clear(){
		mySample = null;
		repaint();
	}

	public void paint(Graphics g) {
		int w = myManager.getWidth();
		int h = myManager.getHeight();

		int x, y;
		int vcell = getHeight() / h;
		int hcell = getWidth() / w;

		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.black);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		for (y = 0; y < h; y++)
			g.drawLine(0, y * vcell, getWidth(), y * vcell);
		for (x = 0; x < w; x++)
			g.drawLine(x * hcell, 0, x * hcell, getHeight());

		
		if (mySample == null){
			return;
		}

		boolean[] picture = mySample.getPicture();

		for (y = 0; y < h; y++) {
			for (x = 0; x < w; x++) {
				if (picture[x + y * w]) {
					g.fillRect(x * hcell, y * vcell, hcell, vcell);
				} 
			}
		}
	}
}
