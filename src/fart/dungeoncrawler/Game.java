package fart.dungeoncrawler;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import fart.dungeoncrawler.enums.Heading;

@SuppressWarnings("serial")
public class Game extends JPanel implements Runnable, KeyListener
{
	//Variables used for timing the GameLoop
	private long frameDelta;
	private long frameLast;
	private final long frameTime = 32l;
	
	//temp. varis for GameState
	private boolean isRunning = true;
	
	private Player player;
	private Tilemap map;
	
	public Game()
	{
		super();
		setFocusable(true);
		requestFocusInWindow();
		initGame();
	}
	
	private void initGame()
	{
		map = new Tilemap();
		CollisionDetector.map = map.room1;
		
		player = new Player(new Point(1, 1));
		
		this.addKeyListener(this);
		frameLast = System.currentTimeMillis();
	}
	
	public void startGameLoop() {
		Thread thread = new Thread(this);
		thread.run();
		
		run();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		map.draw(g2d);
		player.draw(g2d);
	}
	 
	public void run()
	{
		long frameAct;
		Long frameSleep;
		
		while(isRunning) {
			frameAct = System.currentTimeMillis();
			frameDelta = frameAct - frameLast;
			frameSleep = (frameTime - frameDelta);
			if(frameSleep < 2)
				frameSleep = 2l;
			//frameSleep = 14l;
			frameLast = frameAct;
			
			player.update((float)frameSleep);
			repaint();
			
			try {
				Thread.sleep(frameSleep);
			} catch (IllegalArgumentException | InterruptedException e) {
				System.err.print("Exception in GameLoop:\n");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			player.move(Heading.Down);
		if(e.getKeyCode() == KeyEvent.VK_UP)
			player.move(Heading.Up);
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			player.move(Heading.Left);
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			player.move(Heading.Right);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_DOWN || 
				code == KeyEvent.VK_UP || 
				code == KeyEvent.VK_LEFT || 
				code == KeyEvent.VK_RIGHT)
			player.stopMovement();
	}
}
