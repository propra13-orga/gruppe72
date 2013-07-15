package fart.dungeoncrawler.items;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.actor.Stats;

/**
 * This is the abstract base class for all equipped items. It holds a Stats-field which contains
 * the stats that are added when wearing the item. It also creates a new tooltip-image to include
 * the stat-increase.
 * @author Felix
 *
 */
public abstract class StatItem extends BaseItem {
	protected Stats stats;
	
	public StatItem(String name, String tooltip, String iconPath,
			int priceOnBuy, boolean consumed, Stats stats) {
		super(name, tooltip, iconPath, priceOnBuy, consumed);
		this.stats = stats;
		
		generateTooltipImageWithStats();
	}

	/**
	 * Returns the stats to be added when worn.
	 * @return
	 */
	public Stats getStats() {
		return stats;
	}
	
	/**
	 * Generates a tooltip-image including the stats. 
	 */
	protected void generateTooltipImageWithStats() {
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
		
		height += ss.size() * border;
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
	}
	
	@Override
	public void use(Actor owner) {
		Stats s = owner.getStats();
		s.addStamina(stats.getStamina());
		s.addStrength(stats.getStrength());
		s.addAgility(stats.getAgility());
		s.addWill(stats.getWill());
		s.addArmor(stats.getArmor());
		s.addDamage(stats.getDamage());
		s.addSpellDamage(stats.getSpellDamage());
	}
	
	public void unuse(Actor owner) {
		Stats s = owner.getStats();
		s.redStamina(stats.getStamina());
		s.redStrength(stats.getStrength());
		s.redAgility(stats.getAgility());
		s.redWill(stats.getWill());
		s.redArmor(stats.getArmor());
		s.redDamage(stats.getDamage());
		s.redSpellDamage(stats.getSpellDamage());
	}
}
