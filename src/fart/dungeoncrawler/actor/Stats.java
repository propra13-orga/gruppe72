package fart.dungeoncrawler.actor;

import java.awt.Graphics2D;

import Utils.Vector2;

import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.enums.ElementType;
import fart.dungeoncrawler.gamestates.GameStateInShop;

public class Stats implements IDrawable {
	public static final int HEALTH_PER_STAM = 10;
	public static final float HEALTH_REG_PER_STAM = 0.15f;
	public static final int MANA_PER_WILL = 10;
	public static final float MANA_REG_PER_STAM = 0.125f;
	public static final float SPELL_DMG_PER_WILL = 0.25f;
	
	private static final int STATS_PER_LEVEL = 5;
	private static final Stats MELEE_BASE_STATS = new Stats(5, 6, 5, 3, 10, 6, 0);
	private static final Stats CASTER_BASE_STATS = new Stats(4, 4, 4, 7, 4, 2, 0);
	
	private int stamina;
	private int strength;
	private int agility;
	private int will;
	private int armor;
	
	//TODO: Replace resistances with Actor-ElementTypes
	/*private boolean fireResistant;
	private boolean waterResistant;
	private boolean earthResistant;*/
	
	private int damage;
	private int spellDamage;
	
	private int statsToAdd;
	
	private static final Vector2 START_POS = new Vector2(860, 400);
	
	public Stats() {
		stamina = 4;
		strength = 8;
		agility = 7;
		will = 8;
		armor = 0;
		/*fireResistant = false;
		waterResistant = false;*/
		damage = 4;
		spellDamage = 0;
	}
	
	public Stats(int stamina, int strength, int agility, int will, int armor) {
		this.stamina = stamina;
		this.strength = strength;
		this.agility = agility;
		this.will = will;
		this.armor = armor;
		/*fireResistant = false;
		waterResistant = false;*/
		damage = 5;
		spellDamage = 0;
	}
	
	public Stats(int stamina, int strength, int agility, int will, int armor, int damage, int spellDamage) {
		this.stamina = stamina;
		this.strength = strength;
		this.agility = agility;
		this.will = will;
		this.armor = armor;
		/*fireResistant = false;
		waterResistant = false;
		earthResistant = false;*/
		this.damage = damage;
		this.spellDamage = spellDamage;
	}
	
	public Stats(int stamina, int strength, int agility, int will, int armor, int damage, int spellDamage, boolean fireResi, boolean waterResi, boolean earthResi) {
		this.stamina = stamina;
		this.strength = strength;
		this.agility = agility;
		this.will = will;
		this.armor = armor;
		/*fireResistant = fireResi;
		waterResistant = waterResi;
		earthResistant = earthResi;*/
		this.damage = damage;
		this.spellDamage = spellDamage;
	}
	
	public Stats(Stats s) {
		stamina = s.stamina;
		strength = s.strength;
		agility = s.agility;
		will = s.will;
		armor = s.armor;
		/*fireResistant = s.fireResistant;
		waterResistant = s.waterResistant;
		earthResistant = s.earthResistant;*/
		damage = s.damage;
		spellDamage = s.spellDamage;
	}
	
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
	
	public static Stats getCasterStats(int level) {
		Stats result = new Stats(CASTER_BASE_STATS);

		result.stamina += level * 2;
		result.will += level * 3;

		return result;
	}
	
	public void addStats(Stats s) {
		stamina += s.stamina;
		strength += s.strength;
		agility += s.agility;
		will += s.will;
		armor += s.armor;
		damage += s.damage;
		spellDamage += s.spellDamage;
		
		/*if(!fireResistant)
			fireResistant = s.fireResistant;
		if(!waterResistant)
			waterResistant = s.waterResistant;
		if(!earthResistant)
			earthResistant = s.earthResistant;*/
	}
	
	public void redStats(Stats s) {
		stamina -= s.stamina;
		strength -= s.strength;
		agility -= s.agility;
		will -= s.will;
		armor -= s.armor;
		damage -= s.damage;
		spellDamage -= s.spellDamage;
		
		/*if(s.fireResistant)
			fireResistant = false;
		if(s.waterResistant)
			waterResistant = false;
		if(s.earthResistant)
			earthResistant = false;*/
	}
	
	public void levelUp() {
		statsToAdd += STATS_PER_LEVEL;
	}
	
	public void drawInGame(Graphics2D graphics) {
		
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		graphics.setFont(GameStateInShop.FONT);
		graphics.setColor(GameStateInShop.FONT_COLOR);
		
		int yPos = 280;
		int border = 15;
		int pos = 0;
		graphics.drawString("Stamina:          " + stamina, START_POS.x, yPos + border * pos++);
		graphics.drawString("Strength:         " + strength, START_POS.x, yPos + border * pos++);
		graphics.drawString("Agility:          " + agility, START_POS.x, yPos + border * pos++);
		graphics.drawString("Will:             " + will, START_POS.x, yPos + border * pos++);
		graphics.drawString("Armor:            " + armor, START_POS.x, yPos + border * pos++);
		
		graphics.drawString("Damage:           " + damage, START_POS.x, yPos + border * pos++);
		graphics.drawString("Spell-Damage:     " + spellDamage, START_POS.x, yPos + border * pos++);
		
		/*if(fireResistant)
			graphics.drawString("Fire resistant", START_POS.x, yPos + border * pos++);
		if(waterResistant)
			graphics.drawString("Water resistant", START_POS.x, yPos + border * pos++);
		if(earthResistant)
			graphics.drawString("Earth resistant", START_POS.x, yPos + border * pos++);*/
	}

	/**
	 * @return the stamina
	 */
	public int getStamina() {
		return stamina;
	}

	public void addStamina(int amount) {
		this.stamina += amount;
	}
	
	public void redStamina(int amount) {
		this.stamina -= amount;
	}
	
	/**
	 * @return the strength
	 */
	public int getStrength() {
		return strength;
	}

	public void addStrength(int amount) {
		this.strength += amount;
	}
	
	public void redStrength(int amount) {
		this.strength -= strength;
	}
	
	/**
	 * @return the agility
	 */
	public int getAgility() {
		return agility;
	}

	public void addAgility(int amount) {
		this.agility += amount;
	}
	
	public void redAgility(int amount) {
		this.agility -= amount;
	}
	
	/**
	 * @return the will
	 */
	public int getWill() {
		return will;
	}

	public void addWill(int amount) {
		this.will += amount;
	}
	
	public void redWill(int amount) {
		this.will -= amount;
	}
	
	/**
	 * @return the armor
	 */
	public int getArmor() {
		return armor;
	}

	public void addArmor(int amount) {
		this.armor += amount;
	}
	
	public void redArmor(int amount) {
		this.armor -= amount;
	}
	
	/**
	 * @return the fireResistance
	 */
	/*public boolean getFireResistance() {
		return fireResistant;
	}
	
	public void setFireResistant(boolean res) {
		fireResistant = res;
	}*/

	/**
	 * @return the iceResistance
	 */
	/*
	public boolean getWaterResistance() {
		return waterResistant;
	}
	
	public void setWaterResistant(boolean res) {
		waterResistant = res;
	}
	
	public boolean getEarthResistance() {
		return earthResistant;
	}
	
	public void setEarthResistant(boolean res) {
		earthResistant = res;
	}
	
	public boolean isResistant(ElementType type) {
		switch(type) {
		case Earth:
			return getEarthResistance();
		case Fire:
			return getFireResistance();
		case Water:
			return getWaterResistance();
		}
		
		return false;
	}*/

	public int getDamage() {
		return damage;
	}
	
	public void addDamage(int amount) {
		this.damage += amount;
	}
	
	public void redDamage(int amount) {
		this.damage -= amount;
	}
	
	public int getSpellDamage() {
		return spellDamage;
	}
	
	public void addSpellDamage(int amount) {
		this.spellDamage += amount;
	}
	
	public void redSpellDamage(int amount) {
		this.spellDamage -= amount;
	}

	public float getHealthRegAmount() {
		return stamina * HEALTH_REG_PER_STAM;
	}
	
	public float getManaRegAmount() {
		return will * MANA_REG_PER_STAM;
	}
}
