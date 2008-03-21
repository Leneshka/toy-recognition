package org.amse.shElena.toyRec.view.paintActions;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;

import org.amse.shElena.toyRec.samples.ISample;
import org.amse.shElena.toyRec.view.PaintRecognitionTab;

public class ShowSampleAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private PaintRecognitionTab myTab;

	public ShowSampleAction(PaintRecognitionTab tab) {
		myTab = tab;

		putValue(AbstractAction.NAME, "Show sample");
		putValue(SHORT_DESCRIPTION, "Shows sample of symbol");
	}

	public void actionPerformed(ActionEvent arg0) {
		BufferedImage image = myTab.getPaintingPanel().getImage();
		ISample sample = myTab.getManager().makeSample(' ', image);

		myTab.getSamplePainter().showSample(sample);
		
		int right = myTab.getManager().getRightBorder(image);
		int left = myTab.getManager().getLeftBorder(image);
		int upper = myTab.getManager().getUpperBorder(image);
		int lower = myTab.getManager().getLowerBorder(image);
		myTab.getPaintingPanel().paintRedRectangle(right, left, upper, lower);
		
		// System.out.println(myView.getList().getSelectedIndex());
		// myView.getList().setSelectedIndex(-1);
		// myView.getList().repaint();
	}
}
