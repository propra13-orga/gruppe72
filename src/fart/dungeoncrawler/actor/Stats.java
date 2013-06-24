package fart.dungeoncrawler.actor;

import java.awt.Graphics2D;

import Utils.Vector2;

import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.enums.MagicType;
import fart.dungeoncrawler.gamestates.GameStateInShop;

public class Stats implements IDrawable {
	public static final int HEALTH_PER_STAM = 10;
	public static final int MANA_PER_WILL = 10;
	public static final float SPELL_DMG_PER_WILL = 0.25f;
	
	private int stamina;
	private int strength;
	private int agility;
	private int will;
	private int armor;
	private boolean fireResistant;
	private boolean waterResistant;
	private boolean earthResistant;
	private int damage;
	private int spellDamage;
	
	private static final Vector2 START_POS = new Vector2(860, 400);
	
	public Stats() {
		stamina = 10;
		strength = 8;
		agility = 7;
		will = 8;
		armor = 0;
		fireResistant = false;
		waterResistant = false;
		damage = 5;
		spellDamage = 0;
	}
	
	public Stats(int stamina, int strength, int agility, int will, int armor) {
		this.stamina = stamina;
		this.strength = strength;
		this.agility = agility;
		this.will = will;
		this.armor = armor;
		fireResistant = false;
		waterResistant = false;
		damage = 5;
		spellDamage = 0;
	}
	
	public Stats(int stamina, int strength, int agility, int will, int armor, int damage, int spellDamage) {
		this.stamina = stamina;
		this.strength = strength;
		this.agility = agility;
		this.will = will;
		this.armor = armor;
		fireResistant = false;
		waterResistant = false;
		earthResistant = false;
		this.damage = damage;
		this.spellDamage = spellDamage;
	}
	
	public Stats(Stats s) {
		stamina = s.stamina;
		strength = s.strength;
		agility = s.agility;
		will = s.will;
		armor = s.armor;
		fireResistant = s.fireResistant;
		waterResistant = s.waterResistant;
		earthResistant = s.earthResistant;
		damage = s.damage;
		spellDamage = s.spellDamage;
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
		
		if(fireResistant)
			graphics.drawString("Fire resistant", START_POS.x, yPos + border * pos++);
		if(waterResistant)
			graphics.drawString("Water resistant", START_POS.x, yPos + border * pos++);
		if(earthResistant)
			graphics.drawString("Earth resistant", START_POS.x, yPos + border * pos++);
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
	public boolean getFireResistance() {
		return fireResistant;
	}
	
	public void setFireResistant(boolean res) {
		fireResistant = res;
	}

	/**
	 * @return the iceResistance
	 */
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
	
	public boolean isResistant(MagicType type) {
		switch(type) {
		case Earth:
			return getEarthResistance();
		case Fire:
			return getFireResistance();
		case Water:
			return getWaterResistance();
		}
		
		return false;
	}

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

}
