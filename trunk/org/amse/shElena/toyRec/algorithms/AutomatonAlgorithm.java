package org.amse.shElena.toyRec.algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.amse.shElena.toyRec.cellAutomaton.CellAutomaton;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class AutomatonAlgorithm extends Algorithm {
	// private ISampleBase myBase;

	private CellAutomaton myAutomaton;

	private List<Description> myDescriptions;

	/**
	 * Unique orange point is considered central iff there is less than
	 * MY_CENTRAL_POINT_PERCENT portion of blue points above it.
	 */
	private static final int MY_CENTRAL_POINT_PERCENT = 61;

	private static Algorithm myInstance = new AutomatonAlgorithm();

	private AutomatonAlgorithm() {
		myAutomaton = new CellAutomaton();
		myDescriptions = new ArrayList<Description>();
	}

	public static Algorithm getInstance() {
		return myInstance;
	}

	@Override
	public List<ComparisonResult> getWholeResults(boolean[] picture) {
		return null;
	}

	public List<ComparisonResult> getWholeResults(ISample sample) {
		Description description = getDescription(sample);
		List<ComparisonResult> res = new ArrayList<ComparisonResult>();

		for (Description des : myDescriptions) {
			res.add(new ComparisonResult(des.getSymbol(), Math.abs(description.compareTo(des))));
		}

		return res;
	}

	private Description getDescription(ISample sample) {
		myAutomaton.setHardImage(sample.getImage());
		myAutomaton.handle();
		Color[][] res = myAutomaton.getFactorization();

		int blue = 0;
		int orange = 0;
		// counter for blue points before(over) first orange point
		int upperBlue = 0;

		for (int j = 0; j < res[0].length; j++) {
			for (int i = 0; i < res.length; i++) {
				//System.out.println(res[i][j]);
				if (Color.BLUE.equals(res[i][j])) {
					blue++;
				} else if (Color.ORANGE.equals(res[i][j])) {
					orange++;
					if (orange == 1) {
						upperBlue = blue;
					}
				}
			}
		}
		
		//System.out.print(orange);

		if (orange == 1) {
			boolean central = (upperBlue / blue < MY_CENTRAL_POINT_PERCENT);
			return new Description(sample.getSymbol(), orange, central, blue);
		} else {
			return new Description(sample.getSymbol(), orange, false, blue);
		}

	}

	@Override
	public void learnBase(ISampleBase base) {
		myDescriptions.clear();

		for (ISample s : base) {
			myDescriptions.add(getDescription(s));
		}
	}

	@Override
	public String toString() {
		return "Automaton algorithm";
	}

	private static class Description implements Comparable<Description> {
		private Character mySymbol;

		private int myOrangeNumber;

		private boolean myIsOrangeCentral;

		private int myBlueNumber;

		public Description(Character symbol, int orangeNumber,
				boolean isOrangeCentral, int blueNumber) {
			mySymbol = symbol;
			myOrangeNumber = orangeNumber;
			myIsOrangeCentral = isOrangeCentral;
			myBlueNumber = blueNumber;
		}

		public int getBlueNumber() {
			return myBlueNumber;
		}

		public boolean isOrangeCentral() {
			return myIsOrangeCentral;
		}

		public int getOrangeNumber() {
			return myOrangeNumber;
		}

		public Character getSymbol() {
			return mySymbol;
		}

		public int compareTo(Description des) {
			if (myOrangeNumber == 1) {
				return compare(myIsOrangeCentral, myBlueNumber, des);
			} else {
				return compare(myOrangeNumber, myBlueNumber, des);
			}
		}

		private int compare(boolean isCentral, int blue, Description des) {
			if (des.getOrangeNumber() != 1) {
				return 100;
			} else {
				if (des.isOrangeCentral() != isCentral) {
					return 50;
				} else {
					return blue - des.getBlueNumber();
				}
			}
		}

		private int compare(int orange, int blue, Description des) {
			if (des.getOrangeNumber() != orange) {
				return 100;
			} else {
				return blue - des.getBlueNumber();
			}
		}
	}

}
