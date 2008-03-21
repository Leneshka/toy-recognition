package org.amse.shElena.toyRec.algorithms;

public class ComparisonResult implements Comparable<ComparisonResult> {
	private char mySymbol;

	private int myDifference;

	public ComparisonResult(Character s, int k) {
		mySymbol = s;
		myDifference = k;
	}

	public char getSymbol() {
		return mySymbol;
	}

	public int getDifference() {
		return myDifference;
	}

	public int compareTo(ComparisonResult res) {
		int dif = myDifference - res.myDifference;
		if (dif > 0) {
			return 1;
		} else if (dif == 0) {
			return mySymbol - res.mySymbol;
		} else {
			return -1;
		}
	}

}
