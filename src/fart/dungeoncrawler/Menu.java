package fart.dungeoncrawler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


import Utils.Vector2;

import fart.dungeoncrawler.enums.GameState;
import fart.dungeoncrawler.gamestates.GameStateInLobby;
import fart.dungeoncrawler.items.IconController;
import fart.dungeoncrawler.mapeditor.MapEditor;
import fart.dungeoncrawler.network.Client;
import fart.dungeoncrawler.network.Lobby;
import fart.dungeoncrawler.network.Server;

/**
 * The game menu that is shown at the beginning. 
 * @author Erhan
 *
 */
public class Menu implements IDrawable, IUpdateable{
	private static Color bgColor = new Color(0.4f, 0.4f, 0.8f);
	private static Vector2 startPos = new Vector2(32 * 10, 32 * 5.5f);
	private static Font font = new Font("Arial", 0x1, 24);
	private static Color fontColor = new Color(0.1f, 0.1f, 0.35f);
	
	private Game game;
	private Controller controller;
	private IconController iconController;
	private ArrayList<MenuItem> items;
	private boolean isGameStarted;
	private boolean waitingForName;
	private boolean waitingForIP;
	private StringBuilder currentString;
	private String enteredName;
	private String enteredIP;
	
	/**
	 * Creates the menu. 
	 * @param game instance of the game
	 * @param controller keyboard controller
	 */
	public Menu(Game game, Controller controller) {
		this.game = game;
		this.controller = controller;
		
		items = new ArrayList<MenuItem>();
		isGameStarted = false;
		waitingForName = false;
		waitingForIP = false;
		currentString = new StringBuilder();
		enteredName = "";
		enteredIP = "";
		
		BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D)icon.getGraphics();
		g2d.setColor(new Color(0.75f, 0.75f, 0.75f));
		g2d.fillRect(0, 0, 32, 32);
		
		items.add(new MenuItem(icon, "Start Game", 0));
		items.add(new MenuItem(icon, "Host DeathMatch", 1));
		items.add(new MenuItem(icon, "Join DeathMatch", 2));
		items.add(new MenuItem(icon, "Start Editor", 3));
		items.add(new MenuItem(icon, "Exit", 4));
		
		iconController = new IconController(controller, startPos, 32, 1, 5);
	}
	
	/**
	 * Sets a flag if the game is started. 
	 * @param gameStarted
	 */
	public void setGameStarted(boolean gameStarted) {
		this.isGameStarted = gameStarted;
	}
	
	@Override
	public void update(float elapsed) {
		if(waitingForName) {
			for(int i = KeyEvent.VK_A; i <= KeyEvent.VK_Z; i++) {
				if(controller.justPressed(i)) {
					currentString.append((char)i);
					break;
				}
			}
		} else if (waitingForIP) {
			for(int i = KeyEvent.VK_0; i <= KeyEvent.VK_9; i++) {
				if(controller.justPressed(i)) {
					currentString.append((char)i);
					break;
				}
			}
			
			if(controller.justPressed(46)) {
				currentString.append(".");
			}
		}
		
		if (controller.justPressed(KeyEvent.VK_BACK_SPACE) && currentString.length() > 0)
			currentString.deleteCharAt(currentString.length() - 1);
		
		if (controller.justPressed(KeyEvent.VK_ENTER)) {
			if (waitingForName && waitingForIP) {
				enteredName = currentString.toString();
				currentString = new StringBuilder();
				waitingForName = false;
				return;
			} else if (waitingForName && !waitingForIP) {
				enteredName = currentString.toString();
				startServer();
				return;
			} else if (!waitingForName && waitingForIP) {
				enteredIP = currentString.toString();
				startClient();
				return;
			}
		}
		
		iconController.update(elapsed);
		
		if(controller.justPressed(KeyEvent.VK_ENTER)) {
			int index = iconController.getCurrentIndex();
			
			if(index == 0) {
				game.setInNetwork(false);
				game.createPlayers((byte) 0, 1);
				game.startGame();
				isGameStarted = true;
				game.setGameState(GameState.InGame);
			} else if (index == 1) {
				waitingForName();
			} else if (index == 2) {
				waitingForIP();
			} else if (index == 3) {
				new MapEditor(false);
			} else if (index == 4) {
				System.exit(0);
			}
		}
		
		if(isGameStarted && controller.justPressed(KeyEvent.VK_ESCAPE))
			game.setGameState(GameState.InGame);
	}
	
	/**
	 * Starts the server in a network-game.
	 */
	private void startServer() {
		Server.createInstance();
		Lobby lobby = new Lobby(controller, Server.isOnline());
		Client client = new Client(enteredName, lobby, game);
		client.start();
		lobby.setSelfClient(client);
		game.setGameState(GameState.InLobby);
		((GameStateInLobby)game.getGameState()).initLobby(lobby, true);
	}
	
	/**
	 * Starts a client in a network-game. 
	 */
	private void startClient() {
		Lobby lobby = new Lobby(controller, Server.isOnline());
		Client client = new Client(lobby, game, enteredIP, enteredName);
		lobby.setSelfClient(client);
		client.start();
		game.setGameState(GameState.InLobby);
		((GameStateInLobby)game.getGameState()).initLobby(lobby, false);
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(bgColor);
		graphics.fillRect(0, 0, game.getWidth(), game.getHeight());
		
		graphics.setFont(font);
		graphics.setColor(fontColor);
		
		if(waitingForName || waitingForIP) {
			String prompt = "";
			if(waitingForName)
				prompt = "Enter your name: ";
			else
				prompt = "Enter the server-IP: ";
			
			graphics.drawString(prompt + currentString.toString(), (int)startPos.x + 48, (int)startPos.y + 24);
		} else {
			for(int i = 0; i < items.size(); i++) {
				graphics.drawImage(items.get(i).getIcon(), (int)startPos.x, (int)startPos.y + i * 64, null);
				graphics.drawString(items.get(i).getText(), (int)startPos.x + 48, (int)startPos.y + i * 64 + 24);
			}
			
			iconController.draw(graphics);
		}
	}
	
	/**
	 * When going in a network-game every player has to enter a name. This method initializes
	 * the user input. 
	 */
	public void waitingForName() {
		enteredName = "";
		currentString = new StringBuilder();
		waitingForName = true;
	}
	
	/**
	 * When joining a network-game the player must type in the ip. This method initializes the
	 * user input. 
	 */
	public void waitingForIP() {
		enteredIP = "";
		currentString = new StringBuilder();
		waitingForName = true;
		waitingForIP = true;
	}
	
	/**
	 * This class stands for one item in the menu.
	 * @author Erhan
	 *
	 */
	class MenuItem {
		private BufferedImage icon;
		private String text;
		private int ID;
		
		/**
		 * Creates a MenuItem. 
		 * @param icon the icon to be shown
		 * @param text the text to be shown
		 * @param ID the index of this item
		 */
		MenuItem(BufferedImage icon, String text, int ID) {
			this.icon = icon;
			this.text = text;
			this.ID = ID;
		}
		
		/**
		 * Returns the icon.
		 * @return
		 */
		public BufferedImage getIcon() {
			return icon;
		}
		
		/**
		 * Returns the text. 
		 * @return
		 */
		public String getText() {
			return text;
		}
		
		/**
		 * Returns the ID. 
		 * @return
		 */
		public int getID() {
			return ID;
		}
	}
}
