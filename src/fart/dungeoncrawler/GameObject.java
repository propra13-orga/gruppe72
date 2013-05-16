package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public abstract class GameObject implements IDrawable {
	protected Point screenPosition;
	protected abstract BufferedImage getTexture();
	
	@Override
	public void draw(Graphics2D graphics) {
		graphics.drawImage(getTexture(), screenPosition.x, screenPosition.y, null);
	}
}