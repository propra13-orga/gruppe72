package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.npc.BaseNPC;

public class DynamicObjectManager {
	private ArrayList<BaseNPC> dynamics;
	private Player player;
	private int playerID;
	
	public DynamicObjectManager(Player player) {
		this.player = player;
		this.playerID = player.getID();
		
		dynamics = new ArrayList<BaseNPC>();
	}
	
	public void addObject(BaseNPC npc) {
		dynamics.add(npc);
	}
	
	//objekt entfernen
	public void removeObject(BaseNPC npc) {
		int id = npc.getID();
		for(int i = 0; i < dynamics.size(); i++) {
			if(dynamics.get(i).getID() == id) {
				dynamics.remove(i);
				break;
			}
		}
	}

	//alle objekte löschen
	public void clearObjects() {
		dynamics.clear();
	}
	
	//spieler und objekte updaten
	public void update(float elapsed) {
		player.update(elapsed);
		
		for(BaseNPC npc : dynamics)
			npc.update(elapsed);
	}
	
	//wird aufgerufen, wenn ein objekt oder der spieler angreift. 
	public void handleAttack(Attack attack, GameObject attacker) {
		Rectangle attackRect = attack.getRect();
		int id = attacker.getID();
		
		if(id == playerID) {
			for(BaseNPC npc : dynamics) {
				if(npc.getCollisionRect().intersects(attackRect)) {
					//objekt getroffen
					npc.getHealth().reduceHealth(attack.getDamage());
					npc.activateState(DynamicObjectState.Hit);
				}
			}
		} else {
			if(player.getCollisionRect().intersects(attackRect)) {
				//spieler getroffen
				player.getHealth().reduceHealth(attack.getDamage());
				if(player.getHealth().isDead()) {
					//spieler tot
				}
			}
		}
	}
	
	//zeichnet alle objekte und den spieler
	public void draw(Graphics2D g2d) {
		for(BaseNPC npc : dynamics) {
			npc.draw(g2d);
		}
		
		player.draw(g2d);
	}
	
	public void draw() {
		//??
	}
}
