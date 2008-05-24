package org.amse.shElena.toyRec.view;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.amse.shElena.toyRec.algorithms.fsm.CellFSM;
import org.amse.shElena.toyRec.samples.Sampler;

public class VisualizationAutomataManager {
	private CellFSM myAutomata;

	private boolean myHandlingStarted;

	public VisualizationAutomataManager() {
		myAutomata = new CellFSM();
		myHandlingStarted = false;
	}

	public void clear() {
		myAutomata.clear();
	}

	public void applyNextRule() {
		myAutomata.applyNextRule();
	}

	public void setImage(BufferedImage image) {
		myAutomata.setImage(Sampler.makeSampleImage(image));
	}

	public void handle() {
		myAutomata.handle();
	}

	public Color[][] getFactorization() {
		return myAutomata.getFactorization();
	}

	public boolean isHandlingStarted() {
		return myHandlingStarted;
	}

	public void setHandlingStarted(boolean started) {
		myHandlingStarted = started;
	}

	public int getWidth() {
		return myAutomata.getWidth();
	}

	public int getHeight() {
		return myAutomata.getHeight();
	}

	public Color[][] getColors() {
		return myAutomata.getColors();
	}
}
