package com.risengames.risen;

import com.risengames.risen.gui.Screen;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class RisenComponent extends Canvas implements Runnable {
	private static final long serialVersionUID = 6317203247867165879L;
	private static final int WIDTH = 320;
    private static final int HEIGHT = 240;
    private static final int SCALE = 2;

    private boolean running = false;
    private Thread thread;
    private Screen screen;
    private Game game;
    private BufferedImage bufferedImage;
    private int[] pixels;

    public RisenComponent() {
        Dimension size = new Dimension(WIDTH*SCALE, HEIGHT*SCALE);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        screen = new Screen(WIDTH, HEIGHT);
        game = new Game();

        bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
    }

    public static void main(String[] args) {
        RisenComponent game = new RisenComponent();

        JFrame frame = new JFrame("Risen");
        frame.add(game);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        game.start();
    }

    public synchronized void start() {
        if (running) return;
        running = true;

        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
    	int frames = 0;
    	double unprocessedSeconds = 0;
        long lastTime = System.nanoTime();
        double timePerTick = 1 / 60.0;
        int tickCount = 0;
        
        while (running) {
        	long now = System.nanoTime();
        	long passedTime = now - lastTime;
        	lastTime = now;
        	
        	if (passedTime < 0) passedTime = 0;
        	if (passedTime > 1000000000) passedTime = 1000000000;
        	
        	unprocessedSeconds += passedTime / 1000000000.0;
        	
        	while (unprocessedSeconds > timePerTick) {
        		tick();
        		unprocessedSeconds -= timePerTick;
        		tickCount++;
        		
        		if (tickCount % 60 == 0) {
        			System.out.println(frames + " fps");
        			lastTime += 1000;
        			frames = 0;
        		}
        	}
        	
        	render();
            frames++;   
        }
    }

    private void tick() {
        game.tick();
    }
    
    private void render() {
        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        
        screen.render(game);
        
        for(int i = 0; i < WIDTH*HEIGHT; i++) {
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(bufferedImage, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
        g.dispose();
        bs.show();
    }
}
