package fart.dungeoncrawler.npc;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import MathUtils.Vector2;
import fart.dungeoncrawler.Health;
import fart.dungeoncrawler.enums.Heading;

public class EnemyDescription extends NPCDescription{
	
	private boolean isRanged;
	private int aggroRange;
	private Health health;
	
	public EnemyDescription(Vector2 position,
							Dimension colDimension,
							boolean isRanged,
							BufferedImage spriteSheet,
							Heading heading,
							int aggroRange,
							Health health) {
		super(position, colDimension, spriteSheet, heading);
		this.isRanged = isRanged;
		this.aggroRange = aggroRange;
		this.health = health;
	}
	
	public boolean getIsRanged() { return isRanged; }
	public int getAggroRange() { return aggroRange; }
	public Health getHealth() { return health; }
}
