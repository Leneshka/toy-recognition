package org.amse.shElena.toyRec.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ClearAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private PaintRecognitionTab myTab;

	public ClearAction(PaintRecognitionTab tab) {
		myTab = tab;

		putValue(AbstractAction.NAME, "Clear");
		putValue(SHORT_DESCRIPTION, "Clear painting area");
	}

	public void actionPerformed(ActionEvent arg0) {
		myTab.getPaintingPanel().clear();
		myTab.getSamplePainter().clear();
		myTab.getSamplePainter().repaint();
	}
}
