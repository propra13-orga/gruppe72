package fart.dungeoncrawler.actor;

import fart.dungeoncrawler.enums.QuestObjectiveType;
import fart.dungeoncrawler.items.BaseItem;
import fart.dungeoncrawler.items.ItemCollection;

public class QuestObjCollect extends QuestObjective {
	private int number;
	private int itemIndex;
	
	private int currentNumber;
	
	public QuestObjCollect(int number, int itemIndex) {
		super(QuestObjectiveType.Collect);
		
		this.number = number;
		this.itemIndex = itemIndex;
		currentNumber = 0;
	}
	
	public void collected(int index) {
		if(index == itemIndex)
			currentNumber += 1;
	}
	
	public int getNumber() {
		return number;
	}
	
	public int getCurrent() {
		return currentNumber;
	}
	
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
