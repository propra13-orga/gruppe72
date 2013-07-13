package fart.dungeoncrawler.actor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import Utils.DamageCalculator;
import Utils.Vector2;

import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.Health;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.enums.ElementType;
import fart.dungeoncrawler.network.DeathMatchStatistics;
import fart.dungeoncrawler.network.Server;
import fart.dungeoncrawler.network.messages.game.GameHitMessage;
import fart.dungeoncrawler.network.messages.game.GamePlayerKilledMessage;

public class DynamicObjectManager {
	private ArrayList<Actor> dynamics = new ArrayList<Actor>();
	private ArrayList<SpellProjectile> projectiles = new ArrayList<SpellProjectile>();
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private ArrayList<Attack> attacks = new ArrayList<Attack>();
	private Game game;
	private boolean updateLogic = true;
	
	public DynamicObjectManager(Game game, boolean updateLogic) {
		this.game = game;
		this.updateLogic = updateLogic;
	}
	
	public void addObject(Actor npc) {		
		dynamics.add(npc);
	}
	
	public Actor getActorByID(int actorID) {
		for(Actor a : dynamics) {
			if(a.getActorID() == actorID)
				return a;
		}
		
		return null;
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

	//alle objekte l�schen
	public void clearObjects() {
		dynamics.clear();
	}
	
	public ArrayList<Actor> getActors() {
		return dynamics;
	}
	
	//spieler und objekte updaten
	public void update(float elapsed) {
		for(int i = 0; i < dynamics.size(); i++) {
			Actor a = dynamics.get(i);
			if(updateLogic)
				if(a instanceof BaseEnemy)
					((BaseEnemy)a).updateLogic(elapsed);
			
			a.update(elapsed);
		}
		
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
			
			if(game.isInNetwork() && !Server.isOnline())
				continue;
			
			//attack has already hit a target, so we skip it
			if(a.hasHit())
				continue;
			
			int attackerID = a.getOwner().getActorID();
			Rectangle rect = a.getRect();
			for(int j = 0; j < dynamics.size(); j++) {
				Actor defender = dynamics.get(j);
				
				//attacker should not hurt hisself
				if(defender.getActorID() == attackerID)
					continue;
				
				if(rect.intersects(defender.getCollisionRect())) {
					a.hit();
					float dmg = DamageCalculator.calcDamage(a.getOwner(), defender);
					if(Server.isOnline()) {
						reduceHealth(dmg, a.getOwner(), defender, true);
						Server.getInstance().broadcastMessage(new GameHitMessage(defender, defender.getHealth().getCurrentHealth(), false));
					} else if (!game.isInNetwork()) {
						reduceHealth(dmg, a.getOwner(), defender, true);
					}
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
				
			if(!game.isInNetwork() || game.isServer()) {
				int ownerID = curSpellProj.getOwnerID();
				Rectangle rect = curSpellProj.getCollisionRect();
				for(Actor npc : dynamics) {
				if(npc.getID() != ownerID && rect.intersects(npc.getCollisionRect())) {
						Health health = npc.getHealth();
						if(!health.isDead()) {
							projectiles.remove(i);
							float dmg = DamageCalculator.calcSpellDamage(curSpellProj.getDamage(), curSpell.getType(), curSpellProj.getOwner(), npc);
							reduceHealth(dmg, curSpellProj.getOwner(), npc, true);
							if(Server.isOnline())
								Server.getInstance().broadcastMessage(new GameHitMessage(npc, npc.getHealth().getCurrentHealth(), true));
							return;
						}
					}
				}
			}
			
		}
	}
	
	public void removeProjectileInNetwork(NewPlayer a) {
		float minDis = 9999;
		int minIndex = 9999;
		Vector2 pos = a.getPosition();
		for(int i = 0; i < projectiles.size(); i++) {
			float d = pos.distance(projectiles.get(i).getPosition());
			if(d < minDis) {
				minDis = d;
				minIndex = i;
			}
		}
		
		if(minIndex != 9999)
			projectiles.remove(minIndex);
	}
	
	private void reduceHealth(float dmg, Actor attacker, Actor defender, boolean hitState) {
		defender.getHealth().reduceHealth(dmg);
		
		if(hitState)
			defender.setState(DynamicObjectState.Hit);
		if(defender.getHealth().isDead()) {
			if(Server.isOnline()) {
				System.out.println("**SERVER: Terminating player " + defender.actorID);
				defender.terminate();
				game.playerDeadInNetwork(defender.actorID);
				Server.getInstance().broadcastMessage(new GamePlayerKilledMessage((byte)attacker.getActorID(), (byte)defender.getActorID()));
			} else {
				System.out.println("**CLIENT: Terminating player " + defender.actorID);
				defender.terminate();
			}
			
			if(!game.isInNetwork()) {
				if(attacker instanceof NewPlayer) {
					int exp = Level.getMobExperienceForLevel(defender.getLevel().getLevel());
					attacker.getLevel().addExperince(exp);
					System.out.println("Gained " + exp + " EXP.");
					System.out.println("Player EXP: " + attacker.getLevel().getCurrentExperience() + "/" + attacker.getLevel().getExperienceForLevelUp());
					
					NewPlayer p = (NewPlayer)attacker;
					if(defender instanceof MeleeEnemy)
						p.getQuestLog().mobKilled(game.getMapName());
					else if(defender instanceof BossEnemy) {
						//p.getQuestLog().mobKilled(game.getMapName());
						p.getQuestLog().bossKilled(game.getMapName());
					}
						
				}
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
			if(((actor instanceof BaseEnemy) && actor.getHealth().getCurrentHealth() < actor.getHealth().getMaxHealth()) ||
					game.isInNetwork()) {
				
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

	public void setUpdateLogic(boolean updateLogic) {
		this.updateLogic = updateLogic;
	}
}
