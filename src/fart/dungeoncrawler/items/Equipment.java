package fart.dungeoncrawler.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

import Utils.Vector2;

import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.enums.EquipSlot;
import fart.dungeoncrawler.gamestates.GameStateInShop;

/**
 * This class is responsible for all items that can be worn. It handles equipping and unequipping of items
 * and drawing them when the inventory is opened.
 * @author Felix
 *
 */
public class Equipment implements IDrawable {
	private HashMap<EquipSlot, StatItem> items = new HashMap<EquipSlot, StatItem>();
	private int border = 8;
	private int iconSize = 32;
	private static final Vector2 START_POS = new Vector2(860, -12);
	private static final int ICON_X_POS = 972;
	
	public Equipment() {
	}
	
	/**
	 * Returns if a given EquipSlot is free. 
	 * @param slot
	 * @return
	 */
	public boolean isFree(EquipSlot slot) {
		return items.get(slot) == null;
	}
	
	/**
	 * Equips a new item based on the slot and returns the previously worn item so that it can be put back 
	 * in the inventory.
	 * @param slot Slot to equip.
	 * @param item Item to equip.
	 * @return Previous item. 
	 */
	public StatItem equipItem(EquipSlot slot, StatItem item) {
		StatItem previous = items.get(slot);
		items.remove(slot);
		items.put(slot, item);
		
		return previous;
	}
	
	/**
	 * Unequips a given slot and returns the previously worn item so that it can be put back in the
	 * inventory.
	 * @param slot
	 * @return
	 */
	public StatItem unequipSlot(EquipSlot slot) {
		StatItem previous = items.get(slot);
		items.remove(slot);
		
		return previous;
	}
	
	/**
	 * Returns the currently worn weapon.
	 * @return
	 */
	public Weapon getWeapon() {
		return (Weapon)items.get(EquipSlot.Weapon);
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(new Color(0.8f, 0.8f, 0.4f));
		graphics.fillRect(852, 0, 400, 640);
		
		graphics.setFont(GameStateInShop.FONT);
		graphics.setColor(GameStateInShop.FONT_COLOR);
		
		EquipSlot[] slots = EquipSlot.values();
		for(int i = 1; i < slots.length; i++) {
			int ypos = (int)START_POS.y + i * (iconSize + border);
			graphics.drawString(slots[i].toString(), (int)START_POS.x, ypos);
			if(items.containsKey(slots[i])) {
				StatItem item = items.get(slots[i]);
				graphics.drawImage(item.getIcon(), ICON_X_POS, ypos - (iconSize / 2) - (border / 2), null);
			}
		}
	}
}
