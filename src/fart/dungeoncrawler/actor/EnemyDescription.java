package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

import fart.dungeoncrawler.enums.Heading;

public class EnemyDescription extends NPCDescription{
	
	private boolean isRanged;
	private int aggroRange;
	private int attackRange;
	
	public EnemyDescription(boolean isRanged,
							BufferedImage spriteSheet,
							int aggroRange,
							int attackRange,
							int type,
							int maxHealth, 
							int maxMana, 
							Stats stats, 
							Heading heading) {
		super(spriteSheet, type, maxHealth, maxMana, stats, heading);
		this.isRanged = isRanged;
		this.aggroRange = aggroRange;
		this.attackRange = attackRange;
	}
	
	public EnemyDescription(boolean isRanged,
			BufferedImage spriteSheet,
			int aggroRange,
			int attackRange,
			int type,
			ActorDescription aDesc) {
		super(spriteSheet,
				type,
				aDesc.health.getMaxHealth(),
				aDesc.mana.getMaxMana(),
				aDesc.getStats(),
				aDesc.getHeading());
		this.isRanged = isRanged;
		this.aggroRange = aggroRange;
		this.attackRange = attackRange;
	}
	
	public boolean getIsRanged() { return isRanged; }
	public int getAggroRange() { return aggroRange; }
	public int getAttackRange() { return attackRange; }
}
