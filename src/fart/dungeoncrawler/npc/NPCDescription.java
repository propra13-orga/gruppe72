package fart.dungeoncrawler.npc;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Utils.Vector2;

import fart.dungeoncrawler.enums.Heading;
import fart.dungeoncrawler.enums.NPCType;

public class NPCDescription {
	private Vector2 position;
	private Rectangle colRect;
	private BufferedImage spriteSheet;
	private Heading heading;
	private NPCType type;
	
	public NPCDescription(Vector2 position, Dimension colDimension, BufferedImage spriteSheet, Heading heading, int type) {
		this.position = position;
		colRect = new Rectangle((int)position.x, (int)position.y, colDimension.width, colDimension.height);
		this.spriteSheet = spriteSheet;
		this.heading = heading;
		this.type = NPCType.values()[type];
	}
	
	public Vector2 getPosition() { return position; }
	public Rectangle getColRect() { return colRect; }
	public BufferedImage getSpriteSheet() { return spriteSheet; }
	public Heading getHeading() { return heading; }
	public NPCType getType() { return type; }
}
