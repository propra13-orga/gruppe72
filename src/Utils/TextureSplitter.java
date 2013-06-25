package Utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TextureSplitter {
	
	//Splits a texture into an array of BufferedImages. A whole row is split.
	public static BufferedImage[] splitTexture(BufferedImage source, int splitWidth, int splitHeight, int row) {
		int width = source.getWidth();
		int numSplits = width / splitWidth;
		BufferedImage[] result = new BufferedImage[numSplits];
		
		for(int i = 0; i < numSplits; i++) {
			result[i] = new BufferedImage(splitWidth, splitHeight, source.getType());
			Graphics2D graphics = result[i].createGraphics();
			
			graphics.drawImage(source, 
					0, 
					0, 
					splitWidth, 
					splitHeight, 
					splitWidth * i, 
					splitHeight * row, 
					splitWidth * i + splitWidth, 
					splitHeight * row + splitHeight, 
					null);
		}
		
		return result;
	}
}
