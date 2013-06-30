package fart.dungeoncrawler.items;

import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.actor.Stats;

public abstract class StatItem extends BaseItem {
	protected Stats stats;
	
	public StatItem(String name, String tooltip, String iconPath,
			int priceOnBuy, boolean consumed, Stats stats) {
		super(name, tooltip, iconPath, priceOnBuy, consumed);
		this.stats = stats;
	}

	public Stats getStats() {
		return stats;
	}
	
	@Override
	public void use(Actor owner) {
		Stats s = owner.getStats();
		s.addStamina(stats.getStamina());
		s.addStrength(stats.getStrength());
		s.addAgility(stats.getAgility());
		s.addWill(stats.getWill());
		s.addArmor(stats.getArmor());
		/*s.setFireResistant(stats.getFireResistance());
		s.setWaterResistant(stats.getWaterResistance());
		s.setEarthResistant(stats.getEarthResistance());*/
		s.addDamage(stats.getDamage());
		s.addSpellDamage(stats.getSpellDamage());
	}
	
	public void unuse(Actor owner) {
		Stats s = owner.getStats();
		s.redStamina(stats.getStamina());
		s.redStrength(stats.getStrength());
		s.redAgility(stats.getAgility());
		s.redWill(stats.getWill());
		s.redArmor(stats.getArmor());
		/*s.setFireResistant(stats.getFireResistance());
		s.setWaterResistant(stats.getWaterResistance());
		s.setEarthResistant(stats.getEarthResistance());*/
		s.redDamage(stats.getDamage());
		s.redSpellDamage(stats.getSpellDamage());
	}
}
