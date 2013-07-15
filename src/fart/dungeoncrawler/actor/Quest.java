package fart.dungeoncrawler.actor;

import java.util.ArrayList;

import fart.dungeoncrawler.enums.QuestObjectiveType;

/**
 * Represents a quest as a collection of questobjectives. When objectives are fulfilled the quest
 * is done. 
 * @author Erhan
 *
 */
public class Quest{
	
	private QuestLog qLog;
	
	private String name;
	private int expAmount;
	private int goldAmount;
	private ArrayList<QuestObjective> objectives;
	
	/**
	 * Creates a quest with one objective. 
	 * @param name name of the quest
	 * @param exp the amount of experiance the player gains after finishing
	 * @param gold the amount of gold the player gains after finishing
	 * @param obj the questobjective
	 */
	public Quest(String name, int exp, int gold, QuestObjective obj) {
		this.name = name;
		this.expAmount = exp;
		this.goldAmount = gold;
		this.objectives = new ArrayList<QuestObjective>();
		objectives.add(obj);
	}
	
	/**
	 * Creates a quest with all objectives in the list.
	 * @param name name if the quest
	 * @param exp the amount of experiance the player gains after finishing
	 * @param gold the amount of gold the player gains after finishing
	 * @param objs the list of objetives to be done
	 */
	public Quest(String name, int exp, int gold, ArrayList<QuestObjective> objs) {
		this.name = name;
		this.expAmount = exp;
		this.goldAmount = gold;
		this.objectives = objs;
	}
	
	/**
	 * Sets the questLog that contains this quest.
	 * @param log
	 */
	public void setQuestLog(QuestLog log) {
		this.qLog = log;
	}

	/**
	 * Returns the killobjective if it exists. Otherwise returns null.
	 * @return
	 */
	private QuestObjKill getKillObjective() {
		for(QuestObjective q : objectives)
			if(q.getType() == QuestObjectiveType.Kill)
				return (QuestObjKill)q;
		
		return null;
	}
	
	/**
	 * Returns the bosskillobjective if it exists. Otherwise returns null.
	 * @return
	 */
	private QuestObjKillBoss getBossKillObjective() {
		for(QuestObjective q : objectives)
			if(q.getType() == QuestObjectiveType.KillBoss)
				return (QuestObjKillBoss)q;
		
		return null;
	}
	
	/**
	 * Returns the collectobjective if it exists. Otherwise returns null.
	 * @return
	 */
	private QuestObjCollect getCollectObjective() {
		for(QuestObjective q : objectives)
			if(q.getType() == QuestObjectiveType.Collect)
				return (QuestObjCollect)q;
		
		return null;
	}
	
	/**
	 * This method is called when the player has killed an enemy. It checks if it was part
	 * of this quest.
	 * @param mapName name of the current map
	 */
	public void mobKilled(String mapName) {
		QuestObjKill killObjective = getKillObjective();
		if(killObjective == null)
			return;
		
		killObjective.killed(mapName);
	}
	
	/**
	 * This method is called when the player has killed a boss. It checks if it was part
	 * of this quest.
	 * @param mapName name of the current map
	 */
	public void bossKilled(String mapName) {
		QuestObjKillBoss bossObjective = getBossKillObjective();
		if(bossObjective == null)
			return;
		
		bossObjective.killed(mapName);
	}
	
	/**
	 * This method is called when the player picks up an item. It checks if it was part
	 * of this quest.
	 * @param index the itemIndex
	 */
	public void itemCollected(int index) {
		QuestObjCollect collectObjective = getCollectObjective();
		if(collectObjective == null)
			return;
		
		collectObjective.collected(index);
	}
	
	/**
	 * Checks if the quest is done. If so the player gets his rewards and the quest is deleted from
	 * the log.
	 * @param player
	 * @return
	 */
	public boolean checkQuestDone(Player player) {
		for(QuestObjective q : objectives)
			if(!q.fulfilled())
				return false;
		
		player.getInventory().addGold(goldAmount);
		player.getLevel().addExperince(expAmount);
		
		qLog.removeQuest(this);
		return true;
	}
	
	/**
	 * Returns a list of all objectives. 
	 * @return
	 */
	public ArrayList<QuestObjective> getObjectives() {
		return objectives;
	}
	
	/**
	 * Returns the name of the quest.
	 * @return
	 */
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
