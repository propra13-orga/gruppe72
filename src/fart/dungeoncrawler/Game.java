package fart.dungeoncrawler;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

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
	private ArrayList<Trap> traps;
	private ArrayList<Portal> portals;
	private Tilemap map;
	
	private Controller controller;
	private Collision colDetector;
	
	public Game()
	{
		super();
		setFocusable(true);
		requestFocusInWindow();
		initGame();
	}
	
	private void initGame()
	{
		map = new Tilemap(this);
		colDetector = new Collision(map);
		
		player = new Player(new Point(1, 13), colDetector);
		traps = new ArrayList<Trap>();
		portals = new ArrayList<Portal>();
		
		int[][] mapArray = map.getActRoom();
		
		for(int i=0; i<15; i++)
			for(int j=0; j<15; j++) 
				if((mapArray[i][j]&16) != 0) {
					traps.add(new Trap(new Point(i,j)));
				}
		
		controller = new Controller();
		frameLast = System.currentTimeMillis();
		this.addKeyListener(controller);
	}
	
	public void changeMap(int room, Point playerPosition) {
		map.changeRoom(room);
		colDetector.changeMap(map);
		traps.clear();
		int[][] mapArray = map.getActRoom();
		for(int i=0; i<15; i++)
			for(int j=0; j<15; j++)
				if((mapArray[i][j]&16) != 0)
					traps.add(new Trap(new Point(i,j)));
		player.setTilePosition(playerPosition);
	}
	
	public void startGameLoop() {
		Thread thread = new Thread(this);
		thread.run();
		
		run();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		map.draw(g2d);
		player.draw(g2d);
		
		for(int i = 0; i < traps.size(); i++)
			traps.get(i).draw(g2d);
		
		Iterator<Trap> it = traps.iterator();
		while(it.hasNext())
		{
			Trap tmp = it.next();
			tmp.draw(g2d);
		}
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
