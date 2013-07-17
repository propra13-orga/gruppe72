package fart.dungeoncrawler.actor;

import java.util.ArrayList;

/**
 * In this (singleton-)class all quests are created and stored. When creating an NPCQuest it only
 * has to get a list of indices for the questcollection.
 * @author Erhan
 *
 */
public class QuestCollection {
	private static QuestCollection instance;
	
	private ArrayList<Quest> quests;
	
	private QuestCollection() {
		createQuests();
	}
	
	/**
	 * Creates all quests that are part of the game.
	 */
	private void createQuests() {
		quests = new ArrayList<Quest>();
		
		QuestObjKill q1o1 = new QuestObjKill(1, "FirstChallenge");
		Quest q1 = new Quest("Warm Up", 60, 10, q1o1);
		quests.add(q1);
		
		QuestObjCollect q2o1 = new QuestObjCollect(1, 7);
		Quest q2 = new Quest("First Weapon", 30, 30, q2o1);
		quests.add(q2);
		
		QuestObjKill q3o1 = new QuestObjKill(4, "SecMap");
		Quest q3 = new Quest("Kill some more!", 120, 0, q3o1);
		quests.add(q3);
		
		//QuestObjKill q3o1 = new QuestObjKill(5, "MapEditorSave");
	}
	
	/**
	 * Returns the (singleton-)instance.
	 * @return
	 */
	public static QuestCollection getInstance() {
		if(instance == null)
			instance = new QuestCollection();
		
		return instance;
	}
	
	/**
	 * Returns a quest by its ID. The ID is used as an index in the list of all quests. 
	 * @param id
	 * @return
	 */
	public static Quest getQuestByID(int id) {
		return instance.quests.get(id);
	}
}
