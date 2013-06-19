package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.npc.BaseNPC;

public class DynamicObjectManager {
	private ArrayList<BaseNPC> dynamics = new ArrayList<BaseNPC>();
	private Player player;
	private int playerID;
	private ArrayList<SpellProjectile> projectiles = new ArrayList<SpellProjectile>();
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	
	public void addPlayer(Player player) {
		this.player = player;
		this.playerID = player.getID();
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
		
		for(BaseNPC npc : dynamics)
			npc.update(elapsed);
		
		for(int i = 0; i < spells.size(); i++) {
			Spell spell = spells.get(i);
			spell.update(elapsed);
			if(!spell.isOnCooldown())
				spells.remove(i);
		}
		
		for(SpellProjectile proj : projectiles) {
			proj.update(elapsed);
		}
		
		for(int i = 0; i < projectiles.size(); i++) {
			SpellProjectile curSpell = projectiles.get(i);
			if(curSpell.getCollision().isCollidingStatic(curSpell)) {
				projectiles.remove(i);
				continue;
			}
				
			int ownerID = curSpell.getOwnerID();
			Rectangle rect = curSpell.getCollisionRect();
			for(BaseNPC npc : dynamics) {
				if(npc.getID() != ownerID && rect.intersects(npc.getCollisionRect())) {
					Health health = npc.getHealth();
					if(!health.isDead()) {
						health.reduceHealth(curSpell.getDamage());
						npc.activateState(DynamicObjectState.Hit);
						projectiles.remove(i);
						return;
					}
				}
			}
		}
	}
	
	public void handleAttack(Attack attack, int attackerID) {
		if(attackerID == playerID) {
			for(BaseNPC npc : dynamics) {
				Rectangle attackRect = attack.getRect(player.getHeading());
				if(npc.getCollisionRect().intersects(attackRect)) {
					npc.getHealth().reduceHealth(attack.getDamage());
					npc.activateState(DynamicObjectState.Hit);
					System.out.println(npc.getHealth().getCurrentHealth());
				}
			}
		} else {
			BaseNPC attacker = null;
			for(int i = 0; i < dynamics.size(); i++) {
				if(dynamics.get(i).getID() == attackerID) {
					attacker = dynamics.get(i);
					break;
				}
			}
			if(attacker == null)
				return;
			Rectangle attackRect = attack.getRect(attacker.getHeading());
			if(player.getCollisionRect().intersects(attackRect)) {
				Health health = player.getHealth();
				health.reduceHealth(attack.getDamage());
				if(!health.isInvul())
					System.out.println("Player is hit. HP: " + health.getCurrentHealth());
				health.setInvul(true);
				
				player.setState(DynamicObjectState.Hit);
				if(health.isDead()) {
					player.terminate();
				}
			}
		}
	}
	
	public void spawnSpell(GameObject attacker, SpellProjectile proj, Spell spell) {
		projectiles.add(proj);
		spells.add(spell);
	}
	
	public void draw(Graphics2D g2d) {
		for(BaseNPC npc : dynamics)
			npc.draw(g2d);
		
		for(SpellProjectile projectile : projectiles)
			projectile.draw(g2d);
		
		player.draw(g2d);
	}
}
