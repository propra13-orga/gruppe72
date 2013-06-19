package fart.dungeoncrawler;

public class TrapDescription extends BaseDescription {
	private int damage;
	
	public TrapDescription(String spritePath, int damage) {
		super(spritePath);
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}
}
