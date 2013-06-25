package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Utils.Vector2;

import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.npc.states.EnemyStateMachine;

public class CheckPoint extends GameObject implements ITriggerableOnKey {
	private static BufferedImage texture;
	
	private Game game;
	private DynamicObjectManager dManager;
	private Collision collision;
	
	private Tilemap map;
	private String mapName;
	private ArrayList<CheckPointInfo> infos;
	private CheckPointInfo playerInfo;
	private Rectangle rect;
	
	public CheckPoint(Game game, DynamicObjectManager dManager, Collision collision, Tilemap map, Rectangle rect) {
		this.game = game;
		this.dManager = dManager;
		this.collision = collision;
		this.map = map;
		this.rect = rect;
		
		screenPosition = new Vector2(rect.x, rect.y);
		
		try {
			texture = ImageIO.read(new File("res/goal.png"));
		} catch (IOException e) {
			System.out.println("Could not load image.");
			e.printStackTrace();
		}
		
		infos = new ArrayList<CheckPointInfo>();
	}
	
	public void save(Tilemap map) {
		this.mapName = new String(map.getName());
		ArrayList<Actor> dynamics = dManager.getActors();
		infos.clear();
		
		for (Actor a : dynamics) {
			ActorDescription aDesc = a.getActorDesc();
			EnemyDescription eDesc = null;
			NPCDescription nDesc = null;
			boolean isBoss = false;
			if (a instanceof BossEnemy) {
				BossEnemy b = (BossEnemy)a;
				eDesc = b.getDescription();
				nDesc = b.getNPCDescription();
				isBoss = true;
			}
			//Save enemies
			else if(a instanceof BaseEnemy) {
				BaseEnemy e = (BaseEnemy)a;
				eDesc = e.getDescription();
				nDesc = e.getNPCDescription();
			}
			//Save all BaseNPCs
			else if (a instanceof BaseNPC) {
				BaseNPC n = (BaseNPC)a;
				nDesc = n.getNPCDescription();
			} 
			//Save player
			else if (a instanceof NewPlayer) {
				NewPlayer p = (NewPlayer)a;
				playerInfo = new CheckPointInfo(p.getID(),
									p.getHealth().getCurrentHealth(),
									p.getHealth().getMaxHealth(),
									p.getMana().getCurrentMana(),
									p.getMana().getMaxMana(),
									p.getHeading().ordinal(),
									p.getState().ordinal(),
									p.getStats(),
									p.getCollisionRect(),
									aDesc,
									nDesc,
									eDesc,
									isBoss);
				continue;
			} 
			//Save all other actors
			else {
				
			}
			
			CheckPointInfo info = new CheckPointInfo(a.getID(),
								a.getHealth().getCurrentHealth(),
								a.getHealth().getMaxHealth(),
								a.getMana().getCurrentMana(),
								a.getMana().getMaxMana(),
								a.getHeading().ordinal(),
								a.getState().ordinal(),
								a.getStats(),
								a.getCollisionRect(),
								aDesc,
								nDesc,
								eDesc,
								isBoss);
			
			infos.add(info);
		}
	}
	
	public void load() {
		map.loadMap(mapName);
		
		dManager.clearObjects();
		collision.clearDynamicObjects();
		
		for(CheckPointInfo i : infos) {
			EnemyDescription eDesc = i.getEnemyDesc();
			NPCDescription nDesc = i.getNpcDesc();
			ActorDescription aDesc = i.getActDesc();
			if(eDesc != null) {
				if(!eDesc.getIsRanged()) {
					if(i.isBoss()) {
						BossEnemy b = new BossEnemy(game, i);
						b.setMachine(new EnemyStateMachine(b, game.getPlayer()));
						continue;
					}
					MeleeEnemy e = new MeleeEnemy(game, i);
					e.setMachine(new EnemyStateMachine(e, game.getPlayer()));
					continue;
				}
			} else if(nDesc != null) {
				
			}
		}
		
		game.getPlayer().resetCheckpoint(playerInfo);
		dManager.addObject(game.getPlayer());
		//dManager.addPlayer(game.getPlayer());
	}

	@Override
	public void trigger(Actor actor) {
		game.saveCheckPoint(this);
	}

	@Override
	protected BufferedImage getTexture() {
		return texture;
	}

	@Override
	public Rectangle getCollisionRect() {
		return rect;
	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rectangle getTriggerArea() {
		return rect;
	}
}
