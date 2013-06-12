package fart.dungeoncrawler;

import java.util.ArrayList;

import fart.dungeoncrawler.npc.BaseNPC;

public class DynamicObjectManager {
	private ArrayList<BaseNPC> dynamics;
	private Player player;
	
	public DynamicObjectManager(Player player) {
		this.player = player;
		
		dynamics = new ArrayList<BaseNPC>();
	}
	
	public void addObject(BaseNPC npc) {
		dynamics.add(npc);
	}
	
	public void removeObject(BaseNPC npc) {
		int id = npc.getID();
		for(int i = 0; i < dynamics.size(); i++) {
			if(dynamics.get(i).getID() == id) {
				dynamics.remove(i);
				break;
			}
		}
	}

	public void clearObjects() {
		dynamics.clear();
	}
	
	public void update(float elapsed) {
		player.update(elapsed);
	}
	
	public void draw() {
		//??
	}
}
