package org.amse.shElena.toyRec.samples;

import java.awt.image.BufferedImage;

/**
 * Some useful methods to work with images and their presentations
 * in samples and some algorithms.
 */
public class Sampler {
	/***************************************************************************
	 * Creates a sample using data from view.
	 * 
	 * @param symbol -
	 *            drawn symbol
	 * @param image -
	 *            the picture of symbol
	 * @param blackCoefficient -
	 *            the sufficient percent of black points to consider point in
	 *            sample black. It is 1 to make black any area that has at least
	 *            one point, because considered areas are small.
	 * @return sample.
	 */
	public static boolean[] makeAlgorithmSample(boolean[][] image, int blackCoefficient,
			int sampleWidth, int sampleHeight) {

		boolean[] picture = new boolean[sampleWidth * sampleHeight];
		int imageWidth = image.length;
		int imageHeight = image[0].length;
		double bCoefficient = ((double) blackCoefficient) / 100;

		double ratioWidth = ((double) imageWidth) / sampleWidth;
		double ratioHeight = ((double) imageHeight) / sampleHeight;

		for (int y = 0; y < sampleHeight; y++) {
			for (int x = 0; x < sampleWidth; x++) {
				int startX = (int) (x * ratioWidth);
				int finX = Math.max((int) ((x + 1) * ratioWidth), startX + 1);
				int startY = (int) (y * ratioHeight);
				int finY = Math.max((int) ((y + 1) * ratioHeight), startY + 1);

				int blacks = 0;

				for (int i = startX; i < finX; i++) {
					for (int j = startY; j < finY; j++) {
						if (image[i][j] == true) {
							blacks++;
						}
					}
				}

				int w = Math.max(finX - startX, 1);
				int h = Math.max(finY - startY, 1);

				int square = w * h;
				double coef = ((double) blacks) / square;
				picture[x + sampleWidth * y] = (coef > bCoefficient);

			}
		}

		return picture;
	}

	/**
	 * makeSample(symbol,image,blackCoefficient), where black coefficient is
	 * percent of black points in image
	 */
	public static boolean[] makeAlgorithmRelativeSample(boolean[][] image,
			int sampleWidth, int sampleHeight) {
		int imageWidth = image.length;
		int imageHeight = image[0].length;

		int blacks = 0;

		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				if (image[x][y] == true) {
					blacks++;
				}
			}
		}

		int coefficient = (100 * blacks) / (imageWidth * imageHeight);

		return makeAlgorithmSample(image, coefficient, sampleWidth, sampleHeight);
	}

	public static boolean[][] makeSampleImage(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		boolean[][] img = new boolean[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (image.getRGB(i, j) == -16777216) {
					img[i][j] = true;
				}
			}
		}
		return img;
	}

	/**
	 * Math.max(right - left,1), Math.max(upper - lower,1) for case picture is
	 * totally white
	 * 
	 * @param image
	 * @return
	 */
	public static BufferedImage getEssentialRectangle(BufferedImage image) {
		int right = getRightBorder(image);
		int left = getLeftBorder(image);
		int upper = getUpperBorder(image);
		int lower = getLowerBorder(image);
		return image.getSubimage(left, lower, Math.max(right - left + 1, 1),
				Math.max(upper - lower + 1, 1));
	}

	/**
	 * If the picture is totaly white, returns 0 Needed ridhtBorder = leftBorder
	 * in this case
	 */
	public static int getRightBorder(BufferedImage image) {
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
	public static int getLeftBorder(BufferedImage image) {
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
	public static int getUpperBorder(BufferedImage image) {
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
	public static int getLowerBorder(BufferedImage image) {
		int j = 0;
		while (j < image.getHeight()) {
			if (!rowIsWhite(j, image)) {
				return j;
			}
			j++;
		}
		return 0;
	}

	private static boolean rowIsWhite(int number, BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++) {
			if (image.getRGB(i, number) == -16777216) {
				return false;
			}

		}
		return true;
	}

	private static boolean columnIsWhite(int number, BufferedImage image) {
		for (int j = 0; j < image.getHeight(); j++) {
			if (image.getRGB(number, j) == -16777216) {
				return false;
			}
		}
		return true;
	}

	public static ISample makeSample(Character symbol, BufferedImage image) {
		image = getEssentialRectangle(image);

		return new Sample(symbol, Sampler.makeSampleImage(image));
	}
}
