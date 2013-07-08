package fart.dungeoncrawler.gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import fart.dungeoncrawler.Controller;
import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.network.Lobby;
import fart.dungeoncrawler.network.messages.lobby.LobbyStartGameRequest;

public class GameStateInLobby extends BaseGameState {
	private boolean isServer;
	public static Font font;
	public static Color fontColor;
	
	private Lobby lobby;
	private Controller controller;

	public GameStateInLobby(Game game) {
		super(game);

		font = new Font("Arial", 0x1, 12);
		fontColor = Color.black;
		
		controller = game.getController();
	}
	
	public void initLobby(Lobby lobby, boolean isServer) {
		this.lobby = lobby;
		this.isServer = isServer;
	}

	@Override
	public void update(float elapsed) {
		if(controller.justPressed(KeyEvent.VK_R)) {
			lobby.changeReady();
		}
		
		if(isServer) {
			if(controller.justPressed(KeyEvent.VK_S)) {
				lobby.getSelfClient().sendMessage(new LobbyStartGameRequest());
			}
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(0, 0, game.getWidth(), game.getHeight());
		
		lobby.draw(graphics);
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

}
