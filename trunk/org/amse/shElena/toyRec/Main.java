package org.amse.shElena.toyRec;

import org.amse.shElena.toyRec.algorithms.SimpleComparisonAlgorithm;
import org.amse.shElena.toyRec.manager.Manager;
import org.amse.shElena.toyRec.view.View;


public class Main {
	public static void main(String[] args){
	/*	try {
			UIManager.setLookAndFeel(UIManager
					.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		*/
	/*	try {
			UIManager.setLookAndFeel(new MotifLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}*/
		
		Manager m = new Manager(5, 7, SimpleComparisonAlgorithm.getInstance());
		
		View v = new View(m);
		
		v.setVisible(true);
	}
}
