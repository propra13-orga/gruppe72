package fart.dungeoncrawler.items;

import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.enums.EquipSlot;

/**
 * This class represents a ring that is worn at EquipSlot.Ring
 * @author Felix
 *
 */
public class Ring extends StatItem {

	public Ring(String name, String tooltip, String iconPath, int priceOnBuy,
			boolean consumed, Stats stats) {
		super(name, tooltip, iconPath, priceOnBuy, consumed, stats);
	}
	
	@Override
	public EquipSlot getSlot() {
		return EquipSlot.Ring;
	}

	

}
