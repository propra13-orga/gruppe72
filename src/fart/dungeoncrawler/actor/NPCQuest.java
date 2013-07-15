package fart.dungeoncrawler.actor;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Utils.Vector2;
import fart.dungeoncrawler.Game;

/**
 * Represents an NPC that can give quests to the player.
 * @author Felix
 *
 */
public class NPCQuest extends NPCTrigger {
	private ArrayList<Quest> quests;
	private int actQuest;
	private Rectangle triggerArea;

	/**
	 * Creates an NPCQuest from an ActorDescription. All quests are created and stored in the QuestCollection
	 * and can be accessed through an index. The ArrayList questIDs should contain all IDs from the quest
	 * this NPC can give. Quests are given one at a time. So the first quest has to be finished before the next
	 * is given to the player. 
	 * @param game instance of the game running
	 * @param position position in screenspace
	 * @param npcDesc the NPCDescription
	 * @param triggerRect the area in which the player can accept the quests
	 * @param questIDs a list of all questIDs this NPC should give
	 */
	public NPCQuest(Game game, Vector2 position, NPCDescription npcDesc, Rectangle triggerRect, ArrayList<Integer> questIDs) {
		super(game, position, npcDesc);
		
		this.triggerArea = triggerRect;

		quests = new ArrayList<Quest>();
		for(int i = 0; i < questIDs.size(); i++) {
			quests.add(QuestCollection.getQuestByID(questIDs.get(i)));
		}
		
		actQuest = 0;
	}

	@Override
	public void trigger(Actor actor) {
		if(actQuest >= quests.size())
			return;
		
		if(actor instanceof Player) {
			Player player = (Player)actor;
			Quest quest = QuestCollection.getQuestByID(actQuest);
			
			QuestLog log = player.getQuestLog();
			
			//bereits angenommen
			if(log.contains(quest)) {
				if(log.getQuest(quest).checkQuestDone(player)) {
					actQuest += 1;
				}
			} 
			//noch nicht angenommen
			else {
				log.addQuest(quest);
				System.out.println("Added quest to the log");
			}
		} else {
			return;
		}
	}

	@Override
	public Rectangle getTriggerArea() {
		return triggerArea;
	}
	
	@Override
	public BufferedImage getTexture() {
		return this.texture;
	}
}
