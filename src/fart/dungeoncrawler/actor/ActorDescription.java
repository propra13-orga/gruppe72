package fart.dungeoncrawler.actor;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fart.dungeoncrawler.enums.ElementType;
import fart.dungeoncrawler.enums.Heading;

public class ActorDescription {
	private BufferedImage spriteSheet;
	protected Dimension collisionDimension;
	protected int level;
	protected int element;
	protected Stats stats;
	protected Heading heading;
	
	public ActorDescription(String spritePath, 
			int level, 
			int element, 
			Stats stats, 
			Heading heading) {
		
		try {
			spriteSheet = ImageIO.read(new File(spritePath));
			this.collisionDimension = new Dimension(32, 32);
		} catch (IOException e) {
			System.err.println("Could not load image.");
			e.printStackTrace();
			System.exit(2);
		}
	
		this.stats = stats;
		this.heading = heading;
		this.level = level;
		this.element = element;
	}
	
	public ActorDescription(Dimension collisionDim, 
			int level, 
			int element, 
			Stats stats, 
			Heading heading) {
		this.collisionDimension = collisionDim;
		this.stats = stats;
		this.heading = heading;
		this.level = level;
		this.element = element;
	}
	
	/**
	 * @return the collisionRect
	 */
	public Dimension getCollisionDimension() {
		return collisionDimension;
	}

	/**
	 * @return the health
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the mana
	 */
	public ElementType getElement() {
		return ElementType.values()[element];
	}

	/**
	 * @return the stats
	 */
	public Stats getStats() {
		return stats;
	}

	/**
	 * @return the heading
	 */
	public Heading getHeading() {
		return heading;
	}
	
	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}
}
