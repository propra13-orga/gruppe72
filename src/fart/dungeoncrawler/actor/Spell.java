package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.enums.Heading;
import fart.dungeoncrawler.enums.ElementType;
import Utils.Vector2;

/**
 * An abstract base class for all damage spells.
 * @author Felix
 *
 */
public abstract class Spell extends BaseSpell implements IUpdateable {
	private SpellProjectile projectile;
	protected int damage;
	private float speed;
	
	/**
	 * Creates an instance of the spell. 
	 * @param manaCost manaCost
	 * @param cooldown cooldown of the spell
	 * @param type the element-type
	 * @param icon the icon
	 * @param projectile the projectile that is fired
	 * @param damage spell damage
	 * @param speed the speed of the projectile
	 */
	public Spell(int manaCost, int cooldown, ElementType type, BufferedImage icon, SpellProjectile projectile, int damage, float speed) {
		super(manaCost, cooldown, icon, type);
		this.projectile = projectile;
		this.damage = damage;
		this.speed = speed;
		currentCooldown = 0;
	}
	
	/**
	 * Returns a projectile when the spell is casted. 
	 * @param position current position of the caster
	 * @param heading heading of the caster
	 * @return
	 */
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

	/**
	 * Returns the damage done.
	 * @return
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Activates the spell. It only sets the current cooldown to the cooldown of the spell.
	 */
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
