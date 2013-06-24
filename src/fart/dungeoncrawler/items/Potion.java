package fart.dungeoncrawler.items;

import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.enums.EquipSlot;

public class Potion extends BaseItem {
	private int healthRestore;
	private int manaRestore;
	
	public Potion(String name, String tooltip, String iconPath, int priceOnBuy, int healthRestore, int manaRestore) {
		super(name, tooltip, iconPath, priceOnBuy, true);
		this.name = name;
		this.healthRestore = healthRestore;
		this.manaRestore = manaRestore;
		this.tooltip = tooltip;
	}
	
	@Override
	public EquipSlot getSlot() {
		return EquipSlot.None;
	}
	
	@Override
	public void use(Actor owner) {
		owner.getHealth().addHealth(healthRestore);
		owner.getMana().addMana(manaRestore);
	}
}
