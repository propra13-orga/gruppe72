package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.enums.ElementType;

public abstract class BaseSpell implements IUpdateable {
	public static final int GLOBAL_COOLDOWN = 90;
	public static final int MAX_SKILL_LEVEL = 3;
	
	protected int level;
	protected int manaCost;
	protected int cooldown;
	protected int currentCooldown;
	protected BufferedImage icon;
	protected ElementType type;
	
	public BaseSpell(int manaCost, int cooldown, BufferedImage icon, ElementType type) {
		this.level = 0;
		this.manaCost = manaCost;
		this.cooldown = cooldown;
		this.currentCooldown = 0;
		this.icon = icon;
		this.type = type;
	}
	
	public abstract void levelUp();
	public abstract void activate();
	
	public int getManaCost() {
		return manaCost;
	}

	public int getCooldown() {
		return cooldown;
	}
	
	public int getCurrentCooldown() {
		return currentCooldown;
	}
	
	public void setCurrentCooldown(int cooldown) {
		this.currentCooldown = cooldown;
	}
	
	public ElementType getType() {
		return type;
	}
	
	@Override
	public void update(float elapsed) {
		currentCooldown -= 1;
		if(currentCooldown < 0)
			currentCooldown = 0;
	}

	public boolean isOnCooldown() {
		return currentCooldown > 0;
	}
}
