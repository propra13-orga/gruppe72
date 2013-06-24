package fart.dungeoncrawler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fart.dungeoncrawler.actor.NewPlayer;

public class StatusBar implements IDrawable {
	private Health health;
	private Mana mana;
	private Rectangle healthRect;
	private Rectangle manaRect;
	private BufferedImage barTexture;
	
	//zur anzeige von lebenspunkten etc
	public StatusBar(NewPlayer player) {
		this.health = player.getHealth();
		this.mana = player.getMana();
		
		//bilder fehlen noch... können nicht geladen werden. rect muss etwas kleiner als healthframe sein.
		healthRect = new Rectangle(20, 20, 93, 13);
		//manaRect = new Rectangle(360, 20, 93, 13);
		manaRect = new Rectangle(20, 40, 93, 13);
		
		try {
			barTexture = ImageIO.read(new File("res/emptyBar.png"));
		} catch (IOException e) {
			System.err.println("Could not load image.");
		}
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
		
		graphics.drawImage(barTexture, healthRect.x - 4, healthRect.y - 3, null);
		
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
		graphics.drawImage(barTexture, manaRect.x - 4, manaRect.y - 3, null);
	}
}
