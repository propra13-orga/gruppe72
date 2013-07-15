package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

import fart.dungeoncrawler.enums.ElementType;

/**
 * Class for an firebold (damage)spell. 
 * @author Felix
 *
 */
public class SpellFireBold extends Spell {
	/**
	 * Creates a firebold spell. 
	 * @param icon icon to be shown in the GUI
	 * @param projectile the spellprojectile for this spell
	 */
	public SpellFireBold(BufferedImage icon, SpellProjectile projectile) {
		super(15, 120, ElementType.Fire, icon, projectile, 25, 3.0f);
	}

	@Override
	public void levelUp() {
		this.level += 1;
		
		if (level == 2) {
			manaCost *= 0.75f;
		} else if (level == 3) {
			damage += 8;
		}
	}
}
