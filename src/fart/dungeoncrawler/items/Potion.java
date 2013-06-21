package fart.dungeoncrawler.items;

public class Potion extends BaseItem {
	private int healthRestore;
	private int manaRestore;
	
	public Potion(String name, String tooltip, String iconPath, int priceOnBuy, int healthRestore, int manaRestore) {
		super(name, tooltip, iconPath, priceOnBuy, true);
		this.name = name;
		this.healthRestore = healthRestore;
		this.manaRestore = manaRestore;
		this.tooltip = tooltip;
	}
	
	@Override
	public void use() {
		System.out.println("Used potion.");
	}
}
