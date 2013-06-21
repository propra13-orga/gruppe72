package fart.dungeoncrawler.npc;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import Utils.Vector2;
import fart.dungeoncrawler.Health;
import fart.dungeoncrawler.enums.Heading;

public class EnemyDescription extends NPCDescription{
	
	private boolean isRanged;
	private int aggroRange;
	private int attackRange;
	private Health health;
	
	public EnemyDescription(Vector2 position,
							Dimension colDimension,
							boolean isRanged,
							BufferedImage spriteSheet,
							Heading heading,
							int aggroRange,
							int attackRange,
							Health health,
							int type) {
		super(position, colDimension, spriteSheet, heading, type);
		this.isRanged = isRanged;
		this.aggroRange = aggroRange;
		this.attackRange = attackRange;
		this.health = health;
	}
	
	public boolean getIsRanged() { return isRanged; }
	public int getAggroRange() { return aggroRange; }
	public int getAttackRange() { return attackRange; }
	public Health getHealth() { return health; }
}
