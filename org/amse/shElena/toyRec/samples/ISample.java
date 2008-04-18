package org.amse.shElena.toyRec.samples;

public interface ISample {
	Character getSymbol();

	boolean[] getPicture();

	String writePictureToString();

	void readPicture(String string);
}
