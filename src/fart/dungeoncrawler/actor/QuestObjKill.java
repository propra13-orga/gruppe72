package fart.dungeoncrawler.actor;

import fart.dungeoncrawler.enums.QuestObjectiveType;

public class QuestObjKill extends QuestObjective {
	private int number;
	private String map;
	
	private int currentNumber;
	
	public QuestObjKill(int number, String map) {
		super(QuestObjectiveType.Kill);
		
		this.number = number;
		this.map = map;
		currentNumber = 0;
	}
	
	public int getNumber() {
		return number;
	}
	
	public int getCurrent() {
		return currentNumber;
	}
	
	public String getMap() {
		return map;
	}
	
	public void killed(String mapName) {
		if(map.equals(mapName))
			currentNumber += 1;
	}
	
	@Override
	public boolean fulfilled() {
		return currentNumber >= number;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof QuestObjKill) {
			QuestObjKill q = (QuestObjKill)o;
			return (q.map.equals(map) && q.number == number);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		for(int i = 0; i < map.length(); i++)
			hash += 31 * (int)map.charAt(i);
		
		hash += 23 * number;
		
		return hash;
	}
}
