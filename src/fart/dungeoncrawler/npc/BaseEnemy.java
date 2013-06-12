package fart.dungeoncrawler.npc;

import java.awt.image.BufferedImage;

import fart.dungeoncrawler.Animation;
import fart.dungeoncrawler.Collision;
import fart.dungeoncrawler.Health;
import fart.dungeoncrawler.IUpdateable;

public abstract class BaseEnemy extends BaseNPC implements IUpdateable {
	protected Animation curAnim;
	protected Health health;
	protected int aggroRange;
	protected int attackRange;
	protected Collision collision;
	
	public BaseEnemy(EnemyDescription desc, Collision collision) {
		super(desc);
		this.collision = collision;
		this.aggroRange = desc.getAggroRange();
		//this.attackRange = desc.getAttackRange();
		this.health = desc.getHealth();
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
