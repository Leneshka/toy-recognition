package org.amse.shElena.toyRec.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.amse.shElena.toyRec.Main;
import org.amse.shElena.toyRec.manager.IManager;

public class View extends JFrame {
	private static final long serialVersionUID = 1L;

	private PaintRecognitionTab myPaintTab;

	private MassRecognitionTab myMassRecTab;

	public View(IManager paintMan, IManager recMan) {
		this.setTitle("Recognition");
		JPanel c = new JPanel();
		c.setLayout(new BorderLayout());
		JTabbedPane tpanel = new JTabbedPane(JTabbedPane.TOP);

		myPaintTab = new PaintRecognitionTab(paintMan);
		tpanel.addTab("Paint", myPaintTab);

		myMassRecTab = new MassRecognitionTab(recMan);
		tpanel.addTab("Mass Recognition", myMassRecTab);

		setSize(myPaintTab.getWidth(), myPaintTab.getHeight() + 60);

		c.add(tpanel, BorderLayout.CENTER);
		setContentPane(c);

		JPanel visTab = new VisualizationTab();
		tpanel.addTab("Visualization", visTab);

		setSize(myPaintTab.getWidth(), myPaintTab.getHeight() + 60);

		c.add(tpanel, BorderLayout.CENTER);
		setContentPane(c);

		setResizable(false);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		WindowListener l = new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				myPaintTab.exit();
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

	public static void setIcon(String iconName, AbstractAction act) {
		URL u = Main.class.getResource("utils/icons/" + iconName);
		if (u != null) {
			act.putValue(AbstractAction.SMALL_ICON, new ImageIcon(u));
		}
	}
}
