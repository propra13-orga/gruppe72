package fart.dungeoncrawler.actor;

import java.awt.Font;
import java.awt.Graphics2D;
import java.io.Serializable;

import Utils.Vector2;

import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.gamestates.GameStateInShop;

/**
 * The Stats class holds a number of values improving the overall strength of an actor. 
 * 
 * Stamina: 	increases the maximum HP and regeneration
 * Strength: 	increases the physical damage done
 * Agility: 	increases the attackspeed
 * Will: 		increases the maximum MP, regeneration and spelldamage
 * Armor:		reduces the physical damage taken
 * 
 * Damage:		the physical damage done (without considering strength)
 * SpellDamage:	extradamage done from each damagespell
 * 
 * This class can also draw itself when the player is in the inventory. 
 * @author Felix
 *
 */
public class Stats implements IDrawable, Serializable {
	private static final long serialVersionUID = 7841914871298265438L;
	
	public static final int HEALTH_PER_STAM = 10;
	public static final float HEALTH_REG_PER_STAM = 0.15f;
	public static final int MANA_PER_WILL = 10;
	public static final float MANA_REG_PER_STAM = 0.125f;
	public static final float SPELL_DMG_PER_WILL = 0.25f;
	
	public static final int STATS_PER_LEVEL = 5;
	private static final Stats MELEE_BASE_STATS = new Stats(5, 6, 5, 3, 10, 6, 0);
	private static final Stats CASTER_BASE_STATS = new Stats(4, 4, 4, 7, 4, 2, 0);
	
	private int stamina;
	private int strength;
	private int agility;
	private int will;
	private int armor;
	
	private int damage;
	private int spellDamage;
	
	private static final Vector2 START_POS = new Vector2(860, 400);
	private static final Font font = new Font("Arial", 0x1, 12);
	
	/**
	 * Creates a new instance with default-values. 
	 */
	public Stats() {
		stamina = 4;
		strength = 8;
		agility = 7;
		will = 8;
		armor = 0;
		damage = 4;
		spellDamage = 0;
	}
	
	/**
	 * Creates a new instance. Missing stats hold default values. 
	 * @param stamina
	 * @param strength
	 * @param agility
	 * @param will
	 * @param armor
	 */
	public Stats(int stamina, int strength, int agility, int will, int armor) {
		this.stamina = stamina;
		this.strength = strength;
		this.agility = agility;
		this.will = will;
		this.armor = armor;
		damage = 5;
		spellDamage = 0;
	}
	
	/**
	 * Creates a new instance.
	 * @param stamina
	 * @param strength
	 * @param agility
	 * @param will
	 * @param armor
	 * @param damage
	 * @param spellDamage
	 */
	public Stats(int stamina, int strength, int agility, int will, int armor, int damage, int spellDamage) {
		this.stamina = stamina;
		this.strength = strength;
		this.agility = agility;
		this.will = will;
		this.armor = armor;
		this.damage = damage;
		this.spellDamage = spellDamage;
	}
	
	/**
	 * Copies an instance.
	 * @param s
	 */
	public Stats(Stats s) {
		stamina = s.stamina;
		strength = s.strength;
		agility = s.agility;
		will = s.will;
		armor = s.armor;
		damage = s.damage;
		spellDamage = s.spellDamage;
	}
	
	/**
	 * Creates stats for a melee-enemy of a given level. 
	 * @param level
	 * @return
	 */
	public static Stats getMeleeStats(int level) {
		Stats result = new Stats(MELEE_BASE_STATS);
		//per level we add 5 StatPoints. 2 go to stamina, 2 to strength, 1 to agility
		result.stamina += level * 2;
		result.strength += level * 2;
		result.agility += 1;
		
		//every five levels, we leave one agility and one strength and add those two points to will for some mana-increase
		int levelOverFive = level / 5;
		if(levelOverFive > 0) {
			result.agility -= levelOverFive;
			result.strength -= levelOverFive;
			result.will += (levelOverFive * 2);
		}
		
		result.damage += (int)(level * 4.5f);
		
		return result;
	}
	
	/**
	 * Creates stats for a casting enemy of a given level. 
	 * @param level
	 * @return
	 */
	public static Stats getCasterStats(int level) {
		Stats result = new Stats(CASTER_BASE_STATS);

		result.stamina += level * 2;
		result.will += level * 3;

		return result;
	}
	
	/**
	 * Adds to instances of stats together. 
	 * @param s
	 */
	public void addStats(Stats s) {
		stamina += s.stamina;
		strength += s.strength;
		agility += s.agility;
		will += s.will;
		armor += s.armor;
		damage += s.damage;
		spellDamage += s.spellDamage;
	}
	
	/**
	 * Reduces the values of this instance by the corresponding values in s. 
	 * @param s
	 */
	public void redStats(Stats s) {
		stamina -= s.stamina;
		strength -= s.strength;
		agility -= s.agility;
		will -= s.will;
		armor -= s.armor;
		damage -= s.damage;
		spellDamage -= s.spellDamage;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		graphics.setFont(font);
		graphics.setColor(GameStateInShop.FONT_COLOR);
		
		int yPos = 280;
		int border = 21;
		int pos = 0;
		graphics.drawString("Stamina:", START_POS.x, yPos + border * pos);
		graphics.drawString(((Integer)stamina).toString(), START_POS.x + 4 * 32, yPos + border * pos++);
		graphics.drawString("Strength:", START_POS.x, yPos + border * pos);
		graphics.drawString(((Integer)strength).toString(), START_POS.x + 4 * 32, yPos + border * pos++);
		graphics.drawString("Agility:", START_POS.x, yPos + border * pos);
		graphics.drawString(((Integer)agility).toString(), START_POS.x + 4 * 32, yPos + border * pos++);
		graphics.drawString("Will:", START_POS.x, yPos + border * pos);
		graphics.drawString(((Integer)will).toString(), START_POS.x + 4 * 32, yPos + border * pos++);
		graphics.drawString("Armor:", START_POS.x, yPos + border * pos);
		graphics.drawString(((Integer)armor).toString(), START_POS.x + 4 * 32, yPos + border * pos++);
		
		graphics.drawString("Damage:", START_POS.x, yPos + border * pos);
		graphics.drawString(((Integer)damage).toString(), START_POS.x + 4 * 32, yPos + border * pos++);
		graphics.drawString("Spell-Damage:", START_POS.x, yPos + border * pos);
		graphics.drawString(((Integer)spellDamage).toString(), START_POS.x + 4 * 32, yPos + border * pos++);
	}

	/**
	 * @return the stamina
	 */
	public int getStamina() {
		return stamina;
	}

	/**
	 * Adds the amount to stamina. 
	 * @param amount
	 */
	public void addStamina(int amount) {
		this.stamina += amount;
	}
	
	/**
	 * Reduces stamina by amount.
	 * @param amount
	 */
	public void redStamina(int amount) {
		this.stamina -= amount;
	}
	
	/**
	 * @return the strength
	 */
	public int getStrength() {
		return strength;
	}

	/**
	 * Adds the amount to strength.
	 * @param amount
	 */
	public void addStrength(int amount) {
		this.strength += amount;
	}
	
	/**
	 * Reduces strength by amount.
	 * @param amount
	 */
	public void redStrength(int amount) {
		this.strength -= strength;
	}
	
	/**
	 * @return the agility
	 */
	public int getAgility() {
		return agility;
	}

	/**
	 * Adds the amount to agility.
	 * @param amount
	 */
	public void addAgility(int amount) {
		this.agility += amount;
	}
	
	/**
	 * Reduces agility by amount. 
	 * @param amount
	 */
	public void redAgility(int amount) {
		this.agility -= amount;
	}
	
	/**
	 * @return the will
	 */
	public int getWill() {
		return will;
	}

	/**
	 * Adds the amount to will.
	 * @param amount
	 */
	public void addWill(int amount) {
		this.will += amount;
	}
	
	/**
	 * Reduces will by amount. 
	 * @param amount
	 */
	public void redWill(int amount) {
		this.will -= amount;
	}
	
	/**
	 * @return the armor
	 */
	public int getArmor() {
		return armor;
	}

	/**
	 * Adds the amount to armor.
	 * @param amount
	 */
	public void addArmor(int amount) {
		this.armor += amount;
	}
	
	/**
	 * Reduces armor by amount. 
	 * @param amount
	 */
	public void redArmor(int amount) {
		this.armor -= amount;
	}
	
	/**
	 * Returns the damage. 
	 * @return
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * Adds the amount to damage. 
	 * @param amount
	 */
	public void addDamage(int amount) {
		this.damage += amount;
	}
	
	/**
	 * Reduces damage by amount.
	 * @param amount
	 */
	public void redDamage(int amount) {
		this.damage -= amount;
	}
	
	/**
	 * Returns the spellDamage. 
	 * @return
	 */
	public int getSpellDamage() {
		return spellDamage;
	}
	
	/**
	 * Adds the amount to spelldamage. 
	 * @param amount
	 */
	public void addSpellDamage(int amount) {
		this.spellDamage += amount;
	}
	
	/**
	 * Reduces spelldamage by amount. 
	 * @param amount
	 */
	public void redSpellDamage(int amount) {
		this.spellDamage -= amount;
	}

	/**
	 * Returns the amount of health regenerated.
	 * @return
	 */
	public float getHealthRegAmount() {
		return stamina * HEALTH_REG_PER_STAM;
	}
	
	/**
	 * Returns the amount of mana regenerated. 
	 * @return
	 */
	public float getManaRegAmount() {
		return will * MANA_REG_PER_STAM;
	}
}
