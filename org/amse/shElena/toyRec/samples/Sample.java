package org.amse.shElena.toyRec.samples;

public class Sample implements ISample {

	private final Character mySymbol;

	private boolean[] myPicture;

	protected Sample(Character symbol, boolean[] picture) {
		mySymbol = symbol;
		myPicture = picture;
	}

	public boolean[] getPicture() {
		return myPicture;
	}

	public Character getSymbol() {
		return mySymbol;
	}

	public String writePictureToString() {
		StringBuffer buf = new StringBuffer();
		
		for(int i = 0; i < myPicture.length; i++){
			if(myPicture[i]){
				buf.append("1");
			} else {
				buf.append("0");
			}
		}
		
		return buf.toString();
	}

	public void readPicture(String string) {
		int length = string.length();
		myPicture = new boolean[length];
		
		for(int i = 0; i < length; i++){
			if('0' == string.charAt(i)){
				myPicture[i] = false;
			} else if('1' == string.charAt(i)){
				myPicture[i] = true;
			} else {
				throw new RuntimeException("String " + string + 
						"contains forbidden symbol "+ string.charAt(i));
			}
		}

	}
	
	public String toString(){
		return mySymbol.toString();
	}
}
