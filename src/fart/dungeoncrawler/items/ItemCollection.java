package fart.dungeoncrawler.items;

import java.util.ArrayList;

import fart.dungeoncrawler.actor.Stats;

public class ItemCollection {
	private ArrayList<BaseItem> itemCollection;
	private static ItemCollection instance;
	
	private ItemCollection() {
		itemCollection = new ArrayList<BaseItem>();
		
		createItems();
	}
	
	public static void createNewInstace() {
		instance = new ItemCollection();
	}
	
	public static ItemCollection getInstance() {
		if(instance == null) {
			instance = new ItemCollection();
		}
		
		return instance;
	}
	
	private void createItems() {
		itemCollection.add(new Potion("Small Healpotion", "Restores 40 Healthpoints.", "res/icons/smallhealpotion.png", 30, 40, 0));
		itemCollection.add(new Potion("Small Manapotion", "Restores 40 Manapoints", "res/icons/smallmanapotion.png", 30, 0, 40));
		
		Stats laStats = new Stats(0, 0, 0, 0, 1000);
		itemCollection.add(new Armor("Leather Armor", "Simple Leather Armor, that slightly reduces the physical damage", "res/icons/leatherarmor.png", 5, false, laStats));
		Stats lgStats = new Stats(0, 0, 0, 0, 5);
		itemCollection.add(new Gloves("Leather Gloves", "Simple Leather Gloves, that slightly reduce the physical damage", "res/icons/leathergloves.png", 5, false, lgStats));
		Stats lbStats = new Stats(0, 0, 0, 0, 7);
		itemCollection.add(new Boots("Leather Boots", "Simple Leather Boots, that slightly reduce the physical damage", "res/icons/leatherboots.png", 5, false, lbStats));
		Stats lhStats = new Stats(0, 0, 0, 0, 10);
		itemCollection.add(new Helmet("Rusty Helmet", "Simple Helmet, that slightly reduce the physical damage", "res/icons/rustyhelmet.png", 5, false, lhStats));
		
		Stats ringStats = new Stats(3, 0, 0, 2, 0);
		itemCollection.add(new Ring("Ring", "Ring", "res/icons/ring1.png", 5, false, ringStats));
		
		Stats weaponStats = new Stats(1, 1, 1, 0, 0, 7, 0);
		itemCollection.add(new Weapon("Sword", "A twohanded Sword", "res/icons/sword.png", 20, false, weaponStats));
		Stats staffStats = new Stats(1, 0, 0, 2, 0, 2, 4);
		itemCollection.add(new Weapon("Staff", "A twohanded Staff", "res/icons/staff.png", 20, false, staffStats));
	}
	
	public BaseItem getByID(int id) {
		return itemCollection.get(id);
	}
}
