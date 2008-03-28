package org.amse.shElena.toyRec.samples;

import java.awt.image.BufferedImage;

public class SampleFactory {
	/*
	 * Parameters of net in sampling
	 */
	private int myPictureWidth;

	private int myPictureHeight;

	public SampleFactory(int width, int height) {
		myPictureWidth = width;
		myPictureHeight = height;
	}

	public void setPictureWidth(int width) {
		myPictureWidth = width;
	}

	public void setPictureHeight(int height) {
		myPictureHeight = height;
	}

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
	public ISample makeSample(Character symbol, BufferedImage image,
			int blackCoefficient) {

		boolean[] picture = new boolean[myPictureWidth * myPictureHeight];
		int width = image.getWidth();
		int height = image.getHeight();
		double bCoefficient = ((double)blackCoefficient)/100;

		double ratioWidth = ((double) width) / myPictureWidth;
		double ratioHeight = ((double) height) / myPictureHeight;

		for (int y = 0; y < myPictureHeight; y++) {
			for (int x = 0; x < myPictureWidth; x++) {
				int startX = (int) (x * ratioWidth);
				int finX = Math.max((int) ((x + 1) * ratioWidth), startX + 1);
				int startY = (int) (y * ratioHeight);
				int finY = Math.max((int) ((y + 1) * ratioHeight), startY + 1);

				int blacks = 0;

				for (int i = startX; i < finX; i++) {
					for (int j = startY; j < finY; j++) {
						if (image.getRGB(i, j) == -16777216) {
							blacks++;
						}
					}
				}

				int w = Math.max(finX - startX, 1);
				int h = Math.max(finY - startY, 1);

				int square = w * h;
				double coef = ((double) blacks) / square;
				picture[x + myPictureWidth * y] = (coef > bCoefficient);

			}
		}

		return new Sample(symbol, picture);
	}

	/**
	 * makeSample(symbol,image,blackCoefficient), where black coefficient is
	 * percent of black points in image
	 */
	public ISample makeRelativeSample(Character symbol, BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		int blacks = 0;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (image.getRGB(x, y) == -16777216) {
					blacks++;
				}
			}
		}

		int coefficient = (100 * blacks) / (width * height);

		return makeSample(symbol, image, coefficient);
	}

	/**
	 * 
	 * Throws exception if symbol.equals("") or if s.readPicture(string) throws
	 * it.
	 * 
	 * @param symbol
	 * @param string
	 * @return
	 */
	public static ISample parseSample(String symbol, String string) {
		ISample s = new Sample(symbol.charAt(0), null);
		s.readPicture(string);
		return s;
	}
}
