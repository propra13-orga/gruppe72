package fart.dungeoncrawler;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;

import javax.swing.JPanel;

import Utils.Vector2;

import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.enums.*;
import fart.dungeoncrawler.gamestates.*;
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
	
	private CheckPoint checkPoint = null;
	private String currentMap;
	
	//DEBUG
	private static final Vector2 PLAYER_START_POS = new Vector2(1 * Tilemap.TILE_SIZE, 13 * Tilemap.TILE_SIZE);
	public static boolean debugDraw = false;
	
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
		
		ActorDescription actDesc = new ActorDescription("res/player.png", 1, 0, new Stats(12, 8, 7, 8, 0, 8, 0), Heading.Up);

		player = new NewPlayer(this, actDesc, PLAYER_START_POS);
		collision.addDynamicObject(player);
		
		states = new HashMap<GameState, BaseGameState>();
		states.put(GameState.InMenu, new GameStateInMenu(this));
		states.put(GameState.InGame, new GameStateInGame(this));
		states.put(GameState.InShop, new GameStateInShop(this));
		states.put(GameState.InInventory, new GameStateInInventory(this));
		states.put(GameState.InConversation, new GameStateInConversation(this));
		
		setGameState(GameState.InMenu);
		((GameStateInShop)states.get(GameState.InShop)).setCurrentShop(new Shop(controller));
		

		ItemCollection.createNewInstace();
	}
	
	public void setGameState(GameState state) {
		currentState = state;
		if(currentGameState != null)
			currentGameState.exit();
		
		currentGameState = states.get(state);
		currentGameState.activate();
	}
	
	public void setShopActor(Actor act) {
		((GameStateInShop)states.get(GameState.InShop)).setCurrentActor(act);
	}
	
	public void startGame(boolean newGame) {
			manager.clearObjects();
			sManager.clearObjects();
			collision.clearDynamicObjects();
			collision.clearTriggers();
			map.loadMap("res/maps/L0R0.xml");
			currentMap = "res/maps/L0R0.xml";
			resetPlayer();

			//DEBUG
			//BufferedImage bi;

			//try {
				//bi = ImageIO.read(new File("res/player.png"));
				//EnemyDescription ed = new EnemyDescription(false, "res/player.png", 96, 16, 3, 100, 100, new Stats(8, 6, 5, 4, 55, 8, 0), Heading.Down);

				//BufferedImage si = ImageIO.read(new File("res/shop.png"));
			
			
				//ActorDescription actDesc = new ActorDescription("res/shop.png", 80, 80, new Stats(5, 5, 3, 1, 25, 8, 0), Heading.Down);
				//nshop = new NPCShop(this, /*actDesc, */new Vector2(32 * 11, 32), new NPCDescription("res/shop.png", NPCType.Shop.ordinal(), actDesc), new Rectangle(32 * 11 - 16, 64 - 16, 64, 64));
				//manager.addObject(nshop);
				//collision.addTriggerOnKey(nshop);
				//collision.addStaticObject(nshop.getCollisionRect());
				

				//eboss = new BossEnemy(this, new Vector2(90, 160), ed);
				//EnemyStateMachine machine = new EnemyStateMachine(eboss, player);
				//eboss.setMachine(machine);
				//collision.addDynamicObject(eboss);
				collision.addDynamicObject(player);
				manager.addObject(player);
				
				//MapItem mp = new MapItem(this, 4, new Vector2(164, 87));
				//NPCTalking talk = new NPCTalking(this, new Vector2(180, 87), new NPCDescription("res/shop.png", NPCType.Shop.ordinal(), actDesc), new Rectangle(180 - 16, 87 - 16, 64, 64));
				//sManager.addObject(talk);
				//collision.addTriggerOnKey(talk);
				
				
			//} catch(IOException e) {
			//	System.err.println("Couldn't load image!");
			//	System.exit(1);
			//}
			//ActorDescription actDesc = new ActorDescription("res/shop.png", 80, 80, new Stats(5, 5, 3, 1, 25, 8, 0), Heading.Down);
			
			//collision.addDynamicObject(player);
			//manager.addObject(player);
	}
	
	public void playerDead() {
		if(checkPoint != null) {
			if(checkPoint.load()) {
				saveCheckPoint(checkPoint);
				currentMap = checkPoint.getMapName();
				collision.addDynamicObject(player);
			}
			else {
				setGameState(GameState.InMenu);
				menu.setGameStarted(false);
				checkPoint = null;
			}
			
			return;
		}
		menu.setGameStarted(false);
		setGameState(GameState.InMenu);
		resetPlayer();
	}
	
	private void resetPlayer() {
		player.setScreenPosition(new Vector2(PLAYER_START_POS));
		player.getHealth().addHealth(10000);
		player.getMana().addMana(10000);
		player.setState(DynamicObjectState.Idle);
		player.setHeading(Heading.Up);
		player.setVelocity(Vector2.Zero);
	}
	
	public void saveCheckPoint(CheckPoint cp) {
		checkPoint = cp;
		cp.save(map);
	}
	
	public void playerWins() {
		setGameState(GameState.InMenu);
		menu.setGameStarted(false);
	}
	
	public void changeMap(String mapTo, Vector2 position) {
		map.loadMap(mapTo);
		player.setScreenPosition(new Vector2(position));
		manager.addObject(player);
		collision.addDynamicObject(player);
		currentMap = mapTo;
		//manager.addObject(nshop);
		//collision.addTriggerOnKey(nshop);
		//collision.addStaticObject(nshop.getCollisionRect());
	}
	
	public String getMapName() {
		return currentMap;
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
