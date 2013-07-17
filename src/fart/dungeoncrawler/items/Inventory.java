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
import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.enums.EquipSlot;

/**
 * This class represents an inventory and stores all items and the amount of gold in it.
 * When the player opens up the inventory, this class is responsible for drawing it.
 * With the IconController the player can select an item and use/dress it. 
 * @author Felix
 *
 */
public class Inventory implements IDrawable, IUpdateable {
	public static final int ROWS = 4;
	public static final int COLUMNS = 4;
	public static final int MAX_ITEMS = ROWS * COLUMNS;
	public static final int BORDER_SIZE = 8;
	public static final Vector2 START_POS = new Vector2(860, 474);
	private static final Font fontBig = new Font("Arial", 0x1, 16);
	private BaseItem[] items;
	private int gold;
	private Controller controller;
	private IconController iconController;
	private Actor owner;
	private boolean drawTooltip;
	
	/**
	 * Creates the inventory for a specific actor. If it is user-controlled, the controller is passed to the
	 * IconController for navigating through the inventory.
	 * @param controller Keyboard. Can be null.
	 * @param owner Owner of the inventory.
	 */
	public Inventory(Controller controller, Actor owner) {
		items = new BaseItem[MAX_ITEMS];
		this.controller = controller;
		iconController = new IconController(controller, START_POS, BORDER_SIZE, COLUMNS, ROWS);
		items[0] = ItemCollection.getInstance().getByID(0);
		this.owner = owner;
	}
	
	/**
	 * Sets a flag indicating if the tooltips should be drawn.
	 * @param draw
	 */
	public void setDrawTooltip(boolean draw) {
		drawTooltip = draw;
	}
	
	/**
	 * Sets a specific amount of gold.
	 * @param amount
	 */
	public void setGold(int amount) {
		gold = amount;
	}
	
	/**
	 * Reduces gold by amount. 
	 * @param amount
	 */
	public void reduceGold(int amount) {
		gold -= amount;
	}
	
	/**
	 * Adds amount of gold. 
	 * @param amount
	 */
	public void addGold(int amount) {
		gold += amount;
	}

	/**
	 * Returns the goldamount. 
	 * @return
	 */
	public int getGold() {
		return gold;
	}
	
	/**
	 * Tries to add an item to the inventory and returns if it was successful. 
	 * Returns false if the inventory already contains MAX_ITEMS
	 * @param item item to add
	 * @return
	 */
	public boolean addItem(BaseItem item) {
		for(int i = 0; i < MAX_ITEMS; i++) {
			if(items[i] == null) {
				items[i] = item;
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns if the item with the given ID is already in the inventory. 
	 * @param id
	 * @return
	 */
	public boolean containsItem(int id) {
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null)
				continue;
			
			if(items[i].getIndex() == id)
				return true;
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
				EquipSlot slot = item.getSlot();
				if(slot == EquipSlot.None) {
					item.use(owner);
					if(item.isConsumed())
						items[index] = null;
				} else {
					Equipment equip = owner.getEquipment();
					StatItem prev = equip.equipItem(slot, (StatItem)item);
					((StatItem)item).use(owner);
					items[index] = null;
					if(prev != null) {
						prev.unuse(owner);
						addItem(prev);
					}
				}
			}
			
			owner.getHealth().setMaxHealh(owner.getStats().getStamina() * Stats.HEALTH_PER_STAM);
			owner.getMana().setMaxMana(owner.getStats().getWill() * Stats.MANA_PER_WILL);
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setFont(fontBig);
		graphics.setColor(new Color(0.6f, 0.8f, 0.0f));
		graphics.drawString("Gold: " + gold, (int)START_POS.x, (int)START_POS.y - (BORDER_SIZE * 2));
		
		graphics.setColor(new Color(0.6f, 0.6f, 0.6f));
		BaseItem item;
		
		int index = iconController.getCurrentIndex();
		
		for(int i = 0; i < MAX_ITEMS; i++) {
			graphics.setColor(new Color(0.6f, 0.6f, 0.6f));
			
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
			
			if(drawTooltip && i == index && item != null) {
				item.drawToolTip(graphics, xPos, yPos);
			}
		}
		
		iconController.draw(graphics);
	}

}
