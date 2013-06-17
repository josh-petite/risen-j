package com.risengames.risen.gui;

import com.risengames.risen.Game;

public class Bitmap3d extends Bitmap {
	private double[] zBuffer;
	
	public Bitmap3d(int width, int height) {
		super(width, height);
		
		zBuffer = new double[width*height];
	}

	public void render(Game game) {
		double xCam = 0;
		double yCam = 0;
		double zCam = 0;
		
		double rot = game.time/250.0;
		double rCos = Math.cos(rot);
		double rSin = Math.sin(rot);
		
		for(int y = 0; y < height; y++) {
			double yd = (y - height / 2.0) / height;
			double zd = (6 - zCam) / yd;
			
			if (yd < 0) {
				zd = (8 - zCam) / -yd;
			}
			
			for (int x = 0; x < width; x++) {
				double xd = (x - width / 2.0) / height;
				xd *= zd;
				
				double xx = xd*rCos+zd*rSin + xCam;
				double yy = zd*rCos-xd*rSin + yCam;
				
				// y-axis rotation
				int xPixel = (int) xx;
				int yPixel = (int) yy;
				
				if (xx < 0) xPixel--;
				if (yy < 0) yPixel--;
								
				zBuffer[x+y*width] = zd;
				pixels[x+y*width] = Art.floors.pixels[(xPixel & 15) + (yPixel & 15) * 256];
			}
		}
	}
	
	public void renderEffects() {
		for (int i = 0; i < width*height; i++) {
			int col = pixels[i];
			int brightness = (int) (70000 / (zBuffer[i] * zBuffer[i]));
			
			if(brightness < 0) brightness = 0;
			if(brightness > 255) brightness = 255;
			
			int r = (col>>16) &0xff;
			int g = (col>>8) &0xff;
			int b = (col) &0xff;
			
			r = r * brightness / 255;
			g = g * brightness / 255;
			b = b * brightness / 255;
			
			pixels[i] = r << 16 | g << 8 | b;
		}
	}
}
