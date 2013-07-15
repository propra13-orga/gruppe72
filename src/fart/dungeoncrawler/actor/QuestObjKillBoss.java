package fart.dungeoncrawler.actor;

import fart.dungeoncrawler.enums.QuestObjectiveType;

/**
 * The QuestObjectiveKillBoss is an objective that asks the player to kill a boss on a specific map.
 * @author Erhan
 *
 */
public class QuestObjKillBoss extends QuestObjective {
	private String map;
	private boolean killed;
	
	/**
	 * Creates the objective.
	 * @param map the name of the map on which the boss has to be killed.
	 */
	public QuestObjKillBoss(String map) {
		super(QuestObjectiveType.KillBoss);
		
		this.map = map;
		killed = false;
	}
	
	/**
	 * This method is called when the player has killed a boss. It checks if it was part of this objective.
	 * @param mapName
	 */
	public void killed(String mapName) {
		if(map.equals(mapName))
			killed = true;
	}
	
	@Override
	public boolean fulfilled() {
		return killed;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof QuestObjKillBoss) {
			QuestObjKillBoss q = (QuestObjKillBoss)o;
			return q.map.equals(map);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 23;
		for(int i = 0; i < map.length(); i++)
			hash += 47 * (int)map.charAt(i);
		
		return hash;
	}

	public String getMap() {
		return map;
	}
}
