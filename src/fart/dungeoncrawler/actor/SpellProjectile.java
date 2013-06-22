package fart.dungeoncrawler.actor;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import fart.dungeoncrawler.Collision;
import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.IUpdateable;

import Utils.Vector2;

public class SpellProjectile extends GameObject implements IUpdateable {
	private int ownerID;
	private Vector2 velocity;
	private BufferedImage texture;
	private Rectangle collisionRect;
	private int damage;
	private Collision collision;
	
	public SpellProjectile(GameObject owner, BufferedImage texture,/* Vector2 velocity,*/ int damage,/* Vector2 startPosition,*/ Collision collision) {
		this.ownerID = owner.getID();
		this.texture = texture;
		//this.velocity = velocity;
		this.damage = damage;
		this.collision = collision;
		
		//collisionRect = new Rectangle((int)startPosition.x, (int)startPosition.y, texture.getWidth(), texture.getHeight());
		//screenPosition = startPosition;
	}

	public SpellProjectile(SpellProjectile projectile, Vector2 startPosition, Vector2 velocity) {
		this.ownerID = projectile.ownerID;
		this.texture = projectile.texture;
		this.velocity = velocity;
		this.damage = projectile.damage;
		this.collision = projectile.collision;
		
		collisionRect = new Rectangle((int)startPosition.x, (int)startPosition.y, texture.getWidth(), texture.getHeight());
		screenPosition = startPosition;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public int getOwnerID() {
		return ownerID;
	}
	
	public Collision getCollision() {
		return collision;
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
