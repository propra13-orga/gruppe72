package fart.dungeoncrawler.items;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fart.dungeoncrawler.actor.ElementalDamage;
import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.enums.EquipSlot;

/**
 * This class represents all kinds of weapons. Beside increasing stats it holds an ElementalDamage-field.
 * This can add ElementalDamage to normal attacks. It also holds the attackSpeed, which is modified by
 * Agility. The physical damage done is part of the Stats-field and is modified by Strength.
 * @author Felix
 *
 */
public class Weapon extends StatItem {
	private ElementalDamage eleDamage;
	private int attackSpeed;

	public Weapon(String name, String tooltip, String iconPath, int priceOnBuy,
			boolean consumed, Stats stats, ElementalDamage eleDamage, int attackSpeed) {
		super(name, tooltip, iconPath, priceOnBuy, consumed, stats);
		
		this.eleDamage = eleDamage;
		this.attackSpeed = attackSpeed;
		
		generateTooltipImageWithStatsElemental();
	}
	
	/**
	 * Generates a tooltip-image including the stats and elemental damage. 
	 */
	protected void generateTooltipImageWithStatsElemental() {
		tooltipImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D)tooltipImage.getGraphics();
		
		graphics.setFont(fontSmall);
		
		String tt = tooltip;
		ArrayList<String> tts = new ArrayList<String>();
		int maxw = 32 * 7 - 8;
		String[] split = tt.split(" ");
		FontMetrics met = graphics.getFontMetrics();
		String cur = "";
		String next = "";

		for(int j = 0; j < split.length; j++) {
			next += " " + split[j];
			if(met.stringWidth(next) < maxw) {
				cur += " " + split[j];
				if(j == split.length - 1)
					tts.add(new String(cur));
				continue;
			} else {
				tts.add(new String(cur));
				cur = new String(split[j]);
				next = "";
			}
		}
		
		int height = 42 + tts.size() * border;
		int width = 32 * 7;
		ArrayList<String> ss = new ArrayList<String>();
		
		if(this instanceof StatItem) {
			StatItem i = (StatItem)this;
			Stats s = i.getStats();
			
			ss.add("");
			
			int v = s.getStamina();
			if(v > 0)
				ss.add("Stamina: " + v);
			
			v = s.getStrength();
			if(v > 0)
				ss.add("Strength: " + v);
			
			v = s.getAgility();
			if(v > 0)
				ss.add("Agility: " + v);
			
			v = s.getWill();
			if(v > 0)
				ss.add("Will: " + v);
			
			v = s.getArmor();
			if(v > 0)
				ss.add("Armor: " + v);
			
			v = s.getDamage();
			if(v > 0)
				ss.add("Damage: " + v);
			
			v = s.getSpellDamage();
			if(v > 0)
				ss.add("SpellDamage: " + v);
		}
		
		ArrayList<String> ed = new ArrayList<String>();
		
		if(eleDamage != null) {
			int v = eleDamage.getFireDamage();
			if(v > 0)
				ed.add("Fire Damage: " + v);
			
			v = eleDamage.getWaterDamage();
			if(v > 0)
				ed.add("Water Damage: " + v);
			
			v = eleDamage.getEarthDamage();
			if(v > 0)
				ed.add("Earth Damage: " + v);
		}
		
		height += ss.size() * border;
		height += ed.size() * border;
		tooltipImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D)tooltipImage.getGraphics();
		
		graphics.setColor(ttBgColor);
		graphics.fillRect(0, 0, width, height);
		
		graphics.setColor(fontColor);
		graphics.setFont(fontBig);
		graphics.drawString(name, 4, 16);
		
		graphics.setFont(fontSmall);
		for(int j = 0; j < tts.size(); j++) {
			graphics.drawString(tts.get(j), 4, 42 + j * border);
		}
		
		for(int j = 0; j < ss.size(); j++) {
			graphics.drawString(ss.get(j), 4, 42 + tts.size() * 12 + j * border);
		}
		
		for(int j = 0; j < ed.size(); j++) {
			graphics.drawString(ed.get(j), 4, 42 + tts.size() * 12 + border + ss.size() * 12 + j * border);
		}
	}
	
	@Override
	public EquipSlot getSlot() {
		return EquipSlot.Weapon;
	}

	public int getAttackSpeed() {
		return attackSpeed;
	}
	
	public ElementalDamage getEleDamage() {
		return eleDamage;
	}
}
