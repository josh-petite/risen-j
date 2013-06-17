package com.risengames.risen.gui;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Art {
	public static Bitmap floors = loadBitmap("/tex/floors.png");
	
	public static Bitmap loadBitmap(String filename) {
		try {
			BufferedImage image = ImageIO.read(Art.class.getResource(filename));
			int w = image.getWidth();
			int h = image.getHeight();
			Bitmap result = new Bitmap(w,h);
			image.getRGB(0,0,w,h,result.pixels,0,w);
			return result;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
}
