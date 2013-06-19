package com.risengames.risen.gui;

import com.risengames.risen.Game;
import java.util.Random;

public class Screen extends Bitmap {
	private final int panelHeight = (int)(height * .25);
    private Bitmap gamePanel;
    private Bitmap3d viewport;
    private Bitmap testBitmap;

    public Screen(int width, int height) {
        super(width, height);

        gamePanel = new Bitmap(width, panelHeight);
        viewport = new Bitmap3d(width, height - panelHeight);
        testBitmap = new Bitmap(60,60);
        
        Random random = new Random();
        for(int i = 0; i < 60*60;i++) {
        	testBitmap.pixels[i] = random.nextInt() * random.nextInt(2);
        }
    }

    public void render(Game game) {
    	clearAll();
    	
    	for(int i = 0; i < 2; i++) {
	    	//int xo = (int) (Math.sin(game.time / 10000.0 * i * 2) * 100);
	    	//int yo = (int) (Math.cos(game.time / 10000.0 * i * 6) * 60);
    		int xo = (int) (Math.sin(game.time / 1000.0 * i * 4) * 128);
    		int yo = 0;
	        gamePanel.draw(testBitmap, width / 2 - 30 + xo, 0 + yo);
    	}
    	
    	viewport.render(game);
    	viewport.renderEffects();
    	
		Random r = new Random(100);
		
		for (int i=0; i < 10000; i++) {
			viewport.projectCircle(r.nextInt(0), r.nextInt(50));
			//viewport.projectPixel(r.nextDouble()*2-1, r.nextDouble()*2-1);
		}
    	
    	draw(viewport, 0, 0);
    	//draw(gamePanel, 0, height - panelHeight);
    }

	private void clearAll() {
		clear();
		gamePanel.clear();
		//viewport.clear();
	}
}
