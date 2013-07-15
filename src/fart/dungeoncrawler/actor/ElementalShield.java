package fart.dungeoncrawler.actor;

import java.awt.Rectangle;

import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.enums.ElementType;

/**
 * Represents an elemental shield. The player can activate it and gain some stat and/or damage bonus.
 * With such a shield the player gets the appropriate ElementType. 
 * @author Felix
 *
 */
public class ElementalShield {
	private Actor owner;
	private ElementType elementType;
	private Rectangle area;
	private float areaDamage;
	private Stats statIncrease;
	
	/**
	 * Private constructor to create an ElementalShield. Use the static functions get>Element<Shield to create
	 * one.
	 * @param owner owner of the shield
	 * @param type elemental type
	 * @param areaOfEffect the area of effect for fireshields
	 * @param dmgInArea damage inside the area of effect
	 * @param statIncrease stats that are added to the owners stats
	 */
	private ElementalShield(Actor owner, ElementType type, Rectangle areaOfEffect, float dmgInArea, Stats statIncrease) {
		this.owner = owner;
		elementType = type;
		area = areaOfEffect;
		areaDamage = dmgInArea;
		this.statIncrease = statIncrease;
	}
	
	/**
	 * Creates a fireshield. This adds some firedamage to the weapon and does a small area of effect damage 
	 * around the player.
	 * @param owner owner of the shield
	 * @return the fire shield
	 */
	public static ElementalShield getFireShield(Actor owner) {
		Stats stats = new Stats(0, 0, 0, 0, 0, (int)(owner.getLevel().getLevel() * 1.25f), 0);
		ElementalShield result = new ElementalShield(owner, 
													ElementType.Fire, 
													new Rectangle(-4, -4, Tilemap.TILE_SIZE + 8, Tilemap.TILE_SIZE + 8), 
													owner.getLevel().getLevel() * 0.025f,
													stats);
		
		return result;
	}
	
	/**
	 * Creates a watershield. This increases will and spell-damage.
	 * @param owner owner of the shield
	 * @return the watershield
	 */
	public static ElementalShield getWaterShield(Actor owner) {
		Stats stats = new Stats(0, 0, 0, (int)(owner.getLevel().getLevel() * 0.75f), 0, 0, (int)(owner.getLevel().getLevel() * 1.25f));
		ElementalShield result = new ElementalShield(owner, 
													ElementType.Water, 
													new Rectangle(0, 0, 0, 0), 
													0,
													stats);
		
		return result;
	}
	
	/**
	 * Creates an earthshield. This shield increases stamina and the armor of the player.
	 * @param owner owner of the shield
	 * @return the earthshield
	 */
	public static ElementalShield getEarthShield(Actor owner) {
		Stats stats = new Stats((int)(owner.getLevel().getLevel() * 0.75f), 0, 0, 0, owner.getLevel().getLevel() * 10, 0, 0);
		ElementalShield result = new ElementalShield(owner, 
													ElementType.Earth, 
													new Rectangle(0, 0, 0, 0), 
													0,
													stats);
		
		return result;
	}
	
	/**
	 * Actives the shield which adds the shieldstats to the owners stats and sets the elemental type of
	 * the owner. 
	 */
	public void activate() {
		owner.getStats().addStats(statIncrease);
		owner.setElementType(elementType);
	}
	
	/**
	 * Subtracts the previously added stats and sets the elemental type back to None.
	 */
	public void deactivate() {
		owner.getStats().redStats(statIncrease);
		owner.setElementType(ElementType.None);
	}

	/**
	 * Returns the elemental type of the shield.
	 * @return
	 */
	public ElementType getElementType() {
		return elementType;
	}

	/**
	 * Returns the areadamage. 
	 */
	public float getDamage() {
		return areaDamage;
	}

	/**
	 * Returns the area of effect. 
	 * @return
	 */
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
