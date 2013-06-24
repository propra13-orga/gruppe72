package fart.dungeoncrawler.items;

import java.awt.Graphics2D;
import java.util.HashMap;

import Utils.Vector2;

import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.enums.EquipSlot;
import fart.dungeoncrawler.gamestates.GameStateInShop;

public class Equipment implements IDrawable {
	private HashMap<EquipSlot, StatItem> items = new HashMap<EquipSlot, StatItem>();
	private int border = 8;
	private int iconSize = 32;
	private static final Vector2 START_POS = new Vector2(860, -12);
	private static final int ICON_X_POS = 972;
	
	public Equipment() {
	}
	
	public boolean isFree(EquipSlot slot) {
		return items.get(slot) == null;
	}
	
	/**
	 * Equips a new item based on the slot and returns the previously worn item.
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
	
	public StatItem unequipSlot(EquipSlot slot) {
		StatItem previous = items.get(slot);
		items.remove(slot);
		
		return previous;
	}

	@Override
	public void draw(Graphics2D graphics) {
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
