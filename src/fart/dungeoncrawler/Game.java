package fart.dungeoncrawler;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import Utils.Vector2;

import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.enums.*;
import fart.dungeoncrawler.gamestates.*;
import fart.dungeoncrawler.items.*;
import fart.dungeoncrawler.network.Server;
import fart.dungeoncrawler.network.messages.game.GamePositionMessage;

/**
 * The game class is the heart of the game. It initializes all game-states, loads the game,
 * runs the gameloop and updates/draws the current state. 
 * @author Felix
 *
 */
@SuppressWarnings("serial")
public class Game extends JPanel implements Runnable
{
	//Variables used for timing the GameLoop
	private long frameDelta;
	private long frameLast;
	private final long frameTime = 32l;
	private boolean isRunning = true;
	
	private Player player;
	public Player getPlayer() { return player; }
	
	private Tilemap map;
	public Tilemap getMap() { return map ; }
	
	private DynamicObjectManager manager;
	public DynamicObjectManager getDynamicManager() { return manager; }
	
	private StaticObjectManager sManager;
	public StaticObjectManager getStaticManager() { return sManager; }
	
	private Controller controller;
	public Controller getController() { return controller; }
	
	private CollisionDetector collision;
	public CollisionDetector getCollision() { return collision; }
	
	private Menu menu;
	public Menu getMenu() { return menu; }
	
	private HashMap<GameState, BaseGameState> states;
	private GameState currentState;
	public GameState getState() { return currentState; }
	private BaseGameState currentGameState;
	public BaseGameState getGameState() { return currentGameState; }
	
	private CheckPoint checkPoint = null;
	private String currentMapPath;
	
	private boolean isInNetwork;
	private boolean isServer;
	private byte playerID;
	private int numPlayers;
	private Vector2[] startPositions;
	
	//DEBUG
	private static final Vector2 PLAYER_START_POS = new Vector2(1 * Tilemap.TILE_SIZE, 13 * Tilemap.TILE_SIZE);
	public static boolean debugDraw = false;
	private ArrayList<Player> otherPlayers;
	private ArrayList<Player> allPlayers;
	
	/**
	 * Creates and initializes a new instance of the game. 
	 * @param ID ID of the player. Used in network-games
	 * @param numPlayers the number of players. Used in network-games.
	 * @param isServer indicates if the instance is created from the server. 
	 */
	public Game(byte ID, int numPlayers, boolean isServer)
	{
		super();
		
		this.playerID = ID;
		this.numPlayers = numPlayers;
		this.isServer = isServer;
		if(!isServer) {
			setFocusable(true);
			requestFocusInWindow();
		} else {
			setVisible(false);
		}
		
		initGame();
	}
	
	/**
	 * Returns a list of all players.
	 * @return
	 */
	public ArrayList<Player> getAllPlayers() {
		if(allPlayers == null) {
			allPlayers = new ArrayList<Player>();
			if(player != null)
				allPlayers.add(player);
			for(Player p : otherPlayers)
				allPlayers.add(p);
		}
		
		return allPlayers;
	}
	
	/**
	 * Returns the flag indicating if this instance was created from the server. 
	 * @return
	 */
	public boolean isServer() {
		return isServer;
	}
	
	/**
	 * Sets a flag indicating if a network-game is running.
	 * @param isInNetwork
	 */
	public void setInNetwork(boolean isInNetwork) {
		this.isInNetwork = isInNetwork;
		
		boolean updateLogic = true;
		if(isInNetwork && !isServer)
			updateLogic = false;
		manager.setUpdateLogic(updateLogic);
	}
	
	/**
	 * Returns if the game is a network-game. 
	 * @return
	 */
	public boolean isInNetwork() {
		return isInNetwork;
	}
	
	/**
	 * Initializes the game. The most important classes are instanciated and the game-states
	 * are created. 
	 */
	public void initGame()
	{
		frameLast = System.currentTimeMillis();
		
		if(!isServer) {
			controller = new Controller();
			this.addKeyListener(controller);
			menu = new Menu(this, controller);
		}
		
		manager = new DynamicObjectManager(this, true);
		sManager = new StaticObjectManager();
		collision = new CollisionDetector();
		map = new Tilemap(this, sManager, manager, collision);
		otherPlayers = new ArrayList<Player>();
		
		states = new HashMap<GameState, BaseGameState>();
		states.put(GameState.InMenu, new GameStateInMenu(this));
		states.put(GameState.InGame, new GameStateInGame(this));
		states.put(GameState.InShop, new GameStateInShop(this));
		states.put(GameState.InInventory, new GameStateInInventory(this));
		states.put(GameState.InConversation, new GameStateInConversation(this));
		states.put(GameState.InLobby, new GameStateInLobby(this));
		states.put(GameState.InStatsMenu, new GameStateInStatsMenu(this));
		states.put(GameState.InQuestLog, new GameStateInQuestLog(this));
		
		setGameState(GameState.InMenu);
		((GameStateInShop)states.get(GameState.InShop)).setCurrentShop(new Shop(controller));

		ItemCollection.createNewInstace();
		QuestCollection.getInstance();
		
		startPositions = new Vector2[4];
		startPositions[0] = new Vector2(27 * 32, 16 * 32);
		//startPositions[1] = new Vector2(27 * 32, 4 * 32);
		startPositions[1] = new Vector2(27 * 32, 13 * 32);
		startPositions[2] = new Vector2(4 * 32, 4 * 32);
		startPositions[3] = new Vector2(4 * 32, 16 * 32);
	}
	
	/**
	 * Creates all player-instances. 
	 */
	public void createPlayers() {
		allPlayers = new ArrayList<Player>();
		
		for(int i = 0; i < numPlayers; i++) {
			ActorDescription actDesc = new ActorDescription("res/player.png", 1, 0, new Stats(12, 8, 7, 8, 0, 8, 0), Heading.Up);
			Vector2 plPos = new Vector2(PLAYER_START_POS.x + i * 32 + i, PLAYER_START_POS.y);
			if(isInNetwork)
				plPos = new Vector2(startPositions[i]);
			
			if(!isServer && playerID == i) {
				if(isInNetwork)
					player = new Player(this, actDesc, plPos, true, playerID);
				else
					player = new Player(this, actDesc, plPos, true);
				allPlayers.add(player);
				continue;
			}
			
			Player p = new Player(this, actDesc, plPos, false, i);
			allPlayers.add(p);
			otherPlayers.add(p);
		}
	}
	
	/**
	 * Creates all player-instances. 
	 * @param ID ID of the player that should be controlled
	 * @param numPlayers number of all players
	 */
	public void createPlayers(byte ID, int numPlayers) {
		playerID = ID;
		this.numPlayers = numPlayers;
		
		createPlayers();
	}
	
	/**
	 * Sets a new game-state. 
	 * @param state the state to be set.
	 */
	public void setGameState(GameState state) {
		currentState = state;
		if(currentGameState != null)
			currentGameState.exit();
		
		currentGameState = states.get(state);
		currentGameState.activate();
	}
	
	/**
	 * Passes the actor to the shop that has opened it. 
	 * @param act
	 */
	public void setShopActor(Actor act) {
		((GameStateInShop)states.get(GameState.InShop)).setCurrentActor(act);
	}
	
	/**
	 * Starts a new game. 
	 * @param newGame
	 */
	public void startGame() {
		startGame("res/maps/L0R0.xml");
	}
	
	/**
	 * Starts the game and loads the given map. 
	 * @param mapPath
	 */
	public void startGame(String mapPath) {
		manager.clearObjects();
		sManager.clearObjects();
		collision.clearDynamicObjects();
		collision.clearTriggers();
		
		if(!isServer) {
			player.setInNetwork(isInNetwork);
			resetPlayer();
		}
		
		map.loadMap(mapPath);
		currentMapPath = mapPath;
		
		if(player != null) {
			collision.addDynamicObject(player);
			manager.addObject(player);
		}
		
		for(int i = 0; i < otherPlayers.size(); i++) {
			otherPlayers.get(i).setInNetwork(isInNetwork);
			collision.addDynamicObject(otherPlayers.get(i));
			manager.addObject(otherPlayers.get(i));
		}
	}
	
	/**
	 * This function is called when the player dies. It checks if a checkpoint was reached and if so loads
	 * the state from the checkpoint. Otherwise the menu is opened. 
	 */
	public void playerDead() {
		if(checkPoint != null) {
			if(checkPoint.load()) {
				saveCheckPoint(checkPoint);
				currentMapPath = checkPoint.getMapName();
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
	
	/**
	 * This function is called when a player dies in a network-game. The player is set back to his
	 * start-position, health and mana are restored and the game continues. 
	 * @param actorID
	 */
	public void playerDeadInNetwork(int actorID) {
		Player a = allPlayers.get(actorID);
		a.getHealth().fillHealth();
		a.getMana().fillMana();
		a.setScreenPosition(new Vector2(startPositions[actorID]));
		while(collision.isCollidingDynamic(a))
			a.setScreenPosition(new Vector2(a.getPosition().x + 1, a.getPosition().y));
		a.setState(DynamicObjectState.Idle);
		
		if(isServer) {
			Server.getInstance().broadcastMessage(new GamePositionMessage(a));
		}
	}
	
	/**
	 * Resets the player when starting a new game. 
	 */
	private void resetPlayer() {
		Vector2 plPos = new Vector2(PLAYER_START_POS.x + playerID * Tilemap.TILE_SIZE + playerID, PLAYER_START_POS.y);
		if(isInNetwork)
			plPos = new Vector2(startPositions[playerID]);
		
		player.setScreenPosition(plPos);
		player.getHealth().fillHealth();
		player.getMana().fillMana();
		player.setState(DynamicObjectState.Idle);
		player.setHeading(Heading.Up);
		player.setVelocity(Vector2.Zero);
	}
	
	/**
	 * Saves on a checkpoint. 
	 * @param cp
	 */
	public void saveCheckPoint(CheckPoint cp) {
		checkPoint = cp;
		cp.save(map);
	}
	
	/**
	 * This method is called when the player reaches the goal. 
	 */
	public void playerWins() {
		setGameState(GameState.InMenu);
		menu.setGameStarted(false);
	}
	
	/**
	 * This method is used to load a new map. 
	 * @param mapTo the path to the mapfile
	 * @param position the start-position of the player
	 */
	public void changeMap(String mapTo, Vector2 position) {
		map.loadMap(mapTo);
		player.setScreenPosition(new Vector2(position));
		manager.addObject(player);
		collision.addDynamicObject(player);
		currentMapPath = mapTo;
	}
	
	/**
	 * Returns the path to the mapfile of the current map. 
	 * @return
	 */
	public String getMapPath() {
		return currentMapPath;
	}
	
	/**
	 * Returns the name of the current map.
	 * @return
	 */
	public String getMapName() {
		return map.getName();
	}
	
	/**
	 * Starts a thread running the gameloop. 
	 */
	public void startGameLoop() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	/**
	 * This method contains the gameloop. The current game-state is updated and the window is asked
	 * to be repainted. To get a steady frametime the deltatime is calculated in every frame and the
	 * thread is put to sleep for (frameTime - deltaTime). 
	 */
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
			
			if(!isServer)
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
	
	/**
	 * Draws the window and the current game-state. 
	 */
	public void paintComponent(Graphics g)
	{
		if(!isServer) {
			super.paintComponent(g);
			
			Graphics2D g2d = (Graphics2D)g;
			currentGameState.draw(g2d);
		}
	}
}
