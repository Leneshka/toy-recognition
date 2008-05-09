package org.amse.shElena.toyRec;

import org.amse.shElena.toyRec.manager.Manager;
import org.amse.shElena.toyRec.view.View;


public class Main {
	public static void main(String[] args){
	Manager m = new Manager();
		Manager m2 = new Manager();
		
		View v = new View(m, m2);
		
		v.setVisible(true);
	}
}
