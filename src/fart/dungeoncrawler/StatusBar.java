package fart.dungeoncrawler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fart.dungeoncrawler.actor.Level;
import fart.dungeoncrawler.actor.NewPlayer;
import fart.dungeoncrawler.actor.StatsMenu;

public class StatusBar implements IDrawable {
	private Health health;
	private Mana mana;
	private Level level;
	private Rectangle healthRect;
	private Rectangle manaRect;
	private Rectangle expRect;
	private BufferedImage barTexture;
	private Game game;
	private StatsMenu stats;
	
	private static Font fontHpMp = new Font("Arial", 0x1, 10);
	private static Font fontStats = new Font("Arial", 0x0, 12);
	private static Font fontLevel = new Font("Arial", 0x1, 14);
	
	//zur anzeige von lebenspunkten etc
	public StatusBar(NewPlayer player, Game game) {
		this.health = player.getHealth();
		this.mana = player.getMana();
		this.level = player.getLevel();
		this.game = game;
		this.stats = player.getStatsMenu();
		
		healthRect = new Rectangle(5 * 32, 32 * 20 + 7, 93, 13);
		manaRect = new Rectangle(5 * 32, 32 * 20 + 24, 93, 13);
		expRect = new Rectangle(5 * 32, 32 * 20 + 41, 93, 13);
		
		try {
			barTexture = ImageIO.read(new File("res/emptyBar.png"));
		} catch (IOException e) {
			System.err.println("Could not load image.");
		}
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		graphics.setFont(fontLevel);
		graphics.setColor(Color.white);
		graphics.drawString("Level: " + level.getLevel(), 20, 20 * 32 + 24);
		graphics.drawString(game.getMapName(), 20, 20 * 32 + 42);
		
		Integer hp = (int)health.getCurrentHealth();
		Integer maxHP = (int)health.getMaxHealth();
		
		float percent = (float)hp/maxHP;
		int xStart = healthRect.x;
		int xWidth = (int)(healthRect.width * percent);
		int yStart = healthRect.y;
		int yWidth = healthRect.height;
		
		graphics.setColor(new Color(1.0f, 0.0f, 0.0f));
		graphics.fillRect(xStart, yStart, xWidth, yWidth);
		
		graphics.drawImage(barTexture, healthRect.x - 4, healthRect.y - 3, null);
		
		graphics.setFont(fontHpMp);
		graphics.setColor(Color.white);
		graphics.drawString(hp.toString() + "/" + maxHP.toString(), xStart + 26, yStart + 8);
		
		Integer mp = (int)mana.getCurrentMana();
		Integer maxMP = (int)mana.getMaxMana();
		
		percent = (float)mp/maxMP;
		xStart = manaRect.x;
		xWidth = (int)(manaRect.width * percent);
		yStart = manaRect.y;
		yWidth = manaRect.height;
		
		graphics.setColor(new Color(0.0f, 0.0f, 1.0f));
		graphics.fillRect(xStart, yStart, xWidth, yWidth);
		
		graphics.drawImage(barTexture, manaRect.x - 4, manaRect.y - 3, null);
		
		graphics.setFont(fontHpMp);
		graphics.setColor(Color.white);
		graphics.drawString(mp.toString() + "/" + maxMP.toString(), xStart + 26, yStart + 8);
		
		percent = level.getExpPercent();
		xStart = expRect.x;
		xWidth = (int)(expRect.width * percent);
		yStart = expRect.y;
		yWidth = expRect.height;
		
		graphics.setColor(new Color(1.0f, 1.0f, 1.0f));
		graphics.fillRect(xStart, yStart, xWidth, yWidth);
		
		graphics.drawImage(barTexture, expRect.x - 4, expRect.y - 3, null);
		
		if(stats.pointsRemaining()) {
			graphics.setColor(new Color(0.7f, 0.7f, 0.7f));
			graphics.fillRect(28 * 32, 20 * 32 + 12, 32, 32);
			
			graphics.setFont(fontStats);
			graphics.setColor(Color.white);
			graphics.drawString("O", 28 * 32 + 4, 20 * 32 + 24);
		}
	}

	public void setHealth(Health h) {
		this.health = h;
	}
	
	public void setMana(Mana m) {
		this.mana = m;
	}
}
