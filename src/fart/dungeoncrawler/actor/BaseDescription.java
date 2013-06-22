package fart.dungeoncrawler.actor;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BaseDescription {
	private BufferedImage texture;
	private Rectangle collisionRect;
	
	public BaseDescription(String spritePath) {
		try {
			texture = ImageIO.read(new File(spritePath));
			collisionRect = new Rectangle(0, 0, texture.getWidth(), texture.getHeight());
		} catch (IOException e) {
			System.err.println("Could not load image.");
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	public BufferedImage getTexture() {
		return texture;
	}
	
	public Rectangle getCollisionRect() {
		return collisionRect;
	}
}
