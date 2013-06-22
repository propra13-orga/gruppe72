package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

import fart.dungeoncrawler.enums.NPCType;

public class NPCDescription {
	private BufferedImage spriteSheet;
	private NPCType type;
	
	public NPCDescription(BufferedImage spriteSheet, int type) {
		this.spriteSheet = spriteSheet;
		this.type = NPCType.values()[type];
	}
	
	public BufferedImage getSpriteSheet() { return spriteSheet; }
	public NPCType getType() { return type; }
}
