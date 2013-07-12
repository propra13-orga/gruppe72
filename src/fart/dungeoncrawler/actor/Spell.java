package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.enums.Heading;
import fart.dungeoncrawler.enums.ElementType;
import Utils.Vector2;

public abstract class Spell extends BaseSpell implements IUpdateable {
	private SpellProjectile projectile;
	protected int damage;
	private float speed;
	
	public Spell(int manaCost, int cooldown, ElementType type, BufferedImage icon, SpellProjectile projectile, int damage, float speed) {
		super(manaCost, cooldown, icon, type);
		this.projectile = projectile;
		this.damage = damage;
		this.speed = speed;
		currentCooldown = 0;
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

	public void activate() {
		currentCooldown = cooldown;
	}
	
	@Override
	public void update(float elapsed) {
		currentCooldown -= 1;
		if(currentCooldown < 0)
			currentCooldown = 0;
	}
}
