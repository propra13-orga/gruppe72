package fart.dungeoncrawler.gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.*;

import fart.dungeoncrawler.*;
import fart.dungeoncrawler.enums.GameState;
import fart.dungeoncrawler.network.*;

public class GameStateInMenu extends BaseGameState {
	private Menu menu;
	
	public GameStateInMenu(Game game) {
		super(game);

		menu = game.getMenu();
	}

	@Override
	public void activate() {
		
	}

	@Override
	public void exit() {
		
	}
	
	@Override
	public void update(float elapsed) {
		menu.update(elapsed);
		
		if(game.getController().justPressed(KeyEvent.VK_S)) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Type in your name: ");
			try {
				String name = reader.readLine();
				System.out.println("Starting server...");
				
				Server.createInstance();
				//Server server = Server.getInstance();
				Lobby lobby = new Lobby();
				Client client = new Client(name, lobby, game);
				client.start();
				lobby.setSelfClient(client);
				game.setGameState(GameState.InLobby);
				((GameStateInLobby)game.getGameState()).initLobby(lobby, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(game.getController().justPressed(KeyEvent.VK_J)) {
			System.out.println("Joining server...");
			Lobby lobby = new Lobby();
			Client client = new Client(lobby, game);
			lobby.setSelfClient(client);
			client.start();
			game.setGameState(GameState.InLobby);
			((GameStateInLobby)game.getGameState()).initLobby(lobby, false);
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		menu.draw(graphics);
	}

}
