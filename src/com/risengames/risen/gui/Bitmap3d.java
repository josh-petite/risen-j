package com.risengames.risen.gui;

import java.util.Random;

import com.risengames.risen.Game;

public class Bitmap3d extends Bitmap {
	private double[] zBuffer;
	
	public Bitmap3d(int width, int height) {
		super(width, height);
		
		zBuffer = new double[width*height];
	}

	public void render(Game game) {
		double xCam = 1.5;
		double yCam = game.time % 100.0 / 100.0;
		double zCam = 0;
				
		double rot = 0;
		double rCos = Math.cos(rot);
		double rSin = Math.sin(rot);
		
		for(int y = 0; y < height; y++) {
			double yd = ((y+0.5) - height / 2.0) / height;
			double zd = (4 - zCam) / yd;
			
			if (yd < 0) {
				zd = (4 + zCam) / -yd;
			}
			
			for (int x = 0; x < width; x++) {
				double xd = (x - width / 2.0) / height*2;
				xd *= zd;
				
				double xx = xd*rCos+zd*rSin + (xCam+1)*16.0;
				double yy = zd*rCos-xd*rSin + yCam*8.0;
				
				// y-axis rotation
				int xPixel = (int) xx;
				int yPixel = (int) yy;
				
				if (xx < 0) 
					xPixel--;
				if (yy < 0) 
					yPixel--;
								
				zBuffer[x+y*width] = zd;
				pixels[x+y*width] = Art.floors.pixels[(xPixel & 15) + (yPixel & 15) * 256];
			}
		}
		
		Random random = new Random(100);
		System.out.println(yCam);
		for(int i = 0; i < 1000; i++) {
			double z = 1 - yCam;
			double x = random.nextDouble() * 2 - 1;
			double y = random.nextDouble() * 2 - 1;
					
			int xPixel = (int)(x / z/2 * height/2 + width/2);
			int yPixel = (int)(y / z/2 * height/2 + height/2);
			
			if (xPixel>=0 && yPixel >=0 && xPixel < width && yPixel < height) {
				pixels[xPixel+yPixel*width] = 0xff00ff;
			}
		}
	}
	
	public void projectPixel(double x, double y, Game game) {
		double z = 1;
		
		int xPixel = (int)(x / z/2 * height/2 + width/2);
		int yPixel = (int)(y / z/2 * height/2 + height/2);
		
		//xPixel += game.time / 100.0;
		
		if (xPixel>=0 && yPixel >=0 && xPixel < width && yPixel < height) {
			pixels[xPixel+yPixel*width] = 0xff00ff;
		}
	}
	
	public void renderEffects() {
		for (int i = 0; i < width*height; i++) {
			int col = pixels[i];
			int brightness = (int) (90000 / (zBuffer[i] * zBuffer[i]));
			
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
