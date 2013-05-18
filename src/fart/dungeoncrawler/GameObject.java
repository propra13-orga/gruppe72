package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class GameObject implements IDrawable {
	protected Point screenPosition;
	
	protected abstract BufferedImage getTexture();
	public abstract Rectangle getCollisionRect();
	public abstract void terminate();
	
	@Override
	public void draw(Graphics2D graphics) {
		graphics.drawImage(getTexture(), screenPosition.x, screenPosition.y, null);
	}
}