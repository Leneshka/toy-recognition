package org.amse.shElena.toyRec.view;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.amse.shElena.toyRec.manager.IManager;


public abstract class Tab extends JPanel{
	public abstract JMenuBar getMenu();
	
	public abstract IManager getManager();
	
	protected void setComponentSize(JComponent comp, int width, int height) {
		Dimension d = new Dimension(width, height);
		comp.setMaximumSize(d);
		comp.setMinimumSize(d);
		comp.setPreferredSize(d);
	}
}
