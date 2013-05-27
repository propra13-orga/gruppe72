package fart.dungeoncrawler;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPanel;

import fart.dungeoncrawler.enums.GameState;

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
	private Collision colDetector;
	
	private GameState state;
	private Menu menu;
	
	public Game()
	{
		super();
		setFocusable(true);
		requestFocusInWindow();
		initGame();
	}
	
	private void initGame()
	{
		state = GameState.InMenu;
		
		controller = new Controller();
		frameLast = System.currentTimeMillis();
		this.addKeyListener(controller);
		
		menu = new Menu(this, controller);
		
		startNewGame();
		//changeMap(2, new Point(2, 3));
	}
	
	public void startNewGame() {
		map = new Tilemap(this);
		colDetector = new Collision(map);
		
		player = new Player(new Point(1, 13), colDetector, controller, this);
		state = GameState.InGame;
	}
	
	public void changeMap(int room, Point playerPosition) {
		map.changeRoom(room);
		colDetector.changeMap(map);

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
			
			switch(state) {
			case InMenu:
				updateMenu(frameTime);
				break;
			case InGame:
				updateGame(frameTime);
				break;
			}
			
			repaint();
			
			try {
				Thread.sleep(frameSleep);
			} catch (IllegalArgumentException e) {
				System.err.print("Exception in GameLoop:\n");
				e.printStackTrace();
				System.exit(1);
			} catch (InterruptedException e) {
				System.err.print("Exception in GameLoop:\n");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		switch(state) {
		case InMenu:
			drawMenu(g2d);
			break;
		case InGame:
			drawGame(g2d);
			break;
		}
	}
	
	private void updateMenu(float elapsed) {
		menu.update(elapsed);
	}
	
	private void updateGame(float elapsed) {
		player.update(elapsed);
	}
	
	private void drawMenu(Graphics2D g2d) {
		menu.draw(g2d);
	}
	
	private void drawGame(Graphics2D g2d) {
		map.draw(g2d);
		player.draw(g2d);
	}
}
