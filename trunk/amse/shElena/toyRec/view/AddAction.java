package org.amse.shElena.toyRec.view;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.amse.shElena.toyRec.samples.ISample;

public class AddAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private View myView;

	public AddAction(View view) {
		myView = view;

		putValue(AbstractAction.NAME, "Add");
		putValue(SHORT_DESCRIPTION, "Add a symbol to the base");
	}

	public void actionPerformed(ActionEvent arg0) {
		BufferedImage image = myView.getPaintingPanel().getImage();

		String letter = JOptionPane
				.showInputDialog("Please enter a letter you would like to assign this sample to.");

		if(letter == null){
			return;
		}
		
		if (letter.length() == 0){
			JOptionPane.showMessageDialog(myView,
					"A letter is necessary.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}


		if (letter.length() > 1) {
			JOptionPane.showMessageDialog(myView,
					"Only a single letter is required.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		ISample s = myView.getManager().learnSymbol(letter.charAt(0), image);
		myView.getListModel().addElement(s);
		myView.getList().setSelectedIndex(myView.getListModel().getSize() - 1);
		myView.getPaintingPanel().clear();
	}

}
