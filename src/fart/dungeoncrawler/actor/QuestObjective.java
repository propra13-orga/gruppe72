package fart.dungeoncrawler.actor;

import fart.dungeoncrawler.enums.QuestObjectiveType;

/**
 * An abstract base class for all quest-objectives. Every quest contains one or more objectives that
 * have to be fulfilled to finish the quest. This base class only holds the objective-type.
 * @author Erhan
 *
 */
public abstract class QuestObjective {
	protected QuestObjectiveType type;
	
	/**
	 * Creates the base with the given type. 
	 * @param type
	 */
	public QuestObjective(QuestObjectiveType type) {
		this.type = type;
	}
	
	/**
	 * Returns the objective-type.
	 * @return
	 */
	public QuestObjectiveType getType() {
		return type;
	}
	
	/**
	 * An abstract method that every objectivetype has to override. Returns if the objective is fulfilled. 
	 * @return
	 */
	public abstract boolean fulfilled();
}
