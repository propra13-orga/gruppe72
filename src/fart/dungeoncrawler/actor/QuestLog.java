package fart.dungeoncrawler.actor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Utils.Vector2;

import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.enums.QuestObjectiveType;

public class QuestLog implements IDrawable {
	private static Color bgColor = new Color(0.8f, 0.8f, 0.4f);
	private static Font fontHeader = new Font("Arial", 0x1, 24);
	private static Font font = new Font("Arial", 0x1, 12);
	private static Color fontColor = new Color(0.1f, 0.1f, 0.35f);
	private static Vector2 startPos = new Vector2(18 * 32, 2 * 32);
	
	private NewPlayer owner;
	private ArrayList<Quest> quests;
	private boolean containsNew;
	
	public QuestLog(NewPlayer owner) {
		this.owner = owner;
		quests = new ArrayList<Quest>();
		
		/*QuestObjKill qok = new QuestObjKill(2, "MapEditorSave");
		ArrayList<QuestObjective> qosk = new ArrayList<QuestObjective>();
		qosk.add(qok);
		Quest qk = new Quest("KillMobs", 50, 50, qosk);
		
		QuestObjKillBoss qokb = new QuestObjKillBoss("MapEditorSave");
		ArrayList<QuestObjective> qoskb = new ArrayList<QuestObjective>();
		qoskb.add(qokb);
		Quest qkb = new Quest("KillBoss", 50, 50, qoskb);
		
		QuestObjCollect qoc = new QuestObjCollect(1, 3);
		ArrayList<QuestObjective> qosc = new ArrayList<QuestObjective>();
		qosc.add(qoc);
		Quest qc = new Quest("Collect", 50, 50, qosc);
		
		ArrayList<QuestObjective> qosAll = new ArrayList<QuestObjective>();
		qosAll.add(qokb);
		qosAll.add(qoc);
		Quest qAll = new Quest("Together", 10, 10, qosAll);*/
		
		/*addQuest(qAll);
		addQuest(qk);
		addQuest(qkb);
		addQuest(qc);*/
		
	}
	
	public boolean containsNew() {
		return containsNew;
	}
	
	public void setContainsNew(boolean containsNew) {
		this.containsNew = containsNew;
	}
	
	public void addQuest(Quest quest) {
		quests.add(quest);
		quest.setQuestLog(this);
		containsNew = true;
	}
	
	public boolean contains(Quest q) {
		for(int i = 0; i < quests.size(); i++) {
			if(quests.get(i).equals(q))
				return true;
		}
		
		return false;
	}
	
	public Quest getQuest(Quest q) {
		for(int i = 0; i < quests.size(); i++) {
			if(quests.get(i).equals(q))
				return quests.get(i);
		}
		
		return null;
	}
	
	public void removeQuest(Quest quest) {
		if(quests.contains(quest)) {
			quests.remove(quest);
		}
	}
	
	public void mobKilled(String mapName) {
		for(int i = 0; i < quests.size(); i++)
			quests.get(i).mobKilled(owner, mapName);
		
		//checkQuestsDone();
	}
	
	public void bossKilled(String mapName) {
		for(int i = 0; i < quests.size(); i++)
			quests.get(i).bossKilled(owner, mapName);
		
		//checkQuestsDone();
	}
	
	public void itemCollected(int index) {
		for(int i = 0; i < quests.size(); i++)
			quests.get(i).itemCollected(index);
		
		//checkQuestsDone();
	}
	
	/*private void checkQuestsDone() {
		for(int i = 0; i < quests.size(); i++)
			quests.get(i).checkQuestDone(owner);
	}*/

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
