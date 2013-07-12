package fart.dungeoncrawler.actor;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import fart.dungeoncrawler.Collision;
import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.IUpdateable;

import Utils.Vector2;

public class SpellProjectile extends GameObject implements IUpdateable {
	private Actor owner;
	private Vector2 velocity;
	private BufferedImage texture;
	private Rectangle collisionRect;
	private int damage;
	private Collision collision;
	private Spell spell;
	
	public SpellProjectile(Actor owner, BufferedImage texture, Collision collision) {
		this.owner = owner;
		this.texture = texture;
		this.collision = collision;
	}

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
	
	public int getDamage() {
		return damage;
	}
	
	public Actor getOwner() {
		return owner;
	}
	
	public int getOwnerID() {
		return owner.getID();
	}
	
	public Collision getCollision() {
		return collision;
	}
	
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
