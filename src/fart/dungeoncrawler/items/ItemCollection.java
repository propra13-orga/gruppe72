package fart.dungeoncrawler.items;

import java.util.ArrayList;

import fart.dungeoncrawler.actor.ElementalDamage;
import fart.dungeoncrawler.actor.Stats;

/*
 * 		 ID	ITEM  -  FOR XML FILES ALWAYS ADD 100 TO ID
 * 		------------------------------
 * 		  0	Small Healpotion
 * 		  1 Small Manapotion
 * 		  2 Leather Armor
 * 		  3	Leather Gloves
 * 		  4	Leather Boots
 * 		  5	Rusty Helmet
 * 		  6	Ring
 * 		  7	Sword
 * 		  8 Staff
 */

/**
 * This singleton-class creates a collection of all items. New items have to be added inside createItems().
 * When starting the game this collection is automatically created so that other elements can access every
 * item in the game. 
 * @author Felix
 *
 */
public class ItemCollection {
	private ArrayList<BaseItem> itemCollection;
	private static ItemCollection instance;
	
	private ItemCollection() {
		itemCollection = new ArrayList<BaseItem>();
		
		createItems();
	}
	
	/**
	 * Creates a new instance of the ItemCollection with all items in it. 
	 */
	public static void createNewInstace() {
		if(instance == null)
			instance = new ItemCollection();
	}
	
	/**
	 * Returns the (singleton-)instance.
	 * @return
	 */
	public static ItemCollection getInstance() {
		if(instance == null) {
			instance = new ItemCollection();
		}
		
		return instance;
	}
	
	/**
	 * Creates all items that should be part of the game.
	 */
	private void createItems() {
		itemCollection.add(new Potion("Small Healpotion", "Restores 40 Healthpoints.", "res/icons/smallhealpotion.png", 30, 40, 0));
		itemCollection.add(new Potion("Small Manapotion", "Restores 40 Manapoints", "res/icons/smallmanapotion.png", 30, 0, 40));
		
		Stats laStats = new Stats(0, 0, 0, 0, 17, 0, 0);
		itemCollection.add(new Armor("Leather Armor", "Simple Leather Armor, that slightly reduces the physical damage", "res/icons/leatherarmor.png", 5, false, laStats));
		Stats lgStats = new Stats(0, 0, 0, 0, 5, 0, 0);
		itemCollection.add(new Gloves("Leather Gloves", "Simple Leather Gloves, that slightly reduce the physical damage", "res/icons/leathergloves.png", 5, false, lgStats));
		Stats lbStats = new Stats(0, 0, 0, 0, 7, 0, 0);
		itemCollection.add(new Boots("Leather Boots", "Simple Leather Boots, that slightly reduce the physical damage", "res/icons/leatherboots.png", 5, false, lbStats));
		Stats lhStats = new Stats(0, 0, 0, 0, 10, 0, 0);
		itemCollection.add(new Helmet("Rusty Helmet", "Simple Helmet, that slightly reduce the physical damage", "res/icons/rustyhelmet.png", 5, false, lhStats));
		
		Stats ringStats = new Stats(3, 0, 0, 2, 0, 0, 0);
		itemCollection.add(new Ring("Ring", "Ring", "res/icons/ring1.png", 5, false, ringStats));
		
		Stats weaponStats = new Stats(1, 1, 1, 0, 0, 3, 0);
		itemCollection.add(new Weapon("Sword", "A twohanded Sword", "res/icons/sword.png", 20, false, weaponStats, new ElementalDamage(0, 0, 4), 2300));
		Stats staffStats = new Stats(1, 0, 0, 2, 0, 2, 4);
		itemCollection.add(new Weapon("Staff", "A twohanded Staff", "res/icons/staff.png", 20, false, staffStats, new ElementalDamage(0, 0, 0), 3300));
	}
	
	/**
	 * Returns the item with the specific itemID.
	 * @param id
	 * @return
	 */
	public BaseItem getByID(int id) {
		return itemCollection.get(id);
	}
	
	/**
	 * Returns the number of all items. 
	 * @return
	 */
	public int getItemCount() {
		return itemCollection.size();
	}
}
