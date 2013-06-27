package fart.dungeoncrawler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import Utils.DamageCalculator;

import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.enums.DynamicObjectState;

public class DynamicObjectManager {
	private ArrayList<Actor> dynamics = new ArrayList<Actor>();
	private ArrayList<SpellProjectile> projectiles = new ArrayList<SpellProjectile>();
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private Game game;
	
	public DynamicObjectManager(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
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
	
	public ArrayList<Actor> getActors() {
		return dynamics;
	}
	
	//spieler und objekte updaten
	public void update(float elapsed) {
		//DEBUG
		//if(attackRects.size() != 0)
		//	attackRects.clear();
		
		for(int i = 0; i < dynamics.size(); i++)
			dynamics.get(i).update(elapsed);
		
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
	
	public void handleAttack(Actor attacker, Attack attack) {
		int id = attacker.getID();
		Rectangle attackRect = attack.getRect(attacker.getHeading());
		
		for(int i = 0; i < dynamics.size(); i++) {
			Actor defender = dynamics.get(i);
			if(defender.getID() == id)
				continue;
			
			if(defender.getCollisionRect().intersects(attackRect)) {
				float dmg = DamageCalculator.calcDamage(attacker, defender);
				defender.getHealth().reduceHealth(dmg);
				defender.setState(DynamicObjectState.Hit);
				System.out.println(defender.getHealth().getCurrentHealth());
				if(defender.getHealth().isDead())
					defender.terminate();
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
		
		drawHealthbars(g2d);
		
		if(Game.debugDraw) {
			drawCollisionRects(g2d);
			game.getCollision().drawCollisionRects(g2d);
		}
	}
	
	public void drawHealthbars(Graphics2D graphics) {
		int startX = 0;
		int startY = 0;

		for(int i = 0; i < dynamics.size(); i++) {
			Actor actor = dynamics.get(i);
			if((actor instanceof BaseEnemy) && actor.getHealth().getCurrentHealth() < actor.getHealth().getMaxHealth()) {
				
				Rectangle rect = actor.getCollisionRect();
				startX = (int)rect.x;
				startY = (int)rect.y - 6;
				int w = (int)rect.getWidth();
				int h = 4;
				float perc = (float)actor.getHealth().getCurrentHealth() / (float)actor.getHealth().getMaxHealth();
				w *= perc;
				
				graphics.setColor(new Color(1.0f - perc , perc, 0.0f, 0.6f));
				graphics.fillRect(startX, startY, w, h);
			}
		}
	}
	
	public void drawCollisionRects(Graphics2D graphics) {
		graphics.setColor(Color.cyan);
		
		for(int i = 0; i < projectiles.size(); i++) {
			Rectangle r = projectiles.get(i).getCollisionRect();
			graphics.drawRect(r.x, r.y, (int)r.getWidth(), (int)r.getHeight());
		}
	}
}
