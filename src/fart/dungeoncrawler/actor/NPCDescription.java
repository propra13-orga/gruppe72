package fart.dungeoncrawler.actor;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import fart.dungeoncrawler.enums.Heading;
import fart.dungeoncrawler.enums.NPCType;

public class NPCDescription extends ActorDescription {
	private BufferedImage spriteSheet;
	private NPCType type;
	
	public NPCDescription(BufferedImage spriteSheet,
					int type,
					int maxHealth,
					int maxMana,
					Stats stats,
					Heading heading) {
		super(new Dimension(spriteSheet.getWidth(), spriteSheet.getHeight()),
					maxHealth,
					maxMana,
					stats,
					heading);		
		this.spriteSheet = spriteSheet;
		this.type = NPCType.values()[type];
	}
	
	public NPCDescription(BufferedImage spriteSheet,
			int type,
			ActorDescription aDesc) {
		super(new Dimension(spriteSheet.getWidth(), spriteSheet.getHeight()),
					aDesc.health.getMaxHealth(),
					aDesc.mana.getMaxMana(),
					aDesc.stats,
					aDesc.heading);		
		this.spriteSheet = spriteSheet;
		this.type = NPCType.values()[type];
	}
	
	public BufferedImage getSpriteSheet() { return spriteSheet; }
	public NPCType getType() { return type; }
}
