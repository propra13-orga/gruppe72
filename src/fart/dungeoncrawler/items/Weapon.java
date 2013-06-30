package fart.dungeoncrawler.items;

import fart.dungeoncrawler.actor.ElementalDamage;
import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.enums.EquipSlot;

public class Weapon extends StatItem {
	private ElementalDamage eleDamage;
	private int attackSpeed;

	public Weapon(String name, String tooltip, String iconPath, int priceOnBuy,
			boolean consumed, Stats stats, ElementalDamage eleDamage, int attackSpeed) {
		super(name, tooltip, iconPath, priceOnBuy, consumed, stats);
		
		this.eleDamage = eleDamage;
		this.attackSpeed = attackSpeed;
	}
	
	@Override
	public EquipSlot getSlot() {
		return EquipSlot.Weapon;
	}

	public int getAttackSpeed() {
		return attackSpeed;
	}
	
	public ElementalDamage getEleDamage() {
		return eleDamage;
	}
}
