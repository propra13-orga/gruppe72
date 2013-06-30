package Utils;

import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.actor.ElementalDamage;
import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.enums.ElementType;
import fart.dungeoncrawler.items.Weapon;

public class DamageCalculator {
	private static final float MAX_STAT = 1000;
	private static final float MAX_MULT = 0.8f;
	private static final float RESISTANCE_MULT = 0.6f;
	private static final float OUTCLASS_MULT = 1.4f;
	
	public static float calcDamage(Actor attacker, Actor defender) {
		Stats aStats = attacker.getStats();
		Stats dStats = defender.getStats();
		
		float redByArmor = 1.0f - calcMultiplyerMax(dStats.getArmor());
		float dmgMult = 1.0f + calcMultiplyerMax(aStats.getStrength());
		float damage = aStats.getDamage() * dmgMult;
		
		damage *= redByArmor;
		
		Weapon weapon = attacker.getEquipment().getWeapon();
		if(weapon != null) {
			ElementalDamage eleDmg = weapon.getEleDamage();
			if(eleDmg != null) {
				float add = 0.0f;
				int e = eleDmg.getFireDamage();
				if(e > 0)
					add += calcSpellDamage(e, ElementType.Fire, attacker, defender);
				
				e = eleDmg.getWaterDamage();
				if(e > 0)
					add += calcSpellDamage(e, ElementType.Water, attacker, defender);
				
				e = eleDmg.getEarthDamage();
				if(e > 0)
					add += calcSpellDamage(e, ElementType.Earth, attacker, defender);
				
				damage += add;
			}
		}
		
		return damage;
	}
	
	public static float calcSpellDamage(float spellDamage, ElementType magic, Actor attacker, Actor defender) {
		float damage = spellDamage + attacker.getStats().getSpellDamage();
		
		if(magic == defender.getElementType())
			damage *= RESISTANCE_MULT;
		if(isOutclassing(magic, defender.getElementType()))
			damage *= OUTCLASS_MULT;
		
		return damage;
	}
	
	public static int calcAttackSpeed(Actor owner, int baseSpeed) {
		int ag = owner.getStats().getAgility();
		float mul = 1.0f - calcMultiplyerMax(ag);
		
		return (int)(mul * baseSpeed);
	}
	
	private static boolean isOutclassing(ElementType t1, ElementType t2) {
		if(t2 == ElementType.None)
			return false;
		
		if(t1 == ElementType.Fire && t2 == ElementType.Earth)
			return true;
		if(t1 == ElementType.Water && t2 == ElementType.Fire)
			return true;
		if(t1 == ElementType.Earth && t2 == ElementType.Water)
			return true;
		
		return false;
	}
	
	/*private static boolean isOutclassing(ElementType t1, Stats s2) {
		if(t1 == ElementType.Fire && s2.isResistant(ElementType.Earth))
			return true;
		if(t1 == ElementType.Water && s2.isResistant(ElementType.Fire))
			return true;
		if(t1 == ElementType.Earth && s2.isResistant(ElementType.Water))
			return true;
		
		return false;
	}*/
	
	public static float calcMultiplyerMax(int stat) {
		float perc = stat / MAX_STAT;
		float inv = 1.0f - perc;
		inv *= inv;
		
		return (1.0f - inv) * MAX_MULT;
	}
}
