package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

import fart.dungeoncrawler.enums.ElementType;

/**
 * Class for an waterbold (damage)spell. 
 * @author Felix
 *
 */
public class SpellWaterBold extends Spell {
	/**
	 * Creates a waterbold spell. 
	 * @param icon icon to be shown in the GUI
	 * @param projectile the spellprojectile for this spell
	 */
	public SpellWaterBold(BufferedImage icon, SpellProjectile projectile) {
		super(8, 100, ElementType.Water, icon, projectile, 20, 2.5f);
	}

	@Override
	public void levelUp() {
		this.level += 1;
		
		if(level == 2) {
			manaCost *= 0.8f;
			damage += 4;
		} else if (level == 3) {
			manaCost *= 0.9f;
			damage += 8;
		}
	}

}
