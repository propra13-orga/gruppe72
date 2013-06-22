package fart.dungeoncrawler.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Utils.Vector2;

import fart.dungeoncrawler.Controller;
import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.actor.Actor;

public class Inventory implements IDrawable, IUpdateable {
	public static final int ROWS = 4;
	public static final int COLUMNS = 4;
	public static final int MAX_ITEMS = ROWS * COLUMNS;
	public static final int BORDER_SIZE = 8;
	public static final Vector2 START_POS = new Vector2(300, 300);
	private BaseItem[] items;
	private int gold;
	private Controller controller;
	private IconController iconController;
	private Actor owner;
	private static Font font;
	
	public Inventory(Controller controller, Actor owner) {
		items = new BaseItem[MAX_ITEMS];
		this.controller = controller;
		iconController = new IconController(controller, START_POS, BORDER_SIZE, COLUMNS, ROWS);
		items[0] = ItemCollection.getInstance().getByID(0);
		items[11] = ItemCollection.getInstance().getByID(0);
		items[4] = ItemCollection.getInstance().getByID(0);
		this.owner = owner;
		font = new Font("Arial", 0, 12);
	}
	
	public void setGold(int amount) {
		gold = amount;
	}
	
	public void reduceGold(int amount) {
		gold -= amount;
	}
	
	public void addGold(int amount) {
		gold += amount;
	}

	public int getGold() {
		return gold;
	}
	
	public boolean addItem(BaseItem item) {
		for(int i = 0; i < MAX_ITEMS; i++) {
			if(items[i] == null) {
				items[i] = item;
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void update(float elapsed) {
		iconController.update(elapsed);
		
		if(controller.justPressed(KeyEvent.VK_ENTER)) {
			int index = iconController.getCurrentIndex();
			
			BaseItem item = items[index];
			if(item != null) {
				item.use(owner);
				if(item.isConsumed())
					items[index] = null;
			}
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(new Color(0.6f, 0.6f, 0.6f));
		BaseItem item;
		
		for(int i = 0; i < MAX_ITEMS; i++) {
			int x = i % COLUMNS;
			int xPos = x * Tilemap.TILE_SIZE;
			xPos += (x * BORDER_SIZE);
			xPos += START_POS.x;
			
			int y = i / COLUMNS;
			int yPos = y * Tilemap.TILE_SIZE;
			yPos += (y * BORDER_SIZE);
			yPos += START_POS.y;
			
			graphics.fillRect(xPos, yPos, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
			item = items[i];
			if(item != null) {
				graphics.drawImage(item.getIcon(), xPos, yPos, null);
			}
		}
		
		iconController.draw(graphics);
		
		graphics.setFont(font);
		graphics.setColor(new Color(0.6f, 0.8f, 0.0f));
		graphics.drawString("Gold: " + gold, 300, 286);
	}

}
