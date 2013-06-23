package fart.dungeoncrawler;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Utils.Vector2;

import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.enums.*;
import fart.dungeoncrawler.gamestates.*;
import fart.dungeoncrawler.npc.states.*;
import fart.dungeoncrawler.items.*;

@SuppressWarnings("serial")
public class Game extends JPanel implements Runnable
{
	//Variables used for timing the GameLoop
	private long frameDelta;
	private long frameLast;
	private final long frameTime = 32l;
	private boolean isRunning = true;
	
	private NewPlayer player;
	public NewPlayer getPlayer() { return player; }
	
	private Tilemap map;
	public Tilemap getMap() { return map ; }
	
	private DynamicObjectManager manager;
	public DynamicObjectManager getDynamicManager() { return manager; }
	
	private StaticObjectManager sManager;
	public StaticObjectManager getStaticManager() { return sManager; }
	
	private Controller controller;
	public Controller getController() { return controller; }
	
	private Collision collision;
	public Collision getCollision() { return collision; }
	
	private Menu menu;
	public Menu getMenu() { return menu; }
	
	private HashMap<GameState, BaseGameState> states;
	private GameState currentState;
	public GameState getState() { return currentState; }
	private BaseGameState currentGameState;
	public BaseGameState getGameState() { return currentGameState; }
	
	//DEBUG
	private MeleeEnemy e;
	private BossEnemy eboss;
	private static final Vector2 PLAYER_START_POS = new Vector2(1 * Tilemap.TILE_SIZE, 13 * Tilemap.TILE_SIZE);
	
	public Game()
	{
		super();
		setFocusable(true);
		requestFocusInWindow();
		initGame();
	}
	
	private void initGame()
	{
		controller = new Controller();
		frameLast = System.currentTimeMillis();
		this.addKeyListener(controller);
		
		menu = new Menu(this, controller);
		manager = new DynamicObjectManager(this);
		sManager = new StaticObjectManager();
		collision = new Collision();
		map = new Tilemap(this, sManager, manager, collision);
		
		ActorDescription actDesc = new ActorDescription(new Dimension(32, 32), 100, 100, new Stats(), Heading.Up);

		player = new NewPlayer(this, actDesc, PLAYER_START_POS);
		manager.addPlayer(player);
		collision.addDynamicObject(player);
		
		states = new HashMap<GameState, BaseGameState>();
		states.put(GameState.InMenu, new GameStateInMenu(this));
		states.put(GameState.InGame, new GameStateInGame(this));
		states.put(GameState.InShop, new GameStateInShop(this));
		states.put(GameState.InInventory, new GameStateInInventory(this));
		
		setGameState(GameState.InMenu);
		//setGameState(GameState.InShop);
		//Inventory debugInventory = new Inventory(controller);
		((GameStateInShop)states.get(GameState.InShop)).setCurrentShop(new Shop(controller));
		//((GameStateInShop)states.get(GameState.InShop)).setCurrentInventory(debugInventory);
		//((GameStateInInventory)states.get(GameState.InInventory)).setCurrentInventory(debugInventory);
		
		ItemCollection.createNewInstace();
	}
	
	public void setGameState(GameState state) {
		currentState = state;
		if(currentGameState != null)
			currentGameState.exit();
		
		currentGameState = states.get(state);
		currentGameState.activate();
	}
	
	public void startGame(boolean newGame) {
			manager.clearObjects();
			sManager.clearObjects();
			collision.clearDynamicObjects();
			collision.clearTriggers();
			map.loadMap("res/maps/L0R0.xml");
			resetPlayer();

			//DEBUG
			BufferedImage bi;
			try {
				bi = ImageIO.read(new File("res/player.png"));
				EnemyDescription ed = new EnemyDescription(false, bi, 96, 16, 3, 100, 100, null, null);
				//e = new MeleeEnemy(ed, collision, manager);
				ActorDescription actDesc = new ActorDescription(new Dimension(32, 32), 80, 80, new Stats(), Heading.Down);
				/*e = new MeleeEnemy(this, actDesc, new Vector2(90, 160), ed);
				EnemyStateMachine machine = new EnemyStateMachine(e, player);
				e.setMachine(machine);
				collision.addDynamicObject(e);
				manager.addObject(e);*/
				eboss = new BossEnemy(this, actDesc, new Vector2(90, 160), ed);
				EnemyStateMachine machine = new EnemyStateMachine(eboss, player);
				eboss.setMachine(machine);
				collision.addDynamicObject(eboss);
				manager.addObject(eboss);
			} catch(IOException e) {
				System.err.println("Couldn't load image!");
				System.exit(1);
			}
	}
	
	public void playerDead() {
		menu.setGameStarted(false);
		setGameState(GameState.InMenu);
		resetPlayer();
	}
	
	private void resetPlayer() {
		player.setScreenPosition(PLAYER_START_POS);
		player.getHealth().addHealth(10000);
		player.getMana().addMana(10000);
		player.setState(DynamicObjectState.Idle);
		player.setHeading(Heading.Up);
	}
	
	public void playerWins() {
		currentState = GameState.InMenu;
	}
	
	public void changeMap(String mapTo, Vector2 position) {
		map.loadMap(mapTo);
		player.setScreenPosition(position);
	}
	
	public void startGameLoop() {
		Thread thread = new Thread(this);
		thread.run();
		
		run();
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
			currentGameState.update(frameTime);
			
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
		currentGameState.draw(g2d);
	}
}
