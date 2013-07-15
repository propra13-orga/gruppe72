package fart.dungeoncrawler.actor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import fart.dungeoncrawler.Controller;
import fart.dungeoncrawler.items.IconController;
import fart.dungeoncrawler.network.NetworkManager;

import Utils.Vector2;

/**
 * The stats menu draws the players stats to the screen and is used after a levelup to
 * add some statpoints. 
 * @author Felix
 *
 */
public class StatsMenu {
	private static Color bgColor = new Color(0.8f, 0.8f, 0.4f);
	private static Font font = new Font("Arial", 0x1, 14);
	private static Font font2 = new Font("Arial", 0x1, 12);
	private static Color fontColor = new Color(0.1f, 0.1f, 0.35f);
	private static Vector2 startPos = new Vector2(25 * 32, 6 * 32);
	
	private Stats stats;
	private int remainingPoints;
	private Controller controller;
	private Player player;
	private IconController iconController;
	
	/**
	 * Creates the menu for the given stats and player. Controller is needed for choosing stats to add
	 * after a levelup. 
	 * @param stats
	 * @param controller
	 * @param player
	 */
	public StatsMenu(Stats stats, Controller controller, Player player) {
		this.stats = stats;
		this.controller = controller;
		this.player = player;
		this.iconController = new IconController(controller, new Vector2(startPos.x + 16, startPos.y + 10), 8, 1, 4);
	}
	
	/**
	 * This method is called when the player levels up. After every levelup the player gains Stats.STATS_PER_LEVEL
	 * statpoints to add. 
	 */
	public void leveledUp() {
		remainingPoints += Stats.STATS_PER_LEVEL;
	}
	
	/**
	 * Returns the number of available statpoints. 
	 * @return
	 */
	public boolean pointsRemaining() {
		return remainingPoints > 0;
	}
	
	/**
	 * Updates the menu when it is open and the player has points remaining. 
	 */
	public void update() {
		if(remainingPoints > 0) {
			iconController.update(0);
			
			if(controller.justPressed(KeyEvent.VK_ENTER)) {
				int index = iconController.getCurrentIndex();
				
				if (index == 0)
					stats.addStamina(1);
				else if (index == 1)
					stats.addStrength(1);
				else if (index == 2)
					stats.addAgility(1);
				else if (index == 3)
					stats.addWill(1);
				
				remainingPoints -= 1;
				player.renewHealthMana();
				
				if(player.isInNetwork) {
					NetworkManager.sendStatsMessage(player);
				}
			}
		}
		
	}
	
	/**
	 * Draws the menu. 
	 * @param graphics
	 */
	public void drawMenu(Graphics2D graphics) {
		graphics.setColor(bgColor);
		graphics.fillRect((int)startPos.x, (int)startPos.y, 7 * 32, 8 * 32);
		
		graphics.setColor(fontColor);
		graphics.setFont(font);
		
		int x = (int)startPos.x + 64;
		int y = (int)startPos.y + 32;
		
		Integer v = stats.getStamina();
		graphics.drawString("Stamina", x, y);
		graphics.drawString(v.toString(), x + 32 * 4, y);
		y += 40;
		
		v = stats.getStrength();
		graphics.drawString("Strength", x, y);
		graphics.drawString(v.toString(), x + 32 * 4, y);
		y += 40;
		
		v = stats.getAgility();
		graphics.drawString("Agility", x, y);
		graphics.drawString(v.toString(), x + 32 * 4, y);
		y += 40;
		
		v = stats.getWill();
		graphics.drawString("Will", x, y);
		graphics.drawString(v.toString(), x + 32 * 4, y);
		y += 40;
		
		graphics.setFont(font2);
		
		v = stats.getArmor();
		graphics.drawString("Armor", x, y);
		graphics.drawString(v.toString(), x + 32 * 4, y);
		y += 18;
		
		v = stats.getDamage();
		graphics.drawString("Damage", x, y);
		graphics.drawString(v.toString(), x + 32 * 4, y);
		y += 18;
		
		v = stats.getSpellDamage();
		graphics.drawString("SpellDamage", x, y);
		graphics.drawString(v.toString(), x + 32 * 4, y);
		y += 18;
		
		if(remainingPoints > 0) {
			drawStatButtons(graphics);
			iconController.draw(graphics);
		}
	}
	
	/**
	 * Draws buttons when the player has points remaining. 
	 * @param graphics
	 */
	private void drawStatButtons(Graphics2D graphics) {
		int x = (int)startPos.x + 16;
		int y = (int)startPos.y + 10;
		
		graphics.setColor(new Color(0.8f, 0.8f, 0.8f));
		
		for(int i = 0; i < 4; i++) {
			graphics.fillRect(x, y + i * 40, 32, 32);
		}
	}
}
