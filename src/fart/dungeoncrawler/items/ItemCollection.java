package fart.dungeoncrawler.items;

import java.util.ArrayList;

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
	}
	
	public BaseItem getByID(int id) {
		return itemCollection.get(id);
	}
}
