package fart.dungeoncrawler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class StatusBar implements IDrawable {
	private Health health;
	private Mana mana;
	private BufferedImage healthFrame;
	private Rectangle healthRect;
	private Rectangle manaRect;
	
	//zur anzeige von lebenspunkten etc
	public StatusBar(Player player) {
		this.health = player.getHealth();
		this.mana = player.getMana();
		
		//bilder fehlen noch... können nicht geladen werden. rect muss etwas kleiner als healthframe sein.
		healthRect = new Rectangle(20, 20, 100, 15);
		manaRect = new Rectangle(360, 20, 100, 15);
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
		
		int mp = mana.getCurrentMana();
		int maxMP = mana.getMaxMana();
		
		percent = (float)mp/maxMP;
		xStart = manaRect.x;
		xWidth = (int)(manaRect.width * percent);
		yStart = manaRect.y;
		yWidth = manaRect.height;
		
		graphics.setColor(new Color(0.0f, 0.0f, 1.0f));
		graphics.fillRect(xStart, yStart, xWidth, yWidth);
		
		//hier noch draw frame etc, aber bilder fehlen. 
	}
}
