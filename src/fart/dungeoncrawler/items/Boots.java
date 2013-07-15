package fart.dungeoncrawler.items;

import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.enums.EquipSlot;

/**
 * This class represents boots. Those are worn at EquipSlot.Boots
 * @author Felix
 *
 */
public class Boots extends StatItem {

	public Boots(String name, String tooltip, String iconPath, int priceOnBuy,
			boolean consumed, Stats stats) {
		super(name, tooltip, iconPath, priceOnBuy, consumed, stats);
	}
	
	@Override
	public EquipSlot getSlot() {
		return EquipSlot.Boots;
	}

	

}
