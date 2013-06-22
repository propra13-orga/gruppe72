package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

public class EnemyDescription extends NPCDescription{
	
	private boolean isRanged;
	private int aggroRange;
	private int attackRange;
	
	public EnemyDescription(boolean isRanged,
							BufferedImage spriteSheet,
							int aggroRange,
							int attackRange,
							int type) {
		super(spriteSheet, type);
		this.isRanged = isRanged;
		this.aggroRange = aggroRange;
		this.attackRange = attackRange;
	}
	
	public boolean getIsRanged() { return isRanged; }
	public int getAggroRange() { return aggroRange; }
	public int getAttackRange() { return attackRange; }
}
