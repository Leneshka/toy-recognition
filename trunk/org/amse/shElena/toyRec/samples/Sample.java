package org.amse.shElena.toyRec.samples;


public class Sample implements ISample {

	private final Character mySymbol;

	private boolean[][] myImage;

	public Sample(Character symbol, boolean[][] image) {
		mySymbol = symbol;
		myImage = image;
	}
	
	public Sample(Character symbol, String image, int width, int height) {
		mySymbol = symbol;
		myImage = readImage(image, width, height);
	}
	
	/*public Sample(Character symbol, BufferedImage image) {
		mySymbol = symbol;
		
		int width = image.getWidth();
		int height = image.getHeight();
		myImage = new boolean[width][height];
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (image.getRGB(i, j) == -16777216) {
					myImage[i][j] = true;
				}
			}
		}		
	}
*/
	public boolean[][] getImage() {
		return  myImage;
	}

	public Character getSymbol() {
		return mySymbol;
	}

	public String writeImageToString() {
		StringBuffer buf = new StringBuffer();
		
		for(int j = 0; j < myImage[0].length; j++){
			for(int i = 0; i < myImage.length; i++){
				if(myImage[i][j]){
					buf.append("1");
				} else {
					buf.append("0");
				}
			}
		}
		
		return buf.toString();
	}

	private boolean[][] readImage(String string, int width, int height) {
		boolean[][] image = new boolean[width][height];
		
		int k = 0;
		
		for(int j = 0; j < height; j++){
			for(int i = 0; i < width; i++){
				if('0' == string.charAt(k)){
					image[i][j] = false;
				} else if('1' == string.charAt(k)){
					image[i][j] = true;
				} else {
					throw new RuntimeException("String " + string + 
							"contains forbidden symbol "+ string.charAt(k));
				}
				k++;
			}
		}
		return image;
	}
	
	public String toString(){
		return mySymbol.toString();
	}
}
