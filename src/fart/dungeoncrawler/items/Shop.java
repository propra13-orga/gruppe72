package fart.dungeoncrawler.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Utils.Vector2;

import fart.dungeoncrawler.Controller;
import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.Tilemap;

public class Shop implements IDrawable, IUpdateable {
	private static Vector2 startPosition = new Vector2(50, 50);
	private static int borderSize = 8;
	private static int columns = 5;
	private static int rows = 6;
	private static int maxItems = rows * columns;
	
	private ArrayList<BaseItem> items;
	private Controller controller;
	private IconController iconController;
	private Inventory inventory;
	
	public Shop(Controller controller) {
		iconController = new IconController(controller, startPosition, borderSize, columns, rows);
		this.controller = controller;
		
		items = new ArrayList<BaseItem>();
		items.add(ItemCollection.getInstance().getByID(0));
		items.add(ItemCollection.getInstance().getByID(1));
		items.add(ItemCollection.getInstance().getByID(2));
		items.add(ItemCollection.getInstance().getByID(3));
		items.add(ItemCollection.getInstance().getByID(4));
		items.add(ItemCollection.getInstance().getByID(5));
		items.add(ItemCollection.getInstance().getByID(6));
		items.add(ItemCollection.getInstance().getByID(7));
		items.add(ItemCollection.getInstance().getByID(8));
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public void update(float elapsed) {
		iconController.update(elapsed);
		
		if(controller.justPressed(KeyEvent.VK_ENTER)) {
			int index = iconController.getCurrentIndex();
			if(index < items.size()) {
				BaseItem item = items.get(index);
				int price = item.getPrice();
				if(price <= inventory.getGold()) {
					if(inventory.addItem(items.get(index))) {
						inventory.reduceGold(price);
					}
				}
			}
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(new Color(0.6f, 0.6f, 0.6f));
		
		for(int i = 0; i < maxItems; i++) {
			int x = i % columns;
			int xPos = x * Tilemap.TILE_SIZE;
			xPos += (x * borderSize);
			xPos += startPosition.x;
			
			int y = i / columns;
			int yPos = y * Tilemap.TILE_SIZE;
			yPos += (y * borderSize);
			yPos += startPosition.y;
			
			graphics.fillRect(xPos, yPos, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
			
			if(i < items.size())
				graphics.drawImage(items.get(i).getIcon(), xPos, yPos, null);
		}
		
		iconController.draw(graphics);
	}
}
