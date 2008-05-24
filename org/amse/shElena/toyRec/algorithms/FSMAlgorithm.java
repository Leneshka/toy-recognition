package org.amse.shElena.toyRec.algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.amse.shElena.toyRec.algorithms.fsm.CellFSM;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.samples.ISample;

public class FSMAlgorithm implements IAlgorithm {

	private CellFSM myAutomaton;

	private List<Description> myDescriptions;

	/**
	 * Unique orange point is considered central iff there is less than
	 * MY_CENTRAL_POINT_PERCENT portion of blue points above it.
	 */
	private static final int MY_CENTRAL_POINT_PERCENT = 61;

	public FSMAlgorithm() {
		myAutomaton = new CellFSM();
		myDescriptions = new ArrayList<Description>();
	}

	public List<ComparisonResult> getWholeResults(boolean[][] image) {
		Description description = getDescription(null, image);
		List<ComparisonResult> res = new ArrayList<ComparisonResult>();

		for (Description des : myDescriptions) {
			res.add(new ComparisonResult(des.getSymbol(), Math.abs(description
					.compareTo(des))));
		}

		return res;
	}

	private Description getDescription(Character symbol, boolean[][] image) {
		myAutomaton.setImage(image);
		myAutomaton.handle();
		Color[][] res = myAutomaton.getFactorization();

		int blue = 0;
		int orange = 0;
		// counter for blue points before(over) first orange point
		int upperBlue = 0;

		for (int j = 0; j < res[0].length; j++) {
			for (int i = 0; i < res.length; i++) {
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

		if (orange == 1) {
			boolean central = (upperBlue / blue < MY_CENTRAL_POINT_PERCENT);
			return new Description(symbol, orange, central, blue);
		} else {
			return new Description(symbol, orange, false, blue);
		}

	}

	public void learnBase(ISampleBase base) {
		myDescriptions.clear();

		for (ISample s : base) {
			myDescriptions.add(getDescription(s.getSymbol(), s.getImage()));
		}
	}

	@Override
	public String toString() {
		return "FSM algorithm";
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
				// 100% difference - different number of cycles
				return 100;
			} else {
				if (des.isOrangeCentral() != isCentral) {
					// 50% difference - different position of ! cycle
					return 50;
				} else {
					return blue - des.getBlueNumber();
				}
			}
		}

		private int compare(int orange, int blue, Description des) {
			if (des.getOrangeNumber() != orange) {
				// 100% difference - different number of cycles
				return 100;
			} else {
				return blue - des.getBlueNumber();
			}
		}
	}

}
