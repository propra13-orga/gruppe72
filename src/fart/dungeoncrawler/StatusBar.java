package fart.dungeoncrawler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class StatusBar implements IDrawable {
	private Health health;
	private BufferedImage healthFrame;
	private Rectangle healthRect;
	
	//zur anzeige von lebenspunkten etc
	public StatusBar(Player player) {
		this.health = player.getHealth();
		
		//bilder fehlen noch... können nicht geladen werden. rect muss etwas kleiner als healthframe sein.
		healthRect = new Rectangle(20, 20, 100, 15);
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		int hp = health.getCurrentHealth();
		int maxHP = health.getMaxHealth();
		
		float percent = (float)hp/maxHP;
		int xStart = healthRect.x;
		int xWidth = (int)(healthRect.width * percent);
		int yStart = healthRect.y;
		int yWidth = healthRect.height;
		
		graphics.setColor(new Color(1.0f, 0.0f, 0.0f));
		graphics.fillRect(xStart, yStart, xWidth, yWidth);
		//hier noch draw frame etc, aber bilder fehlen. 
	}
	
	public int interpolate(int x, int y, float val) {
		return (int)(x + (y - x) * val);
	}
}
