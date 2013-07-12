package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

import fart.dungeoncrawler.enums.ElementType;

public class SpellFireBold extends Spell {
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
