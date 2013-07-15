package fart.dungeoncrawler.gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.network.Lobby;

/**
 * This state is activated when a player joins a networkgame. All players meet in the Lobby before
 * the game is started.
 * @author Felix
 *
 */
public class GameStateInLobby extends BaseGameState {

	public static Color bgColor = new Color(0.4f, 0.4f, 0.8f);
	public static Font font = new Font("Arial", 0x1, 16);
	public static Color fontColor = new Color(0.1f, 0.1f, 0.35f);
	
	private Lobby lobby;

	public GameStateInLobby(Game game) {
		super(game);
	}
	
	public void initLobby(Lobby lobby, boolean isServer) {
		this.lobby = lobby;
	}

	@Override
	public void update(float elapsed) {
		lobby.update();
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(bgColor);
		graphics.fillRect(0, 0, game.getWidth(), game.getHeight());
		
		lobby.draw(graphics);
	}

	@Override
	public void activate() {
	}

	@Override
	public void exit() {
	}

}
