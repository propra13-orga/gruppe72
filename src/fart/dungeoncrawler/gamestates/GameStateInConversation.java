package fart.dungeoncrawler.gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import fart.dungeoncrawler.Controller;
import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.StaticObjectManager;
import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.actor.DynamicObjectManager;
import fart.dungeoncrawler.enums.GameState;

public class GameStateInConversation extends BaseGameState {
	//private Inventory inventory;
	//private Equipment equip;
	//private Stats stats;
	private String text;
	private Font font;
	private Color fontColor;
	private Color bgColor;
	private Rectangle bgRect;
	private Point textStart;
	
	private StaticObjectManager sManager;
	private DynamicObjectManager dManager;
	private Tilemap map;
	private Controller controller;

	public GameStateInConversation(Game game) {
		super(game);

		sManager = game.getStaticManager();
		dManager = game.getDynamicManager();
		map = game.getMap();
		controller = game.getController();
		
		init();
	}
	
	private void init() {
		font = new Font("Arial", 0x1, 16);
		fontColor = new Color(0.2f, 0.1f, 0.4f);
		bgColor = new Color(0.8f, 0.8f, 0.2f);
		bgRect = new Rectangle(0, 15 * 32, 32 * 32, 5 * 32);
		textStart = new Point(40, 16 * 32);
	}
	
	public void setCurrentDialog(String text) {
		this.text = text;
	}
	

	@Override
	public void update(float elapsed) {
		if(controller.justPressed(KeyEvent.VK_ENTER)) {
			game.setGameState(GameState.InGame);
			return;
		}
		
		dManager.update(elapsed);
	}

	@Override
	public void draw(Graphics2D graphics) {
		map.draw(graphics);
		sManager.draw(graphics);
		dManager.draw(graphics);
		
		graphics.setColor(bgColor);
		graphics.fillRect(bgRect.x, bgRect.y, (int)bgRect.getWidth(), (int)bgRect.getHeight());
		
		graphics.setColor(fontColor);
		graphics.setFont(font);
		graphics.drawString(text, textStart.x, textStart.y);
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
