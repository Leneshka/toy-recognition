package org.amse.shElena.toyRec.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.amse.shElena.toyRec.manager.IManager;

public class View extends JFrame {
	private static final long serialVersionUID = 1L;

	private PaintRecognitionTab myPaintTab;
	private MassRecognitionTab myMassRecTab;

	public View(IManager paintMan, IManager recMan) {
		JPanel c = new JPanel();
		c.setLayout(new BorderLayout());
		JTabbedPane tpanel = new JTabbedPane(JTabbedPane.TOP);

		myPaintTab = new PaintRecognitionTab(paintMan);
		tpanel.addTab("Paint", myPaintTab);
		// System.out.println(paint.getWidth());
		// System.out.println(paint.getHeight() + 130);
		
		myMassRecTab = new MassRecognitionTab(recMan);
		tpanel.addTab("Mass Recognition", myMassRecTab);
		
		setSize(myPaintTab.getWidth(), myPaintTab.getHeight() + 90);

		c.add(tpanel, BorderLayout.CENTER);
		setContentPane(c);

		setJMenuBar(myPaintTab.getMenu());

		setResizable(false);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		WindowListener l = new WindowAdapter() {

			public void windowClosing(WindowEvent event) {
				myPaintTab.getSavingManager().exit();
			}
		};
		addWindowListener(l);
	}
	
	public static void setComponentSize(JComponent comp, int width, int height) {
		Dimension d = new Dimension(width, height);
		comp.setMaximumSize(d);
		comp.setMinimumSize(d);
		comp.setPreferredSize(d);
	}
}
