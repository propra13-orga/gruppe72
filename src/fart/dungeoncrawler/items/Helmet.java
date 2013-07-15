package fart.dungeoncrawler.items;

import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.enums.EquipSlot;

/**
 * This class represents a helmet. It is worn at EquipSlot.Helmet. 
 * @author Felix
 *
 */
public class Helmet extends StatItem {

	public Helmet(String name, String tooltip, String iconPath, int priceOnBuy,
			boolean consumed, Stats stats) {
		super(name, tooltip, iconPath, priceOnBuy, consumed, stats);
	}
	
	@Override
	public EquipSlot getSlot() {
		return EquipSlot.Helmet;
	}

	

}
