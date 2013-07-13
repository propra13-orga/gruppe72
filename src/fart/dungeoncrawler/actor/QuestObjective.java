package fart.dungeoncrawler.actor;

import fart.dungeoncrawler.enums.QuestObjectiveType;

public abstract class QuestObjective {
	protected QuestObjectiveType type;
	
	public QuestObjective(QuestObjectiveType type) {
		this.type = type;
	}
	
	public QuestObjectiveType getType() {
		return type;
	}
	
	public abstract boolean fulfilled();
}
