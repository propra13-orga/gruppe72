package fart.dungeoncrawler.actor;

import fart.dungeoncrawler.enums.QuestObjectiveType;
import fart.dungeoncrawler.items.BaseItem;
import fart.dungeoncrawler.items.ItemCollection;

/**
 * The QuestObjectiveCollect is an objective that asks for collecting specific items.
 * @author Erhan
 *
 */
public class QuestObjCollect extends QuestObjective {
	private int number;
	private int itemIndex;
	
	private int currentNumber;
	
	/**
	 * Creates the objective. 
	 * @param number number of items to be collected
	 * @param itemIndex index of the item to be collected
	 */
	public QuestObjCollect(int number, int itemIndex) {
		super(QuestObjectiveType.Collect);
		
		this.number = number;
		this.itemIndex = itemIndex;
		currentNumber = 0;
	}
	
	/**
	 * This method is called when the player picks up an item to check if it was part of this objective.
	 * @param index
	 */
	public void collected(int index) {
		if(index == itemIndex)
			currentNumber += 1;
	}
	
	/**
	 * Returns the overall number of items to be collected.
	 * @return
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Returns the number of items already collected.
	 * @return
	 */
	public int getCurrent() {
		return currentNumber;
	}
	
	/**
	 * Returns the item that has to be collected. 
	 * @return
	 */
	public BaseItem getItem() {
		return ItemCollection.getInstance().getByID(itemIndex);
	}
	
	@Override
	public boolean fulfilled() {
		return currentNumber >= number;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof QuestObjCollect) {
			QuestObjCollect q = (QuestObjCollect)o;
			return itemIndex == q.itemIndex && number == q.number;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 43 * itemIndex;
		hash += 23 * number;
		
		return hash;
	}
}
