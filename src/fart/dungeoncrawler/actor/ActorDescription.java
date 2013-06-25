package fart.dungeoncrawler.actor;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fart.dungeoncrawler.*;
import fart.dungeoncrawler.enums.Heading;

public class ActorDescription {
	private BufferedImage spriteSheet;
	protected Dimension collisionDimension;
	protected Health health;
	protected Mana mana;
	protected Stats stats;
	protected Heading heading;
	
	public ActorDescription(String spritePath, 
			int maxHealth, 
			int maxMana, 
			Stats stats, 
			Heading heading) {
		
		try {
			spriteSheet = ImageIO.read(new File(spritePath));
			this.collisionDimension = new Dimension(spriteSheet.getWidth(), spriteSheet.getHeight());
		} catch (IOException e) {
			System.err.println("Could not load image.");
			e.printStackTrace();
			System.exit(2);
		}
	
		this.stats = stats;
		this.heading = heading;
		health = new Health(maxHealth);
		mana = new Mana(maxMana);
	}
	
	public ActorDescription(Dimension collisionDim, 
			int maxHealth, 
			int curHealth,
			int maxMana, 
			int curMana,
			Stats stats, 
			Heading heading) {
		this.collisionDimension = collisionDim;
		this.stats = stats;
		this.heading = heading;
		health = new Health(maxHealth);
		mana = new Mana(maxMana);
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
	public Health getHealth() {
		return new Health(health);
	}

	/**
	 * @return the mana
	 */
	public Mana getMana() {
		return new Mana(mana);
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
