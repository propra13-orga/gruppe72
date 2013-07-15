package fart.dungeoncrawler.items;

import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.enums.EquipSlot;

/**
 * This class represents all kinds of potions. Potions cann restore HP and/or MP.
 * @author Felix
 *
 */
public class Potion extends BaseItem {
	private int healthRestore;
	private int manaRestore;
	
	/**
	 * Creates an instance of a potion.
	 * @param name name of the potion
	 * @param tooltip tooltip to be shown
	 * @param iconPath path to the icon
	 * @param priceOnBuy price then buying in a shop
	 * @param healthRestore amount of HP to be restored
	 * @param manaRestore amount of MP to be restored
	 */
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
