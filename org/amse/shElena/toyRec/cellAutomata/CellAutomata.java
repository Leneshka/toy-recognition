package org.amse.shElena.toyRec.cellAutomata;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class CellAutomata {
	private int myHeight;

	private int myWidth;

	private Cell[][] myCells;

	private boolean[][] myToWorkUpPoints;

	private List<Integer> myRules;

	private boolean[] myRuleChanged;

	public CellAutomata(int width, int height) {
		myHeight = height;
		myWidth = width;
		myCells = new Cell[myWidth][myHeight];
		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				myCells[i][j] = new Cell();
			}
		}
		myToWorkUpPoints = new boolean[myWidth][myHeight];
		myRules = new ArrayList<Integer>();
		initializeRules();
		myRuleChanged = new boolean[15];
	}

	public void clear() {
		for (Cell[] cells : myCells) {
			for (Cell c : cells) {
				c.clear();
			}
		}

		initializeRules();
	}

	public void clearMarks() {
		for (Cell[] cells : myCells) {
			for (Cell c : cells) {
				c.clearMarks();
			}
		}

		initializeRules();
	}

	public int getWidth() {
		return myWidth;
	}

	public int getHeight() {
		return myHeight;
	}

	/*
	 * public void setSize(int width, int height) { myWidth = width; myHeight =
	 * height; for (int i = 0; i < myWidth; i++) { for (int j = 0; j < myHeight;
	 * j++) { myCells[i][j] = new Cell(); } } myToWorkUpPoints = new
	 * boolean[myWidth][myHeight]; initializeRules(); }
	 */

	public void setBlackPoint(int x, int y) {
		myCells[x][y].setBlack();
		clearMarks();
		initializeRules();
	}

	public Color getColor(int x, int y) {
		if (x < myWidth && y < myHeight) {
			return myCells[x][y].getMainColor();
		}
		return Color.WHITE;
	}

	public void setImage(BufferedImage image) {
		int width = Math.min(image.getWidth(), myWidth);
		int height = Math.min(myHeight, image.getHeight());
		clear();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (image.getRGB(i, j) == -16777216) {
					myCells[i][j].setBlack();
				}
			}
		}
		initializeRules();
	}

	public void applyRule(Integer number) {
		switch (number) {
		case 2:
			initAutomata();
			break;
		case 3:
			createTail();
			break;
		case 4:
			createFront();
			break;
		case 5:
			initGreens();
			break;
		case 6:
			workUpTail();
			break;
		case 7:
			workUpFront();
			break;
		case 8:
			firstChoice();
			break;
		case 9:
			removeGreens();
			break;
		case 10:
			secondChoice();
			break;
		case 11:
			markBlue();
			break;
		case 12:
			removeLowerMarks();
			break;
		case 13:
			removeRightMarks();
			break;
		case 14:
			lastChoice();
			break;
		}
	}

	public void applyNextRule() {
		if (!myRules.isEmpty()) {
			Integer r = myRules.remove(0);
			applyRule(r);
		}
	}

	public void handle() {
		while (!myRules.isEmpty()) {
			Integer r = myRules.remove(0);
			applyRule(r);
		}
	}

	public Color[][] getFactorization() {
		boolean[] w = new boolean[myWidth];
		boolean[] h = new boolean[myHeight];

		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				if (myCells[i][j].hasColor(CellColor.ORANGE)
						|| myCells[i][j].hasColor(CellColor.BLUE)) {
					w[i] = true;
					h[j] = true;
				}
			}
		}

		int wid = 0;
		for (int i = 0; i < myWidth; i++) {
			if (w[i]) {
				wid++;
			}
		}

		int hei = 0;
		for (int j = 0; j < myHeight; j++) {
			if (h[j]) {
				hei++;
			}
		}

		Color[][] res = new Color[wid][hei];
		wid = 0;
		hei = 0;
		for (int i = 0; i < myWidth; i++) {
			if (w[i]) {
				for (int j = 0; j < myHeight; j++) {
					if (h[j]) {
						if (myCells[i][j].hasColor(CellColor.ORANGE)) {
							res[wid][hei] = Color.ORANGE;
						} else if (myCells[i][j].hasColor(CellColor.BLUE)) {
							res[wid][hei] = Color.BLUE;
						} else {
							res[wid][hei] = Color.WHITE;
						}
						hei++;
					}
				}
				wid++;
				hei = 0;
			}
		}
		return res;
	}

	private void initializeRules() {
		myRules.clear();
		myRules.add(2);
	}

	private boolean rowIsWhite(int number) {
		for (int i = 0; i < myWidth; i++) {
			if (myCells[i][number].isBlack()) {
				return false;
			}
		}
		return true;
	}

	private void initAutomata() {
		myRules.add(3);
		for (int j = 0; j < myHeight; j++) {
			if (!rowIsWhite(j)) {
				for (int i = 0; i < myWidth; i++) {
					if (myCells[i][j].isBlack()) {
						myCells[i][j].setColor(CellColor.GRAY);
						myCells[i][j].setColor(CellColor.RED);
						return;
					}
				}
			}
		}
	}

	private void createTail() {
		myRules.add(4);
		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				if (myCells[i][j].hasColor(CellColor.RED)) {
					myCells[i][j].removeColor(CellColor.RED);
					myCells[i][j].setColor(CellColor.BLUE);
				}
			}
		}
	}

	/**
	 * ? what kind of neighbours should be taken
	 */
	private void createFront() {
		myRules.add(5);
		myRuleChanged[3] = false;
		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				myToWorkUpPoints[i][j] = false;
			}
		}
		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				if (myCells[i][j].isBlack()
						&& !myCells[i][j].hasColor(CellColor.GRAY)) {
					Cell c = hasNeighbour(i, j, CellColor.GRAY);
					// Cell c = hasCrossNeighbour(i, j, CellColor.GRAY);
					myToWorkUpPoints[i][j] = (c != null);
				}
			}
		}
		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				if (myToWorkUpPoints[i][j]) {
					myCells[i][j].setColor(CellColor.GRAY);
					myCells[i][j].setColor(CellColor.RED);
					myRuleChanged[3] = true;
				}
			}
		}
	}

	private void initGreens() {
		myRules.add(6);
		myRuleChanged[5] = false;
		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				if (myCells[i][j].hasColor(CellColor.RED)) {
					for (Cell cell : getCrossNeighbours(i, j)) {
						if (cell.hasColor(CellColor.BLUE)) {
							cell.setColor(CellColor.GREEN);
							/*
							 * removing Blue will be done during bluify(); too
							 * early removing spoils the idea of connectivity
							 * component
							 */
							// cell.removeColor(CellColor.BLUE);
							myRuleChanged[5] = true;
							return;
						}
					}
				}
			}
		}
	}

	/**
	 * Change blue to green in one connection component that contains green cell
	 */

	private void workUpTail() {
		myRules.add(7);
		/*
		 * for (int i = 0; i < myWidth; i++) { for (int j = 0; j < myHeight;
		 * j++) { myToWorkUpPoints[i][j] = false; } } for (int i = 0; i <
		 * myWidth; i++) { for (int j = 0; j < myHeight; j++) { if
		 * (myCells[i][j].hasColor(CellColor.BLUE)) { Cell c =
		 * hasCrossNeighbour(i, j, CellColor.GREEN); myToWorkUpPoints[i][j] = (c !=
		 * null); } } } for (int i = 0; i < myWidth; i++) { for (int j = 0; j <
		 * myHeight; j++) { if (myToWorkUpPoints[i][j]) {
		 * myCells[i][j].removeColor(CellColor.BLUE);
		 * myCells[i][j].setColor(CellColor.GREEN); } } }
		 */
		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				if (blueify(i, j)) {
					return;
				}
			}
		}
	}

	/**
	 * @return if the rule was applied
	 */
	private boolean blueify(int i, int j) {
		if (myCells[i][j].hasColor(CellColor.BLUE)) {
			if (myCells[i][j].hasColor(CellColor.GREEN)
					|| hasCrossNeighbour(i, j, CellColor.GREEN) != null) {
				myCells[i][j].setColor(CellColor.GREEN);
				myCells[i][j].removeColor(CellColor.BLUE);
				if (i != 0) {
					blueify(i - 1, j);
				}
				if (i != myWidth - 1) {
					blueify(i + 1, j);
				}
				if (j != 0) {
					blueify(i, j - 1);
				}
				if (j != myHeight - 1) {
					blueify(i, j + 1);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * хочу закрасить зеленым компоненту связности + что там надо
	 */
	private void workUpFront() {
		myRules.add(8);
		/*
		 * for (int i = 0; i < myWidth; i++) { for (int j = 0; j < myHeight;
		 * j++) { myToWorkUpPoints[i][j] = false; } }
		 * 
		 * for (int i = 0; i < myWidth; i++) { for (int j = 0; j < myHeight;
		 * j++) { if (myCells[i][j].hasColor(CellColor.RED) &&
		 * !myCells[i][j].hasColor(CellColor.GREEN)) { Cell c = hasNeighbour(i,
		 * j, CellColor.GREEN); myToWorkUpPoints[i][j] = (c != null); } } }
		 * 
		 * for (int i = 0; i < myWidth; i++) { for (int j = 0; j < myHeight;
		 * j++) { if (myToWorkUpPoints[i][j]) {
		 * myCells[i][j].setColor(CellColor.GREEN); } } }
		 */

		boolean done = false;

		for (int i = 0; i < myWidth & !done; i++) {
			for (int j = 0; j < myHeight && !done; j++) {
				done = greenify(i, j);
			}
		}

		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				if (myCells[i][j].hasColor(CellColor.RED)
						&& myCells[i][j].hasColor(CellColor.GREEN)) {
					Cell c = hasCrossNeighbour(i, j, CellColor.BLUE);
					if (c != null) {
						myCells[i][j].setColor(CellColor.ORANGE);
					}
				}
			}
		}
	}

	/**
	 * @return if rule was applied
	 */
	private boolean greenify(int i, int j) {
		if (myCells[i][j].hasColor(CellColor.RED)
				&& !myCells[i][j].hasColor(CellColor.GREEN)) {
			Cell c = hasCrossNeighbour(i, j, CellColor.GREEN);
			if (c != null) {
				myCells[i][j].setColor(CellColor.GREEN);
				if (i != 0) {
					greenify(i - 1, j);
				}
				if (i != myWidth - 1) {
					greenify(i + 1, j);
				}
				if (j != 0) {
					greenify(i, j - 1);
				}
				if (j != myHeight - 1) {
					greenify(i, j + 1);
				}
				return true;
			}
		}
		return false;
	}

	private void firstChoice() {
		if (myRuleChanged[5]) {
			myRules.add(5);
		} else {
			myRules.add(9);
		}
	}

	private void removeGreens() {
		myRules.add(10);
		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				if (myCells[i][j].hasColor(CellColor.GREEN)) {
					myCells[i][j].removeColor(CellColor.GREEN);
				}
			}
		}
	}

	private void secondChoice() {
		if (myRuleChanged[3]) {
			myRules.add(3);
		} else {
			myRules.add(11);
		}
	}

	private void markBlue() {
		myRules.add(12);
		for (int j = 0; j < myHeight; j++) {
			if (!rowIsWhite(j)) {
				for (int i = 0; i < myWidth; i++) {
					if (myCells[i][j].isBlack()) {
						myCells[i][j].setColor(CellColor.BLUE);
						return;
					}
				}
			}
		}
	}

	private void removeLowerMarks() {
		myRules.add(13);
		myRuleChanged[12] = false;

		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				myToWorkUpPoints[i][j] = false;
			}
		}

		for (int i = 1; i < myWidth - 1; i++) {
			for (int j = 0; j < myHeight - 1; j++) {
				if (myCells[i][j].hasColor(CellColor.BLUE)) {
					if (myCells[i][j + 1].hasColor(CellColor.BLUE)) {
						myToWorkUpPoints[i][j] = true;
					} else if (myCells[i + 1][j + 1].hasColor(CellColor.BLUE)
							&& !myCells[i - 1][j + 1].hasColor(CellColor.BLUE)) {
						myToWorkUpPoints[i][j] = true;
					} else if (!myCells[i + 1][j + 1].hasColor(CellColor.BLUE)
							&& myCells[i - 1][j + 1].hasColor(CellColor.BLUE)) {
						myToWorkUpPoints[i][j] = true;
					}
				}
			}
		}

		for (int j = 0; j < myHeight - 1; j++) {
			if (myCells[0][j].hasColor(CellColor.BLUE)) {
				if (myCells[0][j + 1].hasColor(CellColor.BLUE)) {
					myToWorkUpPoints[0][j] = true;
				} else if (myCells[1][j + 1].hasColor(CellColor.BLUE)) {
					myToWorkUpPoints[0][j] = true;
				}
			}
		}

		int l = myWidth - 1;
		for (int j = 0; j < myHeight - 1; j++) {
			if (myCells[l][j].hasColor(CellColor.BLUE)) {
				if (myCells[l][j + 1].hasColor(CellColor.BLUE)) {
					myToWorkUpPoints[l][j] = true;
				} else if (myCells[l - 1][j + 1].hasColor(CellColor.BLUE)) {
					myToWorkUpPoints[l][j] = true;
				}
			}
		}

		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				if (myToWorkUpPoints[i][j]) {
					myCells[i][j].removeColor(CellColor.BLUE);
					myRuleChanged[12] = true;
				}
			}
		}

		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				myToWorkUpPoints[i][j] = false;
			}
		}

		for (int i = 1; i < myWidth - 1; i++) {
			for (int j = 0; j < myHeight - 1; j++) {
				if (myCells[i][j].hasColor(CellColor.ORANGE)) {
					if (myCells[i][j + 1].hasColor(CellColor.ORANGE)) {
						myToWorkUpPoints[i][j] = true;
					} else if (i != myWidth
							&& myCells[i + 1][j + 1].hasColor(CellColor.ORANGE)
							&& !myCells[i - 1][j + 1]
									.hasColor(CellColor.ORANGE)) {
						myToWorkUpPoints[i][j] = true;
					} else if (!myCells[i + 1][j + 1]
							.hasColor(CellColor.ORANGE)
							&& myCells[i - 1][j + 1].hasColor(CellColor.ORANGE)) {
						myToWorkUpPoints[i][j] = true;
					}
				}
			}
		}

		for (int j = 0; j < myHeight - 1; j++) {
			if (myCells[0][j].hasColor(CellColor.ORANGE)) {
				if (myCells[0][j + 1].hasColor(CellColor.ORANGE)) {
					myToWorkUpPoints[0][j] = true;
				} else if (myCells[1][j + 1].hasColor(CellColor.ORANGE)) {
					myToWorkUpPoints[0][j] = true;
				}
			}
		}

		for (int j = 0; j < myHeight - 1; j++) {
			if (myCells[l][j].hasColor(CellColor.ORANGE)) {
				if (myCells[l][j + 1].hasColor(CellColor.ORANGE)) {
					myToWorkUpPoints[l][j] = true;
				} else if (myCells[l - 1][j + 1].hasColor(CellColor.ORANGE)) {
					myToWorkUpPoints[l][j] = true;
				}
			}
		}

		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				if (myToWorkUpPoints[i][j]) {
					myCells[i][j].removeColor(CellColor.ORANGE);
					myRuleChanged[12] = true;
				}
			}
		}
	}

	private void removeRightMarks() {
		myRules.add(14);
		myRuleChanged[13] = false;

		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				myToWorkUpPoints[i][j] = false;
			}
		}

		for (int i = 0; i < myWidth - 1; i++) {
			for (int j = 1; j < myHeight - 1; j++) {
				if (myCells[i][j].hasColor(CellColor.BLUE)) {
					if (myCells[i + 1][j].hasColor(CellColor.BLUE)) {
						myToWorkUpPoints[i][j] = true;
					} else if (myCells[i + 1][j + 1].hasColor(CellColor.BLUE)
							&& !myCells[i + 1][j - 1].hasColor(CellColor.BLUE)) {
						myToWorkUpPoints[i][j] = true;
					} else if (!myCells[i + 1][j + 1].hasColor(CellColor.BLUE)
							&& myCells[i + 1][j - 1].hasColor(CellColor.BLUE)) {
						myToWorkUpPoints[i][j] = true;
					}
				}
			}
		}

		for (int i = 0; i < myWidth - 1; i++) {
			if (myCells[i][0].hasColor(CellColor.BLUE)) {
				if (myCells[i + 1][0].hasColor(CellColor.BLUE)) {
					myToWorkUpPoints[i][0] = true;
				} else if (myCells[i + 1][1].hasColor(CellColor.BLUE)) {
					myToWorkUpPoints[i][0] = true;
				}
			}
		}

		int l = myHeight - 1;
		for (int i = 0; i < myWidth - 1; i++) {
			if (myCells[i][l].hasColor(CellColor.BLUE)) {
				if (myCells[i + 1][l].hasColor(CellColor.BLUE)) {
					myToWorkUpPoints[i][l] = true;
				} else if (myCells[i + 1][l - 1].hasColor(CellColor.BLUE)) {
					myToWorkUpPoints[i][l] = true;
				}
			}
		}

		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				if (myToWorkUpPoints[i][j]) {
					myCells[i][j].removeColor(CellColor.BLUE);
					myRuleChanged[13] = true;
				}
			}
		}

		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				myToWorkUpPoints[i][j] = false;
			}
		}

		for (int i = 0; i < myWidth - 1; i++) {
			for (int j = 1; j < myHeight - 1; j++) {
				if (myCells[i][j].hasColor(CellColor.ORANGE)) {
					if (myCells[i + 1][j].hasColor(CellColor.ORANGE)) {
						myToWorkUpPoints[i][j] = true;
					} else if (myCells[i + 1][j + 1].hasColor(CellColor.ORANGE)
							&& !myCells[i + 1][j - 1]
									.hasColor(CellColor.ORANGE)) {
						myToWorkUpPoints[i][j] = true;
					} else if (!myCells[i + 1][j + 1]
							.hasColor(CellColor.ORANGE)
							&& myCells[i + 1][j - 1].hasColor(CellColor.ORANGE)) {
						myToWorkUpPoints[i][j] = true;
					}
				}
			}
		}

		for (int i = 0; i < myWidth - 1; i++) {
			if (myCells[i][0].hasColor(CellColor.ORANGE)) {
				if (myCells[i + 1][0].hasColor(CellColor.ORANGE)) {
					myToWorkUpPoints[i][0] = true;
				} else if (myCells[i + 1][1].hasColor(CellColor.ORANGE)) {
					myToWorkUpPoints[i][0] = true;
				}
			}
		}

		l = myHeight - 1;
		for (int i = 0; i < myWidth - 1; i++) {
			if (myCells[i][l].hasColor(CellColor.ORANGE)) {
				if (myCells[i + 1][l].hasColor(CellColor.ORANGE)) {
					myToWorkUpPoints[i][l] = true;
				} else if (myCells[i + 1][l - 1].hasColor(CellColor.ORANGE)) {
					myToWorkUpPoints[i][l] = true;
				}
			}
		}

		for (int i = 0; i < myWidth; i++) {
			for (int j = 0; j < myHeight; j++) {
				if (myToWorkUpPoints[i][j]) {
					myCells[i][j].removeColor(CellColor.ORANGE);
					myRuleChanged[13] = true;
				}
			}
		}

	}

	private void lastChoice() {
		if (myRuleChanged[12] || myRuleChanged[13]) {
			myRules.add(12);
		}
	}

	/**
	 * @return any cross(upper, lower, right or left) neighdour of myCells[i][j]
	 *         with color c or null.
	 */
	private Cell hasCrossNeighbour(int i, int j, CellColor c) {
		if (i != 0) {
			if (myCells[i - 1][j].hasColor(c)) {
				return myCells[i - 1][j];
			}
		}

		if (i != myWidth - 1) {
			if (myCells[i + 1][j].hasColor(c)) {
				return myCells[i + 1][j];
			}
		}

		if (j != 0) {
			if (myCells[i][j - 1].hasColor(c)) {
				return myCells[i][j - 1];
			}
		}

		if (j != myHeight - 1) {
			if (myCells[i][j + 1].hasColor(c)) {
				return myCells[i][j + 1];
			}
		}
		return null;
	}

	/**
	 * @return all cross(upper, lower, right or left) neighdours of
	 *         myCells[i][j]
	 */
	private List<Cell> getCrossNeighbours(int i, int j) {
		List<Cell> list = new ArrayList<Cell>(4);
		if (i != 0) {
			list.add(myCells[i - 1][j]);
		}
		;
		if (i != myWidth - 1) {
			list.add(myCells[i + 1][j]);
		}
		;
		if (j != 0) {
			list.add(myCells[i][j - 1]);
		}
		;
		if (j != myHeight - 1) {
			list.add(myCells[i][j + 1]);
		}
		;

		return list;
	}

	/**
	 * @return all neighbours of myCells[i][j]
	 */
	/*
	 * private List<Cell> getNeighbours(int i, int j) { List<Cell> list = new
	 * ArrayList<Cell>(8); list.addAll(getCrossNeighbours(i, j)); if (i != 0 &&
	 * j != 0) { list.add(myCells[i - 1][j - 1]); } ; if (i != myWidth - 1 && j !=
	 * 0) { list.add(myCells[i + 1][j - 1]); } ; if (i != 0 && j != myHeight -
	 * 1) { list.add(myCells[i - 1][j + 1]); } ; if (i != myWidth - 1 && j !=
	 * myHeight - 1) { list.add(myCells[i + 1][j + 1]); } ;
	 * 
	 * return list; }
	 */

	/**
	 * @return any neighbour of myCells[i][j] with color c or null.
	 */
	private Cell hasNeighbour(int i, int j, CellColor c) {
		Cell cell = hasCrossNeighbour(i, j, c);
		if (cell != null) {
			return cell;
		}

		if (i != 0 && j != 0) {
			if (myCells[i - 1][j - 1].hasColor(c)) {
				return myCells[i][j];
			}
		}

		if (i != myWidth - 1 && j != 0) {
			if (myCells[i + 1][j - 1].hasColor(c)) {
				return myCells[i][j];
			}
		}

		if (i != myWidth - 1 && j != myHeight - 1) {
			if (myCells[i + 1][j + 1].hasColor(c)) {
				return myCells[i][j];
			}
		}

		if (i != 0 && j != myHeight - 1) {
			if (myCells[i - 1][j + 1].hasColor(c)) {
				return myCells[i][j];
			}
		}

		return null;
	}
}
