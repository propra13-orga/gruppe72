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

public class Inventory implements IDrawable, IUpdateable {
	public static final int ROWS = 4;
	public static final int COLUMNS = 4;
	public static final int MAX_ITEMS = ROWS * COLUMNS;
	public static final int BORDER_SIZE = 8;
	public static final Vector2 START_POS = new Vector2(860, 474);
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
		this.owner = owner;
		font = new Font("Arial", 0x1, 12);
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
		graphics.setColor(new Color(0.2f, 0.2f, 0.1f));
		graphics.fillRect((int)START_POS.x - BORDER_SIZE, 0, 400, 640);
		
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
		graphics.drawString("Gold: " + gold, (int)START_POS.x, (int)START_POS.y - (BORDER_SIZE * 2));
	}

}