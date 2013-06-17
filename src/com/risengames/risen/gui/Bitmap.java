package com.risengames.risen.gui;

public class Bitmap {
    public final int width;
    public final int height;
    public final int[] pixels;

    public Bitmap(int width, int height) {
        this.height = height;
        this.width = width;

        pixels = new int[width * height];
    }

    public void draw(Bitmap bitmap, int xo, int yo) {
        for (int y = 0; y < bitmap.height; y++) {
            int yPixel = y + yo;
            if (yPixel < 0 || yPixel >= height) continue;

            for (int x = 0; x < bitmap.width; x++) {
                int xPixel = x + xo;
                if (xPixel < 0 || xPixel >= width) continue;

                int src = bitmap.pixels[x+y*bitmap.width];
                
                pixels[xPixel+yPixel*width] = src;
            }
        }
    }
    
    public void clear() {
    	for (int i = 0; i < width*height; i++) {
    		pixels[i] = 0;
    	}
    }
}
