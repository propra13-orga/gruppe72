package fart.dungeoncrawler.items;

import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.enums.EquipSlot;

/**
 * This class represents an armor. It is worn at EquipSlot.Armor. 
 * @author Felix
 *
 */
public class Armor extends StatItem {

	public Armor(String name, String tooltip, String iconPath, int priceOnBuy,
			boolean consumed, Stats stats) {
		super(name, tooltip, iconPath, priceOnBuy, consumed, stats);
	}
	
	@Override
	public EquipSlot getSlot() {
		return EquipSlot.Armor;
	}
}
