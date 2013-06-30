package fart.dungeoncrawler.actor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import Utils.DamageCalculator;

import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.Health;
import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.enums.ElementType;

public class DynamicObjectManager {
	private ArrayList<Actor> dynamics = new ArrayList<Actor>();
	private ArrayList<SpellProjectile> projectiles = new ArrayList<SpellProjectile>();
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private ArrayList<Attack> attacks = new ArrayList<Attack>();
	private HashMap<Integer, Integer> attackHits = new HashMap<Integer, Integer>();
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
		
		for(int i = 0; i < dynamics.size(); i++)
			dynamics.get(i).update(elapsed);
		
		for(int i = 0; i < attacks.size(); i++) {
			Attack a = attacks.get(i);
			
			//check if the attack is over
			if(a.update(elapsed)) {
				attacks.remove(i);
				i -= 1;
				a.getOwner().setState(DynamicObjectState.Idle);
				if(a.getOwner() instanceof NewPlayer)
					((NewPlayer)(a.getOwner())).setIdleAnim();
				
				continue;
			}
			
			//attack has already hit a target, so we skip it
			if(a.hasHit())
				continue;
			
			int attackerID = a.getOwner().getID();
			Rectangle rect = a.getRect();
			for(int j = 0; j < dynamics.size(); j++) {
				Actor defender = dynamics.get(j);
				
				//attacker should not hurt hisself
				if(defender.getID() == attackerID)
					continue;
				
				if(rect.intersects(defender.getCollisionRect())) {
					a.hit();
					float dmg = DamageCalculator.calcDamage(a.getOwner(), defender);
					reduceHealth(dmg, a.getOwner(), defender, true);
				}
			}
		}
		
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
			SpellProjectile curSpellProj = projectiles.get(i);
			Spell curSpell = curSpellProj.getSpell();
			if(curSpellProj.getCollision().isCollidingStatic(curSpellProj)) {
				projectiles.remove(i);
				continue;
			}
				
			int ownerID = curSpellProj.getOwnerID();
			Rectangle rect = curSpellProj.getCollisionRect();
			for(Actor npc : dynamics) {
				if(npc.getID() != ownerID && rect.intersects(npc.getCollisionRect())) {
					Health health = npc.getHealth();
					if(!health.isDead()) {
						projectiles.remove(i);
						float dmg = DamageCalculator.calcSpellDamage(curSpellProj.getDamage(), curSpell.getType(), curSpellProj.getOwner(), npc);
						reduceHealth(dmg, curSpellProj.getOwner(), npc, true);
						return;
					}
				}
			}
		}
	}
	
	private void reduceHealth(float dmg, Actor attacker, Actor defender, boolean hitState) {
		defender.getHealth().reduceHealth(dmg);
		if(hitState)
			defender.setState(DynamicObjectState.Hit);
		if(defender.getHealth().isDead()) {
			defender.terminate();
			
			if(attacker instanceof NewPlayer) {
				int exp = Level.getMobExperienceForLevel(defender.getLevel().getLevel());
				attacker.getLevel().addExperince(exp);
				System.out.println("Gained " + exp + " EXP.");
				System.out.println("Player EXP: " + attacker.getLevel().getCurrentExperience() + "/" + attacker.getLevel().getExperienceForLevelUp());
			}
		}
	}
	
	public void registerAttack(Attack attack) {
		attacks.add(attack);
	}
	
	public void spawnSpell(GameObject attacker, SpellProjectile proj, Spell spell) {
		projectiles.add(proj);
	}
	
	public void addSpellToUpdate(Spell spell) {
		spells.add(spell);
	}
	
	public void handleAreaOfEffectSpell(Actor owner, float damage, ElementType type, Rectangle area) {
		for (int i = 0; i < dynamics.size(); i++) {
			Actor a = dynamics.get(i);
			if(a.equals(owner))
				continue;
			
			if(a.getCollisionRect().intersects(area)) {
				float dmg = DamageCalculator.calcSpellDamage(damage, type, owner, a);
				reduceHealth(dmg, owner, a, false);
			}
		}
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
				
				graphics.setColor(new Color(1.0f - perc , perc, 0.0f, 0.8f));
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
