package Utils;

import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.enums.MagicType;

public class DamageCalculator {
	private static final float MAX_STAT = 1000;
	private static final float MAX_MULT = 0.8f;
	private static final float RESISTANCE_MULT = 0.6f;
	
	public static float calcDamage(Actor attacker, Actor defender) {
		Stats aStats = attacker.getStats();
		Stats dStats = defender.getStats();
		
		float redByArmor = 1.0f - calcMultiplyerMax(dStats.getArmor());
		float dmgMult = 1.0f + calcMultiplyerMax(aStats.getStrength());
		float damage = aStats.getDamage() * dmgMult;
		
		return damage * redByArmor;
	}
	
	public static float calcSpellDamage(int spellDamage, MagicType magic, Actor attacker, Actor defender) {
		float damage = spellDamage + attacker.getStats().getSpellDamage();
		
		if(defender.getStats().isResistant(magic))
			damage *= RESISTANCE_MULT;
		
		return damage;
	}
	
	public static float calcMultiplyerMax(int stat) {
		float perc = stat / MAX_STAT;
		float inv = 1.0f - perc;
		inv *= inv;
		
		return (1.0f - inv) * MAX_MULT;
	}
	
	public static float calcInvMultiplyer(int stat) {
		float perc = stat / MAX_STAT;
		float inv = 1.0f - perc;
		inv *= inv;
		
		return Math.min(inv, 1.0f - MAX_MULT);
	}
}
