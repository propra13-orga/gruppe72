package fart.dungeoncrawler.actor;

import java.util.ArrayList;

public class QuestCollection {
	private static QuestCollection instance;
	
	private ArrayList<Quest> quests;
	
	private QuestCollection() {
		createQuests();
	}
	
	private void createQuests() {
		quests = new ArrayList<Quest>();
		
		QuestObjKill q1o1 = new QuestObjKill(1, "MapEditorSave");
		Quest q1 = new Quest("Warm Up", 60, 10, q1o1);
		quests.add(q1);
		
		QuestObjCollect q2o1 = new QuestObjCollect(1, 7);
		Quest q2 = new Quest("First Weapon", 30, 0, q2o1);
		quests.add(q2);
		
		QuestObjKill q3o1 = new QuestObjKill(5, "MapEditorSave");
	}
	
	public static QuestCollection getInstance() {
		if(instance == null)
			instance = new QuestCollection();
		
		return instance;
	}
	
	public static Quest getQuestByID(int id) {
		return instance.quests.get(id);
	}
}
