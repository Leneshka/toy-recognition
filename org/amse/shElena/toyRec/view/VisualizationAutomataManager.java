package org.amse.shElena.toyRec.view;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.amse.shElena.toyRec.cellAutomaton.CellAutomaton;

public class VisualizationAutomataManager {
	private CellAutomaton myAutomata;

	private VisualizationTab myTab;

	private JFileChooser myFileChooser;
	
	public VisualizationAutomataManager(VisualizationTab tab, int width,
			int height) {
		myTab = tab;
		myAutomata = new CellAutomaton(width, height);
	
		myFileChooser = new JFileChooser();
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				return (file.isDirectory() || file.getName().endsWith(".bmp"));
			}

			@Override
			public String getDescription() {
				return ".bmp files";
			}
		};

		myFileChooser.addChoosableFileFilter(filter);
		myFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		myFileChooser.setDialogTitle("Load");
		myFileChooser.setCurrentDirectory(new File("."));
	}

	private File chooseFile() {
		myFileChooser.setVisible(true);
		int res = myFileChooser.showOpenDialog(myTab);

		if (res != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		return myFileChooser.getSelectedFile();
	}

	public void setBlackPoint(int x, int y) {
		myAutomata.setBlackPoint(x, y);
	}

	public Color getColor(int i, int j) {
		return myAutomata.getColor(i, j);
	}

	public void clear() {
		myAutomata.clear();
	}

	public void applyNextRule() {
		myAutomata.applyNextRule();
	}

	public void loadPicture() {
		File file = chooseFile();
		if (file != null) {
			myAutomata.clear();
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				return;
			}

			image = getEssentialRectangle(image);
			
			myAutomata.setImage(image);
		}
	}
	
	public void handle(){
		myAutomata.handle();
	}
	
	public Color[][] getFactorization(){
		return myAutomata.getFactorization();
	}
	
	private BufferedImage getEssentialRectangle(BufferedImage image) {
		int right = getRightBorder(image);
		int left = getLeftBorder(image);
		int upper = getUpperBorder(image);
		int lower = getLowerBorder(image);
		return image.getSubimage(left, lower, Math.max(right - left + 1,1), Math.max(upper - lower + 1,1));
	}

	/**
	 * If the picture is totaly white, returns 0 Needed ridhtBorder = leftBorder
	 * in this case
	 */
	private int getRightBorder(BufferedImage image) {
		int i = image.getWidth() - 1;
		while (i > 0) {
			if (!columnIsWhite(i, image)) {
				return i;
			}
			i--;
		}
		return 0;
	}

	/**
	 * If the picture is totaly white, returns 0 Needed ridhtBorder = leftBorder
	 * in this case
	 */
	private int getLeftBorder(BufferedImage image) {
		int i = 0;
		while (i < image.getWidth()) {
			if (!columnIsWhite(i, image)) {
				return i;
			}
			i++;
		}
		return 0;
	}

	/**
	 * If the picture is totaly white, returns 0 Needed upperBorder =
	 * lowerBorder in this case
	 */
	private  int getUpperBorder(BufferedImage image) {
		int j = image.getHeight() - 1;
		while (j > 0) {
			if (!rowIsWhite(j, image)) {
				return j;
			}
			j--;
		}
		return 0;
	}

	/**
	 * If the picture is totaly white, returns 0 Needed upperBorder =
	 * lowerBorder in this case
	 */
	private  int getLowerBorder(BufferedImage image) {
		int j = 0;
		while (j < image.getHeight()) {
			if (!rowIsWhite(j, image)) {
				return j;
			}
			j++;
		}
		return 0;
	}

	private boolean rowIsWhite(int number, BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++) {
			if (image.getRGB(i, number) == -16777216) {
				return false;
			}
			
		/*	if (image.getRGB(i, number) != -16777216 && image.getRGB(i, number) != -1) {
				System.out.println(image.getRGB(i, number));
			}*/
			
		}
		return true;
	}

	private boolean columnIsWhite(int number, BufferedImage image) {
		for (int j = 0; j < image.getHeight(); j++) {
			if (image.getRGB(number, j) == -16777216) {
				return false;
			}
		}
		return true;
	}
}
