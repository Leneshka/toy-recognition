package org.amse.shElena.toyRec.view;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.amse.shElena.toyRec.algorithms.ComparisonResult;
import org.amse.shElena.toyRec.samples.ISample;

public class RecognizeSymbolAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private PaintRecognitionTab myTab;

	public RecognizeSymbolAction(PaintRecognitionTab tab) {
		myTab = tab;
		
		putValue(AbstractAction.NAME, "Recognize");
		putValue(SHORT_DESCRIPTION, "Recognize symbol");
	}

	public void actionPerformed(ActionEvent arg0) {
		BufferedImage image = myTab.getPaintingPanel().getImage();
		ISample sample = myTab.getManager().makeSample(' ', image);

		myTab.getSamplePainter().showSample(sample);
		
		List<ComparisonResult> result = myTab.getManager().recognizeImage(image);
		
		JOptionPane.showMessageDialog(myTab,
				makeReport(result), "Recognition result",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	private String makeReport(List<ComparisonResult> result){
		if (result.size() == 0){
			return "The symbol base is empty. No recognition is available.";
		} else {
			Collections.sort(result);
			
			StringBuffer s = new StringBuffer();
			
			s.append("The most similar character in database is "+result.get(0).getSymbol()+".\n");
			s.append("Total results:\n" +
					"symbols and their similarity\n");
			
			for (ComparisonResult cr: result){
				s.append(cr.getSymbol() + "    "+ (100 - cr.getDifference())+"%\n");
			}
			
			s.append("\n" + myTab.getManager().getAlgorithm().toString());
			
			return s.toString();
		}
	}
}
