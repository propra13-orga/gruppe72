package fart.dungeoncrawler.npc;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import Utils.Vector2;

import fart.dungeoncrawler.Animation;
import fart.dungeoncrawler.Collision;
import fart.dungeoncrawler.Health;
import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.enums.Heading;

public abstract class BaseEnemy extends BaseNPC implements IUpdateable {
	protected Animation curAnim;
	protected Health health;
	protected int aggroRange;
	protected int attackRange;
	protected Collision collision;
	protected HashMap<DynamicObjectState, HashMap<Heading, Animation>> animations;
	
	public BaseEnemy(EnemyDescription desc, Collision collision) {
		super(desc);
		this.collision = collision;
		this.aggroRange = desc.getAggroRange();
		//this.attackRange = desc.getAttackRange();
		this.health = desc.getHealth();
		
		animations = new HashMap<DynamicObjectState, HashMap<Heading, Animation>>();
	}
	
	public void setHeading() {
		Vector2 velocity = getVelocity();
		Heading newHeading = heading;
		if(velocity.x > 0.01f)
			newHeading = Heading.Right;
		else if(velocity.x < -0.01f)
			newHeading = Heading.Left;
		else if(velocity.y > 0.01f)
			newHeading = Heading.Down;
		else if(velocity.y < -0.01f)
			newHeading = Heading.Up;
		
		if(heading != newHeading) {
			heading = newHeading;
			setCurrentAnimation(curState);
		}
	}
	
	public void setCurrentAnimation(DynamicObjectState state) {
		HashMap<Heading, Animation> map = animations.get(state);
		curAnim = map.get(heading);
	}
	
	@Override
	public BufferedImage getTexture() {
		return curAnim.getTexture();
	}
	
	public int getAggroRange() {
		return aggroRange;
	}
	
	public int getAttackRange() {
		return attackRange;
	}
}
