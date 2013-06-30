package fart.dungeoncrawler.actor;

import java.awt.Rectangle;

import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.enums.ElementType;

public class ElementalShield {
	private Actor owner;
	private ElementType elementType;
	private Rectangle area;
	private float areaDamage;
	private Stats statIncrease;
	
	private ElementalShield(Actor owner, ElementType type, Rectangle areaOfEffect, float dmgInArea, Stats statIncrease) {
		this.owner = owner;
		elementType = type;
		area = areaOfEffect;
		areaDamage = dmgInArea;
		this.statIncrease = statIncrease;
	}
	
	public static ElementalShield getFireShield(Actor owner) {
		Stats stats = new Stats(0, 0, 0, 0, 0, (int)(owner.getLevel().getLevel() * 1.25f), 0, true, false, false);
		ElementalShield result = new ElementalShield(owner, 
													ElementType.Fire, 
													new Rectangle(-4, -4, Tilemap.TILE_SIZE + 8, Tilemap.TILE_SIZE + 8), 
													owner.getLevel().getLevel() * 0.025f,
													stats);
		
		return result;
	}
	
	public static ElementalShield getWaterShield(Actor owner) {
		Stats stats = new Stats(0, 0, 0, (int)(owner.getLevel().getLevel() * 0.75f), 0, 0, (int)(owner.getLevel().getLevel() * 1.25f), false, true, false);
		ElementalShield result = new ElementalShield(owner, 
													ElementType.Water, 
													new Rectangle(0, 0, 0, 0), 
													0,
													stats);
		
		return result;
	}
	
	public static ElementalShield getEarthShield(Actor owner) {
		Stats stats = new Stats((int)(owner.getLevel().getLevel() * 0.75f), 0, 0, 0, owner.getLevel().getLevel() * 10, 0, 0, false, false, true);
		ElementalShield result = new ElementalShield(owner, 
													ElementType.Earth, 
													new Rectangle(0, 0, 0, 0), 
													0,
													stats);
		
		return result;
	}
	
	public void activate() {
		owner.getStats().addStats(statIncrease);
		owner.setElementType(elementType);
	}
	
	public void deactivate() {
		owner.getStats().redStats(statIncrease);
	}

	public ElementType getElementType() {
		return elementType;
	}

	public float getDamage() {
		return areaDamage;
	}

	public Rectangle getAOErect() {
		Rectangle or = owner.getCollisionRect();
		area = new Rectangle(or);
		area.x -= 4;
		area.y -= 4;
		area.width += 8;
		area.height += 8;
		
		return area;
	}

}
