package org.amse.shElena.toyRec.view;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;

import org.amse.shElena.toyRec.samples.ISample;

public class ShowSampleAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private View myView;

	public ShowSampleAction(View view) {
		myView = view;

		putValue(AbstractAction.NAME, "Show sample");
		putValue(SHORT_DESCRIPTION, "Shows sample of symbol");
	}

	public void actionPerformed(ActionEvent arg0) {
		BufferedImage image = myView.getPaintingPanel().getImage();
		ISample sample = myView.getManager().makeSample(' ', image);

		myView.getSamplePainter().showSample(sample);
		// System.out.println(myView.getList().getSelectedIndex());
		// myView.getList().setSelectedIndex(-1);
		// myView.getList().repaint();
	}
}
