package fart.dungeoncrawler.actor;

import fart.dungeoncrawler.enums.QuestObjectiveType;

/**
 * The QuestObjectiveKill is an objective that asks the player to kill a specific amount of
 * enemies on a specific map. 
 * @author Erhan
 *
 */
public class QuestObjKill extends QuestObjective {
	private int number;
	private String map;
	
	private int currentNumber;
	
	/**
	 * Creates the objective.
	 * @param number the number of enemies to be killed
	 * @param map the map on which the enemies have to be killed
	 */
	public QuestObjKill(int number, String map) {
		super(QuestObjectiveType.Kill);
		
		this.number = number;
		this.map = map;
		currentNumber = 0;
	}
	
	/**
	 * Returns the overall number of enemies to be killed.
	 * @return
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Returns the number of enemies already killed.
	 * @return
	 */
	public int getCurrent() {
		return currentNumber;
	}
	
	/**
	 * Returns the name of the map on which the enemies have to be killed. 
	 * @return
	 */
	public String getMap() {
		return map;
	}
	
	/**
	 * This method is called when the player has killed an enemy. It checks if it was part of this objective.
	 * @param mapName
	 */
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
