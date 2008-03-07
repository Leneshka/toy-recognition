package org.amse.shElena.toyRec.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ClearAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private View myView;

	public ClearAction(View view) {
		myView = view;

		putValue(AbstractAction.NAME, "Clear");
		putValue(SHORT_DESCRIPTION, "Clear painting area");
	}

	public void actionPerformed(ActionEvent arg0) {
		myView.getPaintingPanel().clear();
		myView.getSamplePainter().clear();
		myView.getSamplePainter().repaint();
	}
}
