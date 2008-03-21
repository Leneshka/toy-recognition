package org.amse.shElena.toyRec.view.paintActions;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.amse.shElena.toyRec.samples.ISample;
import org.amse.shElena.toyRec.view.PaintRecognitionTab;

public class AddSymbolAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private PaintRecognitionTab myTab;

	public AddSymbolAction(PaintRecognitionTab tab) {
		myTab = tab;

		putValue(AbstractAction.NAME, "Add");
		putValue(SHORT_DESCRIPTION, "Add a symbol to the base");
	}

	public void actionPerformed(ActionEvent arg0) {
		BufferedImage image = myTab.getPaintingPanel().getImage();

		String letter = JOptionPane
				.showInputDialog("Please enter a letter you would like to assign this sample to.");

		if(letter == null){
			return;
		}
		
		if (letter.length() == 0){
			JOptionPane.showMessageDialog(myTab,
					"A letter is necessary.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}


		if (letter.length() > 1) {
			JOptionPane.showMessageDialog(myTab,
					"Only a single letter is required.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		ISample s = myTab.getManager().learnSymbol(letter.charAt(0), image);
		myTab.getListModel().addElement(s);
		myTab.getList().setSelectedIndex(myTab.getListModel().getSize() - 1);
		//myTab.getPaintingPanel().clear();
		int right = myTab.getManager().getRightBorder(image);
		int left = myTab.getManager().getLeftBorder(image);
		int upper = myTab.getManager().getUpperBorder(image);
		int lower = myTab.getManager().getLowerBorder(image);
		myTab.getPaintingPanel().paintRedRectangle(right, left, upper, lower);
	}

}
