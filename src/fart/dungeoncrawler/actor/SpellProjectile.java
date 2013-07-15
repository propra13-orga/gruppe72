package fart.dungeoncrawler.actor;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import fart.dungeoncrawler.CollisionDetector;
import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.IUpdateable;

import Utils.Vector2;

/**
 * Represents the projectile of a damagespell. 
 * @author Felix
 *
 */
public class SpellProjectile extends GameObject implements IUpdateable {
	private Actor owner;
	private Vector2 velocity;
	private BufferedImage texture;
	private Rectangle collisionRect;
	private int damage;
	private CollisionDetector collision;
	private Spell spell;
	
	/**
	 * Creates an instance of the SpellProjectile that is passed to the contructor of a spell. When casting
	 * this projectile is partly copied. 
	 * @param owner owner of the spell
	 * @param texture texture of the projectile
	 * @param collision collision-detector
	 */
	public SpellProjectile(Actor owner, BufferedImage texture, CollisionDetector collision) {
		this.owner = owner;
		this.texture = texture;
		this.collision = collision;
	}

	/**
	 * Creates a SpellProjectile from a given projectile. 
	 * @param projectile the projectile to be copied from
	 * @param startPosition startposition of the projectile
	 * @param velocity velocoty of the projectile
	 * @param spell the spell that this projectile belongs to
	 */
	public SpellProjectile(SpellProjectile projectile, Vector2 startPosition, Vector2 velocity, Spell spell) {
		this.owner = projectile.owner;
		this.texture = projectile.texture;
		this.velocity = velocity;
		this.damage = spell.damage;
		this.collision = projectile.collision;
		this.spell = spell;
		
		collisionRect = new Rectangle((int)startPosition.x, (int)startPosition.y, texture.getWidth(), texture.getHeight());
		screenPosition = startPosition;
	}
	
	/**
	 * Returns the damage done by this projectile.
	 * @return
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * Returns the owner of this projectile.
	 * @return
	 */
	public Actor getOwner() {
		return owner;
	}
	
	/**
	 * Returns the ID of the owner. 
	 * @return
	 */
	public int getOwnerID() {
		return owner.getID();
	}
	
	/**
	 * Returns the collision-detector.
	 */
	public CollisionDetector getCollision() {
		return collision;
	}
	
	/**
	 * Returns the spell that this projectile belongs to. 
	 * @return
	 */
	public Spell getSpell() {
		return spell;
	}
	
	@Override
	protected BufferedImage getTexture() {
		return texture;
	}
	
	@Override
	public void update(float elapsed) {
		screenPosition.x += velocity.x;
		screenPosition.y += velocity.y;
		collisionRect.x = (int)screenPosition.x;
		collisionRect.y = (int)screenPosition.y;
	}

	@Override
	public Rectangle getCollisionRect() {
		return collisionRect;
	}

	@Override
	public void terminate() {
	}

}
