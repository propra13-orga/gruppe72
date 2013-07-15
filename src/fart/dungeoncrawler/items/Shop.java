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

/**
 * The Shop holds a list of all items that can be bought in it. It is not an NPC but only the
 * shop-element.
 * @author Felix
 *
 */
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
	
	/**
	 * Creates a shop. The controller is passed to an IconController which lets the player navigate through
	 * all items.
	 * @param controller keyboard
	 */
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
	
	/**
	 * Sets the inventory of the actor currently visiting the shop. 
	 * @param inventory
	 */
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
		int ttx = 0;
		int tty = 0;
		int index = iconController.getCurrentIndex();
		boolean dtt = false;
		
		for(int i = 0; i < maxItems; i++) {
			int x = i % columns;
			int xPos = x * Tilemap.TILE_SIZE;
			xPos += (x * borderSize);
			xPos += startPosition.x;
			
			int y = i / columns;
			int yPos = y * Tilemap.TILE_SIZE;
			yPos += (y * borderSize);
			yPos += startPosition.y;
			
			graphics.setColor(new Color(0.6f, 0.6f, 0.6f));
			graphics.fillRect(xPos, yPos, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
			
			if(i < items.size())
				graphics.drawImage(items.get(i).getIcon(), xPos, yPos, null);
			
			
			if(index == i) {
				if(index < items.size()) {
					ttx = xPos;
					tty = yPos;
					dtt = true;
				}
			}
		}
		
		if(dtt) {
			items.get(index).drawToolTip(graphics, ttx, tty);
		}
		
		iconController.draw(graphics);
	}
}
