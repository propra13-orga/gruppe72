package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import Utils.DamageCalculator;

import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.enums.DynamicObjectState;

public class DynamicObjectManager {
	private ArrayList<Actor> dynamics = new ArrayList<Actor>();
	private NewPlayer player;
	private int playerID;
	private ArrayList<SpellProjectile> projectiles = new ArrayList<SpellProjectile>();
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private Game game;
	
	public DynamicObjectManager(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void addPlayer(NewPlayer player) {
		this.player = player;
		this.playerID = player.getID();
	}
	
	public void addObject(Actor npc) {
		dynamics.add(npc);
	}
	
	//objekt entfernen
	public void removeObject(Actor npc) {
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
		
		for(Actor npc : dynamics)
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
			for(Actor npc : dynamics) {
				if(npc.getID() != ownerID && rect.intersects(npc.getCollisionRect())) {
					Health health = npc.getHealth();
					if(!health.isDead()) {
						health.reduceHealth(curSpell.getDamage());
						npc.setState(DynamicObjectState.Hit);
						projectiles.remove(i);
						return;
					}
				}
			}
		}
	}
	
	//wird aufgerufen, wenn ein objekt oder der spieler angreift. 
	/*public void handleAttack(Attack attack, Actor attacker) {
		Rectangle attackRect = attack.getRect(player.getHeading());
		int id = attacker.getID();
		
		if(id == playerID) {
			for(Actor npc : dynamics) {
				if(npc.getCollisionRect().intersects(attackRect)) {
					//objekt getroffen
					int dmg = (int)DamageCalculator.calcDamage(attacker,  npc);
					npc.getHealth().reduceHealth(dmg);
					npc.setState(DynamicObjectState.Hit);
				}
			}
		} else {
			if(player.getCollisionRect().intersects(attackRect)) {
				//spieler getroffen
				int dmg = (int)DamageCalculator.calcDamage(attacker,  player);
				player.getHealth().reduceHealth(dmg);
				if(player.getHealth().isDead()) {
					//spieler tot
				}
			}
		}
	}*/
	
	public void handleAttack(Attack attack, int attackerID) {
		if(attackerID == playerID) {
			for(Actor npc : dynamics) {
				Rectangle attackRect = attack.getRect(player.getHeading());
				if(npc.getCollisionRect().intersects(attackRect)) {
					int dmg = (int)DamageCalculator.calcDamage(player, npc);
					npc.getHealth().reduceHealth(dmg);
					npc.setState(DynamicObjectState.Hit);
					System.out.println(npc.getHealth().getCurrentHealth());
				}
			}
		} else {
			Actor attacker = null;
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
				int dmg = (int)DamageCalculator.calcDamage(attacker, player);
				Health health = player.getHealth();
				health.reduceHealth(dmg);
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
		for(Actor npc : dynamics)
			npc.draw(g2d);
		
		for(SpellProjectile projectile : projectiles)
			projectile.draw(g2d);
		
		player.draw(g2d);
	}
}
