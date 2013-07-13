package fart.dungeoncrawler.actor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Utils.Vector2;

import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.enums.ElementType;

public class SpellManager implements IDrawable, IUpdateable {
	private static final Vector2 START_POS = new Vector2(8 * Tilemap.TILE_SIZE - 20, 20 * Tilemap.TILE_SIZE + 16);
	private static int BORDER = 16;
	private static final int GLOBAL_COOLDOWN = 90;
	private static final int SHIELD_COOLDOWN = 5 * 60;
	private static final Color bgColor = new Color(0.4f, 0.4f, 0.4f);
	private static final Font font = new Font("Arial", 0x0, 12);
	private static final Color fontColor = new Color(1f, 1f, 1f);
	
	private Actor owner;
	private ArrayList<Spell> spells;
	private ElementalShield[] shields = new ElementalShield[3];
	private ElementalShield currentShield;
	private int shieldCooldown;
	
	public SpellManager(Actor owner) {
		spells = new ArrayList<Spell>();
		this.owner = owner;
	}
	
	public void addShields() {
		shields[0] = ElementalShield.getFireShield(owner);
		shields[1] = ElementalShield.getWaterShield(owner);
		shields[2] = ElementalShield.getEarthShield(owner);
		//shieldCooldown = 0;
	}
	
	@SuppressWarnings("incomplete-switch")
	public void activateShield(ElementType type) {
		if(shieldCooldown > 0)
			return;
		
		if(currentShield != null)
			currentShield.deactivate();
		
		switch(type) {
		case Earth:
			currentShield = shields[2];
			break;
		case Fire:
			currentShield = shields[0];
			break;
		case Water:
			currentShield = shields[1];
			break;
		}
		
		currentShield.activate();
		shieldCooldown = SHIELD_COOLDOWN;
		
		if(owner instanceof NewPlayer)
			((NewPlayer)owner).renewHealthMana();
	}
	
	public void addSpell(Spell spell) {
		spells.add(spell);
	}
	
	public Spell getSpell(int index) {
		return spells.get(index);
	}

	@Override
	public void draw(Graphics2D graphics) {
		int x = (int)START_POS.x;
		int y = (int)START_POS.y;
		
		graphics.setColor(bgColor);
		graphics.fillRect(0, y - 16, 32 * 32, 32 * 2);
		
		graphics.setFont(font);
		
		for(int i = 0; i < spells.size(); i++) {
			x += BORDER + Tilemap.TILE_SIZE;
			
			graphics.drawImage(spells.get(i).getIcon(), x, y, null);
			
			graphics.setColor(fontColor);
			String n = "";
			if(i == 0)
				n = "S";
			else if(i == 1)
				n = "D";
			else if(i == 2)
				n = "F";
			
			graphics.drawString(n, x + 4, y + 12);
			
			if(spells.get(i).isOnCooldown()) {
				graphics.setColor(new Color(0.3f, 0.3f, 0.3f, 0.5f));
				graphics.fillRect(x, y, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
			}
		}
		
		if(shields[0] != null) {
			x = (int)START_POS.x + 5 * BORDER + 5 * Tilemap.TILE_SIZE;
			y = (int)START_POS.y;
			graphics.setColor(new Color(1f, 0.6f, 0.0f));
			graphics.fillRect(x, y, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
			graphics.setColor(fontColor);
			graphics.drawString("5", x + 4, y + 12);
			if(shieldCooldown > 0) {
				graphics.setColor(new Color(0.3f, 0.3f, 0.3f, 0.5f));
				graphics.fillRect(x, y, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
			}
		}
		
		if(shields[1] != null) {
			x = (int)START_POS.x + 6 * BORDER + 6 * Tilemap.TILE_SIZE;
			y = (int)START_POS.y;
			graphics.setColor(new Color(0.4f, 0.4f, 1.0f));
			graphics.fillRect(x, y, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
			graphics.setColor(fontColor);
			graphics.drawString("6", x + 4, y + 12);
			if(shieldCooldown > 0) {
				graphics.setColor(new Color(0.3f, 0.3f, 0.3f, 0.5f));
				graphics.fillRect(x, y, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
			}
		}
		
		if(shields[2] != null) {
			x = (int)START_POS.x + 7* BORDER + 7 * Tilemap.TILE_SIZE;
			y = (int)START_POS.y;
			graphics.setColor(new Color(0.2f, 1.0f, 0.2f));
			graphics.fillRect(x, y, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
			graphics.setColor(fontColor);
			graphics.drawString("7", x + 4, y + 12);
			if(shieldCooldown > 0) {
				graphics.setColor(new Color(0.3f, 0.3f, 0.3f, 0.5f));
				graphics.fillRect(x, y, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
			}
		}
	}
	
	public void activate(int index) {
		spells.get(index).activate();
		
		for(int i = 0; i < spells.size(); i++) {
			if (i == index)
				continue;
			
			if(spells.get(i).getCurrentCooldown() < GLOBAL_COOLDOWN)
				spells.get(i).setCurrentCooldown(GLOBAL_COOLDOWN);
		}
	}

	@Override
	public void update(float elapsed) {
		for(int i = 0; i < spells.size(); i++)
			spells.get(i).update(elapsed);

		shieldCooldown -= 1;
		if(shieldCooldown < 0)
			shieldCooldown = 0;
	}

	public ElementalShield getCurrentShield() {
		return currentShield;
	}

}
