package fart.dungeoncrawler.actor;

import java.util.ArrayList;

import fart.dungeoncrawler.enums.QuestObjectiveType;

public class Quest{
	
	private QuestLog qLog;
	
	private String name;
	private int expAmount;
	private int goldAmount;
	private ArrayList<QuestObjective> objectives;
	
	public Quest(String name, int exp, int gold, QuestObjective obj) {
		this.name = name;
		this.expAmount = exp;
		this.goldAmount = gold;
		this.objectives = new ArrayList<QuestObjective>();
		objectives.add(obj);
	}
	
	public Quest(String name, int exp, int gold, ArrayList<QuestObjective> objs) {
		this.name = name;
		this.expAmount = exp;
		this.goldAmount = gold;
		this.objectives = objs;
	}
	
	public void setQuestLog(QuestLog log) {
		this.qLog = log;
	}

	private QuestObjKill getKillObjective() {
		for(QuestObjective q : objectives)
			if(q.getType() == QuestObjectiveType.Kill)
				return (QuestObjKill)q;
		
		return null;
	}
	
	private QuestObjKillBoss getBossKillObjective() {
		for(QuestObjective q : objectives)
			if(q.getType() == QuestObjectiveType.KillBoss)
				return (QuestObjKillBoss)q;
		
		return null;
	}
	
	private QuestObjCollect getCollectObjective() {
		for(QuestObjective q : objectives)
			if(q.getType() == QuestObjectiveType.Collect)
				return (QuestObjCollect)q;
		
		return null;
	}
	
	public void mobKilled(NewPlayer player, String mapName) {
		QuestObjKill killObjective = getKillObjective();
		if(killObjective == null)
			return;
		
		killObjective.killed(mapName);
	}
	
	public void bossKilled(NewPlayer player, String mapName) {
		QuestObjKillBoss bossObjective = getBossKillObjective();
		if(bossObjective == null)
			return;
		
		bossObjective.killed(mapName);
	}
	
	public void itemCollected(int index) {
		QuestObjCollect collectObjective = getCollectObjective();
		if(collectObjective == null)
			return;
		
		collectObjective.collected(index);
	}
	
	public boolean checkQuestDone(NewPlayer player) {
		for(QuestObjective q : objectives)
			if(!q.fulfilled())
				return false;
		
		player.getInventory().addGold(goldAmount);
		player.getLevel().addExperince(expAmount);
		
		qLog.removeQuest(this);
		System.out.println("Quest done!");
		return true;
	}
	
	public ArrayList<QuestObjective> getObjectives() {
		return objectives;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Quest) {
			Quest q = (Quest)o;
			if(!name.equals(q.name))
				return false;
			
			if(q.objectives.size() != objectives.size())
				return false;
			
			for(int i = 0; i < objectives.size(); i++) {
				if(!objectives.get(i).equals(q.objectives.get(i)))
					return false;
			}
		} else
			return false;

		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = 3;
		for(int i = 0; i < name.length(); i++)
			hash += 23 * (int)name.charAt(i);
		
		for(QuestObjective o : objectives)
			hash += o.hashCode();
		
		return hash;
	}
}
