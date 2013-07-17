package fart.dungeoncrawler.actor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Utils.Vector2;

import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.enums.QuestObjectiveType;

/**
 * The QuestLog holds all currently active quests. It can draw itself to the screen. 
 * @author Erhan
 *
 */
public class QuestLog implements IDrawable {
	private static Color bgColor = new Color(0.8f, 0.8f, 0.4f);
	private static Font fontHeader = new Font("Arial", 0x1, 24);
	private static Font font = new Font("Arial", 0x1, 12);
	private static Color fontColor = new Color(0.1f, 0.1f, 0.35f);
	private static Vector2 startPos = new Vector2(18 * 32, 2 * 32);
	
	private ArrayList<Quest> quests;
	private boolean containsNew;
	private Player owner;
	
	/**
	 * Creates a new QuestLog.
	 */
	public QuestLog(Player p) {
		quests = new ArrayList<Quest>();
		owner = p;
	}
	
	/**
	 * Indicates if a new quest is in the log since the player last opened it. 
	 * @return
	 */
	public boolean containsNew() {
		return containsNew;
	}
	
	/**
	 * Sets the flag indicating if a new quest is in the log.
	 * @param containsNew
	 */
	public void setContainsNew(boolean containsNew) {
		this.containsNew = containsNew;
	}
	
	/**
	 * Adds a new quest to the log.
	 * @param quest
	 */
	public void addQuest(Quest quest) {
		quests.add(quest);
		quest.setQuestLog(this);
		containsNew = true;
		quest.checkItems();
	}
	
	/**
	 * Checks if the log already contains a specific quest.
	 */
	public boolean contains(Quest q) {
		for(int i = 0; i < quests.size(); i++) {
			if(quests.get(i).equals(q))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a specific quest from the log. 
	 * @param q
	 * @return
	 */
	public Quest getQuest(Quest q) {
		for(int i = 0; i < quests.size(); i++) {
			if(quests.get(i).equals(q))
				return quests.get(i);
		}
		
		return null;
	}
	
	/**
	 * Removes a quest from the log. 
	 * @param quest
	 */
	public void removeQuest(Quest quest) {
		if(quests.contains(quest)) {
			quests.remove(quest);
		}
	}
	
	/**
	 * This method is called when the player has killed an enemy. Every quest inside the log checks if this
	 * was part of the quest.
	 * @param mapName
	 */
	public void mobKilled(String mapName) {
		for(int i = 0; i < quests.size(); i++)
			quests.get(i).mobKilled(mapName);
	}
	
	/**
	 * This method is called when the player has killed a boss. Every quest inside the log checks if this
	 * was part of the quest.
	 * @param mapName
	 */
	public void bossKilled(String mapName) {
		for(int i = 0; i < quests.size(); i++)
			quests.get(i).bossKilled(mapName);
	}
	
	/**
	 * This method is called when the player picks up an item. Every quest inside the log checks if this
	 * was part of the quest.
	 * @param index
	 */
	public void itemCollected(int index) {
		for(int i = 0; i < quests.size(); i++)
			quests.get(i).itemCollected(index);
	}
	
	public Player getOwner() {
		return owner;
	}

	@Override
	public void draw(Graphics2D graphics) {
		containsNew = false;
		
		graphics.setColor(bgColor);
		graphics.fillRect((int)startPos.x, (int)startPos.y, 15 * 32, 16 * 32);
		
		graphics.setColor(fontColor);
		graphics.setFont(fontHeader);
		graphics.drawString("QuestLog", startPos.x + 64, startPos.y + 32);
		
		graphics.setColor(fontColor);
		graphics.setFont(font);
		
		String text = "";
		ArrayList<QuestObjective> os;
		int yOffsetQ = 0;
		
		for(int i = 0; i < quests.size(); i++) {
			Quest q = quests.get(i);
			os = q.getObjectives();
			
			graphics.drawString(q.getName(), startPos.x + 16, startPos.y + 64 + i * 32 + yOffsetQ);
			
			int yOffsetO = yOffsetQ;
			
			for(int j = 0; j < os.size(); j++) {
				QuestObjective obj = os.get(j);
				if(obj.getType() == QuestObjectiveType.Collect) {
					QuestObjCollect o = (QuestObjCollect)obj;
					text = "Collect " + o.getNumber() + " x " + o.getItem().getName();
					graphics.drawString(text.toString(), startPos.x + 16 + 4 * 32, startPos.y + 64 + i * 32 + j * 16 + yOffsetO);
					graphics.drawString(o.getCurrent() + "/" + o.getNumber(), startPos.x + 16 + 12 * 32, startPos.y + 64 + i * 32 + j * 16 + yOffsetO);
				} 
				else if (obj.getType() == QuestObjectiveType.Kill) {
					QuestObjKill o = (QuestObjKill)obj;
					text = "Kill " + o.getNumber() + " enemies in " + o.getMap();
					graphics.drawString(text.toString(), startPos.x + 16 + 4 * 32, startPos.y + 64 + i * 32 + j * 16 + yOffsetO);
					graphics.drawString(o.getCurrent() + "/" + o.getNumber(), startPos.x + 16 + 12 * 32, startPos.y + 64 + i * 32 + j * 16 + yOffsetO);
				} 
				else if (obj.getType() == QuestObjectiveType.KillBoss) {
					QuestObjKillBoss o = (QuestObjKillBoss)obj;
					text = "Kill the Boss in " + o.getMap();
					int c = o.fulfilled() ? 1 : 0;
					graphics.drawString(text, startPos.x + 16 + 4 * 32, startPos.y + 64 + i * 32 + j * 16 + yOffsetO);
					graphics.drawString(c + "/" + 1, startPos.x + 16 + 12 * 32, startPos.y + 64 + i * 32 + j * 16 + yOffsetO);
				}
				if(j >= 1)
					yOffsetQ += 16;
			}
		}
	}
}
