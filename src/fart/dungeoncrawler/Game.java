package fart.dungeoncrawler;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Utils.Vector2;

import fart.dungeoncrawler.enums.GameState;
import fart.dungeoncrawler.enums.Heading;
import fart.dungeoncrawler.npc.EnemyDescription;
import fart.dungeoncrawler.npc.MeleeEnemy;
import fart.dungeoncrawler.npc.states.EnemyStateMachine;

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
	private boolean isGameStarted;
	private DynamicObjectManager manager;
	private StaticObjectManager sManager;
	
	//DEBUG
	private MeleeEnemy e;
	
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
		
		isGameStarted = false;
	}
	
	public void startGame(boolean newGame) {
		if(newGame) {
			manager = new DynamicObjectManager();
			sManager = new StaticObjectManager();
			map = new Tilemap(this, sManager, manager);
			colDetector = new Collision(map);
			
			player = new Player(new Vector2(1, 13), colDetector, controller, this, manager);
			colDetector.addDynamicObject(player);
			state = GameState.InGame;
			isGameStarted = true;
			
			//DEBUG
			BufferedImage bi;
			try {
				bi = ImageIO.read(new File("res/player.png"));
				EnemyDescription ed = new EnemyDescription(new Vector2(90, 160), new Dimension(32, 32), false, bi, Heading.Down, 4 * Tilemap.TILE_SIZE, 12, new Health(100, 30));
				e = new MeleeEnemy(ed, colDetector, manager);
				EnemyStateMachine machine = new EnemyStateMachine(e, player);
				e.setMachine(machine);
				colDetector.addDynamicObject(e);
				manager.addObject(e);
			} catch(IOException e) {
				System.err.println("Couldn't load image!");
				System.exit(1);
			}
			
		} else {
			state = GameState.InGame;
		}
	}
	
	public void playerDead() {
		isGameStarted = false;
		pauseResumeGame();
	}
	
	public void playerWins() {
		isGameStarted = false;
		state = GameState.InMenu;
	}
	
	public void pauseResumeGame() {
		if(state == GameState.InMenu) {
			startGame(false);
		} else
			state = GameState.InMenu;
	}
	
	public void changeMap(int room, Vector2 playerPosition) {
		map.changeRoom(room, sManager, manager);
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
			controller.update();
			if(controller.justPressed(KeyEvent.VK_ESCAPE))
				pauseResumeGame();
			
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
		manager.update(elapsed);
	}
	
	private void drawMenu(Graphics2D g2d) {
		menu.draw(g2d);
	}
	
	private void drawGame(Graphics2D g2d) {
		map.draw(g2d);
		sManager.draw(g2d);
		manager.draw(g2d);
	}
}
