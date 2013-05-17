package fart.dungeoncrawler;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPanel;

import fart.dungeoncrawler.enums.Heading;

@SuppressWarnings("serial")
public class Game extends JPanel implements Runnable
{
	//Variables used for timing the GameLoop
	private long frameDelta;
	private long frameLast;
	private final long frameTime = 32l;
	
	//temp. varis for GameState
	private boolean isRunning = true;
	
	private Player player;
	private Tilemap map;
	
	private Controller controller;
	
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
		CollisionDetector.map = map.rooms[0];
		
		player = new Player(new Point(1, 1));
		
		controller = new Controller();
		this.addKeyListener(controller);
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
			
			if(controller.isDownPressed())
				player.move(Heading.Down);
			else if(controller.isUpPressed())
				player.move(Heading.Up);
			else if(controller.isLeftPressed())
				player.move(Heading.Left);
			else if(controller.isRightPressed())
				player.move(Heading.Right);
			else
				player.stopMovement();
			
			player.update((float)frameSleep);
			repaint();
			
			try {
				Thread.sleep(frameSleep);
			//} catch (IllegalArgumentException | InterruptedException e) {
			// Java 7
			} catch (InterruptedException e) {
				System.err.print("Exception in GameLoop:\n");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
