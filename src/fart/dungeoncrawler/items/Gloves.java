package fart.dungeoncrawler.items;

import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.enums.EquipSlot;

public class Gloves extends StatItem {

	public Gloves(String name, String tooltip, String iconPath, int priceOnBuy,
			boolean consumed, Stats stats) {
		super(name, tooltip, iconPath, priceOnBuy, consumed, stats);
	}
	
	@Override
	public EquipSlot getSlot() {
		return EquipSlot.Gloves;
	}

	

}
