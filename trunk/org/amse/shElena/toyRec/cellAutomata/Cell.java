package org.amse.shElena.toyRec.cellAutomata;

import java.awt.Color;


public class Cell {
	private boolean[] myColors;
	private static CellColor[] MY_CELL_COLORS;
	private boolean myIsBlack;

	public Cell() {
		myIsBlack = false;
		MY_CELL_COLORS = CellColor.values();
		myColors = new boolean[MY_CELL_COLORS.length];
	}
	
	/*
	 * ordinal() of enum element returns
	 * number of this element in emum`s list.
	 * First number is 0.
	 */
	public void setColor(CellColor c){
		myColors[c.ordinal()] = true;
	}
	
	public void setBlack(){
		myIsBlack = true;
	}
	
	public boolean isBlack(){
		return myIsBlack;
	}
	
	public void removeColor(CellColor c){
		myColors[c.ordinal()] = false;
	}
	
	public boolean hasColor(CellColor c){
		return myColors[c.ordinal()];
	}
	
	public Color getMainColor(){
		boolean found = false;
		
		for(int i = 0; i < myColors.length && !found; i++){
			if(myColors[i]){
				return MY_CELL_COLORS[i].getColor();
			}
		}
		
		if(myIsBlack){
			return Color.BLACK;
		} else {
			return Color.WHITE;
		}
	}

	public void clear(){
		myIsBlack = false;
		clearMarks();
	}
	
	public void clearMarks(){
		for(int i = 0; i < myColors.length; i++){
			myColors[i] = false;
		};
	}
}
