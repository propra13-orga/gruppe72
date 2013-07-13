package fart.dungeoncrawler.actor;

import fart.dungeoncrawler.enums.QuestObjectiveType;

public class QuestObjKillBoss extends QuestObjective {
	private String map;
	private boolean killed;
	
	public QuestObjKillBoss(String map) {
		super(QuestObjectiveType.KillBoss);
		
		this.map = map;
		killed = false;
	}
	
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
