package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.enums.Heading;
import fart.dungeoncrawler.enums.ElementType;
import Utils.Vector2;

public class Spell implements IUpdateable {
	private SpellProjectile projectile;
	private int damage;
	private int manaCost;
	private int cooldown;
	private int currentCooldown;
	private float speed;
	private BufferedImage icon;
	private ElementType type;
	
	public Spell(SpellProjectile projectile, int damage, int manaCost, int cooldown, float speed, ElementType type) {
		this.projectile = projectile;
		this.damage = damage;
		this.manaCost = manaCost;
		this.cooldown = cooldown;
		this.speed = speed;
		this.type = type;
		currentCooldown = 0;
	}
	
	public BufferedImage getIcon() {
		return icon;
	}

	public SpellProjectile getProjectile(Vector2 position, Heading heading) {
		Vector2 velocity = Vector2.Zero;
		
		switch(heading) {
		case Left:
			velocity = new Vector2(-1, 0);
			break;
		case Right:
			velocity = new Vector2(1, 0);
			break;
		case Up:
			velocity = new Vector2(0, -1);
			break;
		case Down:
			velocity = new Vector2(0, 1);
			break;
		}
		
		return new SpellProjectile(projectile, position, velocity.mul(speed), this);
	}

	public int getDamage() {
		return damage;
	}

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
	
	public void activate() {
		currentCooldown = cooldown;
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
