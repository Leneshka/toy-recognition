package org.amse.shElena.toyRec.samples;

import java.awt.image.BufferedImage;

public interface ISample {
	Character getSymbol();

	boolean[] getPicture();
	
	BufferedImage getImage();

	String writePictureToString();

	void readPicture(String string);
}
