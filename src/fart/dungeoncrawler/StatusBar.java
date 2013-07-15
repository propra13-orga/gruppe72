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
import fart.dungeoncrawler.actor.Player;
import fart.dungeoncrawler.actor.QuestLog;
import fart.dungeoncrawler.actor.StatsMenu;

/**
 * This is a GUI-class that draws HP, MP, experiance, Level and the name of the map in a
 * statusbar. 
 * @author Erhan
 *
 */
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
	private QuestLog qLog;
	
	private static Font fontHpMp = new Font("Arial", 0x1, 10);
	private static Font fontStats = new Font("Arial", 0x0, 12);
	private static Font fontLevel = new Font("Arial", 0x1, 14);
	
	/**
	 * Creates the statusbar and gets all information needed from the game instance and the player. 
	 */
	public StatusBar(Player player, Game game) {
		this.health = player.getHealth();
		this.mana = player.getMana();
		this.level = player.getLevel();
		this.game = game;
		this.stats = player.getStatsMenu();
		this.qLog = player.getQuestLog();
		
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
		
		if(qLog != null && qLog.containsNew()) {
			graphics.setColor(new Color(0.8f, 0.8f, 0.4f));
			graphics.fillRect(26 * 32 + 16, 20 * 32 + 12, 32, 32);
			
			graphics.setFont(fontStats);
			graphics.setColor(Color.white);
			graphics.drawString("T", 26 * 32 + 20, 20 * 32 + 24);
		}
		
		if(stats.pointsRemaining()) {
			graphics.setColor(new Color(0.7f, 0.7f, 0.7f));
			graphics.fillRect(28 * 32, 20 * 32 + 12, 32, 32);
			
			graphics.setFont(fontStats);
			graphics.setColor(Color.white);
			graphics.drawString("O", 28 * 32 + 4, 20 * 32 + 24);
		}
	}

	/**
	 * Sets the health-instance which should be drawn. After every change in stats a new
	 * instance is created and has to be set here. 
	 * @param h
	 */
	public void setHealth(Health h) {
		this.health = h;
	}
	
	/**
	 * Sets the mana-instance which should be drawn. After every change in stats a new instance
	 * is created and has to be set here. 
	 * @param m
	 */
	public void setMana(Mana m) {
		this.mana = m;
	}
}
