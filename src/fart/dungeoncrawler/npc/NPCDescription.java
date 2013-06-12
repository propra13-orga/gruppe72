package fart.dungeoncrawler.npc;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import MathUtils.Vector2;

import fart.dungeoncrawler.enums.Heading;

public class NPCDescription {
	private Vector2 position;
	private Rectangle colRect;
	private BufferedImage spriteSheet;
	private Heading heading;
	
	public NPCDescription(Vector2 position, Dimension colDimension, BufferedImage spriteSheet, Heading heading) {
		this.position = position;
		colRect = new Rectangle((int)position.x, (int)position.y, colDimension.width, colDimension.height);
		this.spriteSheet = spriteSheet;
		this.heading = heading;
	}
	
	public Vector2 getPosition() { return position; }
	public Rectangle getColRect() { return colRect; }
	public BufferedImage getSpriteSheet() { return spriteSheet; }
	public Heading getHeading() { return heading; }
}
