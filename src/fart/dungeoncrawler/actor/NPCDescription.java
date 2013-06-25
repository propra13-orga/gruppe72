package fart.dungeoncrawler.actor;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import fart.dungeoncrawler.enums.Heading;
import fart.dungeoncrawler.enums.NPCType;

public class NPCDescription extends ActorDescription {
	private NPCType type;
	
	public NPCDescription(String spritePath,
					int type,
					int maxHealth,
					int maxMana,
					Stats stats,
					Heading heading) {
		super(spritePath,
					maxHealth,
					maxMana,
					stats,
					heading);		
		this.type = NPCType.values()[type];
	}
	
	public NPCDescription(String spritePath,
			int type,
			ActorDescription aDesc) {
		super(spritePath,
					aDesc.health.getMaxHealth(),
					aDesc.mana.getMaxMana(),
					aDesc.stats,
					aDesc.heading);
		this.type = NPCType.values()[type];
	}
	
	public NPCType getType() { return type; }
}
