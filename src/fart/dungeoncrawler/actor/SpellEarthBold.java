package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

import fart.dungeoncrawler.enums.ElementType;

public class SpellEarthBold extends Spell {

	public SpellEarthBold(BufferedImage icon, SpellProjectile projectile) {
		super(12, 180, ElementType.Earth, icon, projectile, 30, 2.2f);
	}

	@Override
	public void levelUp() {
		this.level += 1;
		
		if(level == 2) {
			manaCost *= 0.7f;
		} else if (level == 3) {
			damage += 6;
			cooldown -= 30;
		}
	}

}
